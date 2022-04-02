package me.superckl.biometweaker;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biome.ClimateSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeModificationManager {

	private static final Map<ResourceLocation, BiomeModificationManager> modifiers = new Object2ObjectOpenHashMap<>();

	public static BiomeModificationManager forBiome(final ResourceLocation rLoc) {
		return BiomeModificationManager.modifiers.computeIfAbsent(rLoc, loc -> new BiomeModificationManager());
	}

	private ClimateModification climate;
	private EffectsModification effects;
	private GenerationModification generation;
	private MobSpawnModification spawn;
	@Getter
	@Setter
	private BiomeCategory category;

	private BiomeModificationManager() {}

	public boolean hasClimate() {
		return this.climate != null;
	}

	public ClimateModification getClimate() {
		if(this.climate == null)
			this.climate = new ClimateModification();
		return this.climate;
	}

	public boolean hasEffects() {
		return this.effects != null;
	}

	public EffectsModification getEffects() {
		if(this.effects == null)
			this.effects = new EffectsModification();
		return this.effects;
	}

	public boolean hasGeneration() {
		return this.generation != null;
	}

	public GenerationModification getGeneration() {
		if(this.generation == null)
			this.generation = new GenerationModification();
		return this.generation;
	}

	public boolean hasSpawn() {
		return this.spawn != null;
	}

	public MobSpawnModification getSpawn() {
		if(this.spawn == null)
			this.spawn = new MobSpawnModification();
		return this.spawn;
	}

	public boolean hasCategory() {
		return this.category != null;
	}

	@Data
	public static class ClimateModification{

		private Biome.Precipitation precipitation;
		private Optional<Float> temperature = Optional.empty();
		private Biome.TemperatureModifier temperatureModifier;
		private Optional<Float> downfall = Optional.empty();

		public ClimateSettings modify(final ClimateSettings val) {
			return new ClimateSettings(this.precipitation == null ? val.precipitation : this.precipitation,
					this.temperature.orElse(val.temperature),
					this.temperatureModifier == null ? val.temperatureModifier : this.temperatureModifier,
							this.downfall.orElse(val.downfall));
		}

	}

	@Data
	public static class EffectsModification{

		private OptionalInt fogColor = OptionalInt.empty();
		private OptionalInt waterColor = OptionalInt.empty();
		private OptionalInt waterFogColor = OptionalInt.empty();
		private OptionalInt skyColor = OptionalInt.empty();
		private OptionalInt foliageColorOverride = OptionalInt.empty();
		private OptionalInt grassColorOverride = OptionalInt.empty();
		private BiomeSpecialEffects.GrassColorModifier grassColorModifier;

		public BiomeSpecialEffects modify(final BiomeSpecialEffects val) {
			final BiomeSpecialEffects.Builder builder = this.toBuilder(val);

			this.fogColor.ifPresent(builder::fogColor);
			this.waterColor.ifPresent(builder::waterColor);
			this.waterFogColor.ifPresent(builder::waterFogColor);
			this.skyColor.ifPresent(builder::skyColor);
			this.foliageColorOverride.ifPresent(builder::foliageColorOverride);
			this.grassColorOverride.ifPresent(builder::grassColorOverride);
			builder.grassColorModifier(this.grassColorModifier == null ? val.getGrassColorModifier() : this.grassColorModifier);
			return builder.build();
		}

		private BiomeSpecialEffects.Builder toBuilder(final BiomeSpecialEffects val){
			final BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder();
			builder.fogColor(val.getFogColor());
			builder.waterColor(val.getWaterColor());
			builder.waterFogColor(val.getWaterFogColor());
			builder.skyColor(val.getSkyColor());
			val.getFoliageColorOverride().ifPresent(builder::foliageColorOverride);
			val.getGrassColorOverride().ifPresent(builder::grassColorOverride);
			builder.grassColorModifier(val.getGrassColorModifier());
			val.getAmbientParticleSettings().ifPresent(builder::ambientParticle);
			val.getAmbientLoopSoundEvent().ifPresent(builder::ambientLoopSound);
			val.getAmbientMoodSettings().ifPresent(builder::ambientMoodSound);
			val.getAmbientAdditionsSettings().ifPresent(builder::ambientAdditionsSound);
			val.getBackgroundMusic().ifPresent(builder::backgroundMusic);
			return builder;
		}

	}

	public static class MobSpawnModification{

		private final Multimap<MobCategory, ResourceLocation> removedSpawns = MultimapBuilder.enumKeys(MobCategory.class).hashSetValues().build();
		private final Set<MobCategory> allSpawns = EnumSet.noneOf(MobCategory.class);
		private final Multimap<MobCategory, SpawnerData> addedSpawns = MultimapBuilder.enumKeys(MobCategory.class).hashSetValues().build();
		private final Map<ResourceLocation, DoubleDoublePair> costs = new HashMap<>();
		@Setter
		@Getter
		private Optional<Float> probability = Optional.empty();

		public void modify(final MobSpawnSettingsBuilder val) {
			this.allSpawns.forEach(type -> val.getSpawner(type).clear());
			this.removedSpawns.forEach((type, loc) -> val.getSpawner(type).removeIf(spawner -> spawner.type.delegate.name().equals(loc)));
			this.addedSpawns.forEach((type, spawn) -> val.getSpawner(type).add(spawn));
			this.probability.ifPresent(val::creatureGenerationProbability);
			this.costs.forEach((loc, pair) -> val.addMobCharge(ForgeRegistries.ENTITIES.getValue(loc), pair.leftDouble(), pair.rightDouble()));

		}

		public void removeSpawn(final ResourceLocation feature, final MobCategory... types) {
			for (final MobCategory type : types)
				this.removedSpawns.put(type, feature);
		}

		public void removeAllSpawns(final MobCategory type) {
			this.allSpawns.add(type);
		}

		public void addSpawn(final SpawnerData spawn, final MobCategory... types) {
			for (final MobCategory type : types)
				this.addedSpawns.put(type, spawn);
		}

		public void setCost(final ResourceLocation entity, final double costPer, final double maxCost) {
			this.costs.put(entity, DoubleDoublePair.of(costPer, maxCost));
		}

	}

	public static class GenerationModification{

		private final Multimap<Decoration, ResourceLocation> removedFeatures = MultimapBuilder.enumKeys(Decoration.class).hashSetValues().build();
		private final Set<Decoration> allFeatures = EnumSet.noneOf(Decoration.class);
		private final Multimap<Carving, ResourceLocation> removedCarvers = MultimapBuilder.enumKeys(Carving.class).hashSetValues().build();
		private final Set<Carving> allCarvers = EnumSet.noneOf(Carving.class);

		public void removeFeature(final ResourceLocation feature, final Decoration... stages) {
			for (final Decoration stage : stages)
				this.removedFeatures.put(stage, feature);
		}

		public void removeAllFeatures(final Decoration stage) {
			this.allFeatures.add(stage);
		}

		public void removeCarver(final ResourceLocation feature, final Carving... stages) {
			for (final Carving stage : stages)
				this.removedCarvers.put(stage, feature);
		}

		public void removeAllCarvers(final Carving stage) {
			this.allCarvers.add(stage);
		}

		public void modify(final BiomeGenerationSettingsBuilder val) {
			this.allFeatures.forEach(stage -> val.getFeatures(stage).clear());
			this.removedFeatures.forEach((stage, loc) -> val.getFeatures(stage).removeIf(feature -> feature.is(loc)));
			this.allCarvers.forEach(stage -> val.getCarvers(stage).clear());
			this.removedCarvers.forEach((stage, loc) -> val.getCarvers(stage).removeIf(carver -> carver.is(loc)));
		}

	}

}
