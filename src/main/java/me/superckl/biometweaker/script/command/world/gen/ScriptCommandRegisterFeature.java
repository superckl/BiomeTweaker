package me.superckl.biometweaker.script.command.world.gen;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

@RequiredArgsConstructor
@AutoRegister(classes = TweakerScriptObject.class, name = "registerFeature")
public class ScriptCommandRegisterFeature extends StagedScriptCommand{

	private final String name;

	@Override
	public void perform() throws Exception {
		final File jsonFile = new File(BiomeTweaker.getINSTANCE().getFeatureDir(), this.name+".json");
		if(!jsonFile.isFile())
			throw new IllegalArgumentException("Failed to find placed feature file "+jsonFile);
		try (FileReader reader = new FileReader(jsonFile)){
			final JsonObject el = JsonParser.parseReader(reader).getAsJsonObject();
			final DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
			final ResourceLocation featureLoc = new ResourceLocation(el.getAsJsonPrimitive("feature").getAsString());
			Holder<ConfiguredFeature<?, ?>> featureHolder = null;
			if(!BuiltinRegistries.CONFIGURED_FEATURE.containsKey(featureLoc)) {
				if(!BiomeTweakerAPI.MOD_ID.equals(featureLoc.getNamespace()))
					throw new IllegalArgumentException(String.format("Failed to find configured feature %s. Location must belong to biometweaker to be created.", featureLoc));
				BiomeTweaker.LOG.debug("Attempting to create and register configured feature %s", featureLoc);
				final File configFile = new File(BiomeTweaker.getINSTANCE().getConfigedFeatureDir(), featureLoc.getPath()+".json");
				if(!configFile.isFile())
					throw new IllegalArgumentException("Failed to find configured feature file "+configFile);
				try (FileReader configReader = new FileReader(configFile)){
					final JsonElement configEl = JsonParser.parseReader(configReader);
					final ConfiguredFeature<?, ?> feature = ScriptCommandRegisterFeature.decode(ops, configEl, ConfiguredFeature.DIRECT_CODEC);
					featureHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, featureLoc, feature);
				}
			}else
				featureHolder = BuiltinRegistries.CONFIGURED_FEATURE.getHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, featureLoc));
			final JsonArray array = el.getAsJsonArray("placement");
			final List<PlacementModifier> modifiers = new ArrayList<>();
			array.forEach(plEl -> modifiers.add(ScriptCommandRegisterFeature.decode(ops, plEl, PlacementModifier.CODEC)));
			final PlacedFeature feature = new PlacedFeature(featureHolder, modifiers);
			final ResourceLocation key = new ResourceLocation(BiomeTweakerAPI.MOD_ID, this.name);
			BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, key, feature);
			BiomeTweaker.LOG.debug("Created and registered feature %s", key);
		} catch (final Exception e) {
			BiomeTweaker.LOG.error("Failed to register placed feature "+this.name, e);
		}
	}

	public static <T, V> T decode(final DynamicOps<V> ops, final V data, final Codec<T> codec) {
		final var result = codec.decode(ops, data).get();
		if(result.right().isPresent())
			throw new IllegalArgumentException(String.format("Failed to decode: %s", result.right().get().message()));
		return result.left().get().getFirst();
	}

	@Override
	public StageRequirement requiredStage() {
		return StageRequirement.LATE;
	}

}
