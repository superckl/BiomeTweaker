package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult.PartialResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import lombok.Cleanup;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;

public class Output {

	public static void generateOutputFiles(final RegistryAccess registry, final Registry<LevelStem> levelRegistry, final File baseDir){
		final DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registry);
		final BiFunction<String, String, Function<Optional<JsonObject>, String>> namer = (key, def) -> opt ->
		opt.map(obj -> new StringBuilder(obj.get(key).getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(".json").toString()).orElse(def);

		if(Config.getInstance().getOutputBiomes().get()) {
			final File biomeDir = new File(baseDir, "/biome/");
			Output.genOutput(Streams.stream(registry.ownedRegistryOrThrow(Registry.BIOME_REGISTRY).iterator()), biomeDir, entry -> {
				final Either<JsonElement, PartialResult<JsonElement>> result = Biome.DIRECT_CODEC.encodeStart(ops, entry).get();
				result.ifRight(pR -> LogHelper.warn("Failed to encode a biome! "+pR.message()));
				return result.left().orElseGet(JsonObject::new).getAsJsonObject();
			}, namer.apply("forge:registry_name", "Biome"));
		}

		if(Config.getInstance().getOutputEntities().get()) {
			final File entityDir = new File(baseDir, "/entity/");
			Output.genOutput(Streams.stream(ForgeRegistries.ENTITIES.iterator()), entityDir, Output::entityTypeToJson, namer.apply("registry_name", "Entity"));
		}

		if(Config.getInstance().getOutputDims().get()) {
			final File dimDir = new File(baseDir, "/dimension/");
			Output.genOutput(levelRegistry.holders(), dimDir, entry -> {
				final JsonObject obj = new JsonObject();
				obj.addProperty("registry_name", entry.key().location().toString());
				final Either<JsonElement, PartialResult<JsonElement>> result = LevelStem.CODEC.encodeStart(ops, entry.value()).get();
				result.ifRight(pR -> LogHelper.warn("Failed to encode an entity! "+pR.message()));
				result.ifLeft(el -> obj.add("level_stem", el));
				return obj;
			}, namer.apply("registry_name", "Dimension"));
		}

		if(Config.getInstance().getOutputFeatures().get()) {
			final File featureDir = new File(baseDir, "/feature/");
			Output.genOutput(registry.ownedRegistryOrThrow(Registry.PLACED_FEATURE_REGISTRY).holders(), featureDir, holder -> {
				final JsonObject obj = new JsonObject();
				obj.addProperty("registry_name", holder.key().location().toString());
				final Either<JsonElement, PartialResult<JsonElement>> result = PlacedFeature.DIRECT_CODEC.encodeStart(ops, holder.value()).get();
				result.ifRight(pR -> LogHelper.warn("Failed to encode a feature! "+pR.message()));
				result.ifLeft(el -> obj.add("placed_feature", el));
				return obj;
			}, namer.apply("registry_name", "Feature"));
		}

		if(Config.getInstance().getOutputCarvers().get()) {
			final File featureDir = new File(baseDir, "/carver/");
			Output.genOutput(registry.ownedRegistryOrThrow(Registry.CONFIGURED_CARVER_REGISTRY).holders(), featureDir, holder -> {
				final JsonObject obj = new JsonObject();
				obj.addProperty("registry_name", holder.key().location().toString());
				final Either<JsonElement, PartialResult<JsonElement>> result = ConfiguredWorldCarver.DIRECT_CODEC.encodeStart(ops, holder.value()).get();
				result.ifRight(pR -> LogHelper.warn("Failed to encode a carver! "+pR.message()));
				result.ifLeft(el -> obj.add("configured_carver", el));
				return obj;
			}, namer.apply("registry_name", "Carver"));
		}
	}

	private static <T, V extends JsonElement> void genOutput(final Stream<T> values, final File dir, final Function<T, V> serializer, final Function<Optional<V>, String> namingStrategy) {
		JsonArray array;
		final String name = namingStrategy.apply(Optional.empty());
		if(Config.getInstance().getOutputDims().get())
			try {
				LogHelper.info(String.format("Generating %s status report...", name));

				dir.mkdirs();
				Output.clearOutput(dir);

				array = new JsonArray();
				values.map(serializer).forEach(array::add);

				Output.writeArray(array, dir, namingStrategy);
			} catch (final Exception e) {
				LogHelper.error(String.format("Caught an exception while generating %s status report!", name));
				e.printStackTrace();
			}
	}

	private static JsonObject entityTypeToJson(final EntityType<?> type) {
		final JsonObject obj = new JsonObject();
		obj.addProperty("registry_name", ForgeRegistries.ENTITIES.getKey(type).toString());
		obj.addProperty("category", type.getCategory().name());
		obj.addProperty("fire_immune", type.fireImmune());
		obj.addProperty("summonable", type.canSummon());
		obj.addProperty("spawn_far_from_player", type.canSpawnFarFromPlayer());
		obj.addProperty("tracking_range", type.clientTrackingRange());
		obj.addProperty("update_interval", type.updateInterval());
		obj.addProperty("defaul_loot_table", type.getDefaultLootTable().toString());
		final JsonObject size = new JsonObject();
		size.addProperty("height", type.getDimensions().height);
		size.addProperty("width", type.getDimensions().width);
		size.addProperty("fixed", type.getDimensions().fixed);
		obj.add("size", size);
		return obj;
	}

	private static void clearOutput(final File dir) {
		for(final File file:dir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();
	}

	private static <T extends JsonElement> void writeArray(final JsonArray array, final File dir, final Function<Optional<T>, String> namingStrategy) throws IOException {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if(Config.getInstance().getSeparateFiles().get())
			for(final JsonElement ele:array){
				final T obj = WarningHelper.uncheckedCast(ele);
				final String fileName = namingStrategy.apply(Optional.of(obj));
				final File dimOutput = new File(dir, fileName);
				if(dimOutput.exists())
					dimOutput.delete();
				dimOutput.createNewFile();
				@Cleanup
				final BufferedWriter writer = new BufferedWriter(new FileWriter(dimOutput));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{

			final File entityOutput = new File(dir, String.format("BiomeTweaker - %s Status Report.json", namingStrategy.apply(Optional.empty())));
			if(entityOutput.exists())
				entityOutput.delete();
			entityOutput.createNewFile();
			@Cleanup
			final BufferedWriter writer = new BufferedWriter(new FileWriter(entityOutput));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(array));
		}
	}

}
