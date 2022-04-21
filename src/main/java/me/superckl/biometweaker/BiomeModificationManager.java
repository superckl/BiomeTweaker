package me.superckl.biometweaker;

import java.util.Collection;
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
import lombok.experimental.Accessors;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biome.ClimateSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeModificationManager {

	public static boolean hasMobEffects = false;
	private static final Map<ResourceLocation, BiomeModificationManager> modifiers = new Object2ObjectOpenHashMap<>();

	public static BiomeModificationManager forBiome(final ResourceLocation rLoc) {
		return BiomeModificationManager.modifiers.computeIfAbsent(rLoc, loc -> new BiomeModificationManager());
	}

	public static Optional<BiomeModificationManager> forBiomeOpt(final ResourceLocation rLoc) {
		return Optional.ofNullable(BiomeModificationManager.modifiers.get(rLoc));
	}

	private ClimateModification climate;
	private EffectsModification effects;
	private GenerationModification generation;
	private MobSpawnModification spawn;
	@Getter @Setter
	private BiomeCategory category;
	@Getter @Setter
	private boolean disableSleep;
	@Getter @Setter
	private boolean disableBonemeal;
	@Getter @Setter
	private boolean disableCropGrowth;
	@Getter @Setter
	private boolean disableSaplingGrowth;

	private final Multimap<EntityType<?>, MobEffectModification> potionEffects = MultimapBuilder.hashKeys().arrayListValues().build();

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

	public void addMobEffect(final EntityType<?> type, final MobEffectModification effect) {
		BiomeModificationManager.hasMobEffects = true;
		this.potionEffects.put(type, effect);
	}

	public Collection<MobEffectModification> getMobEffects(final EntityType<?> type){
		return this.potionEffects.get(type);
	}
	
	public static void checkBiomes() {
		BiomeModificationManager.modifiers.keySet().forEach(loc -> {
			if(!ForgeRegistries.BIOMES.containsKey(loc))
				BiomeTweaker.LOG.error("No biome found for resource location %s. Ensure it is correct and has there is a corresponding biome in the output folder.", loc);
		});
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
		private Optional<SoundEvent> ambientSoundLoop = Optional.empty();
		private Optional<AmbientParticleSettings> particle = Optional.empty();
		private Optional<AmbientAdditionsSettings> additions = Optional.empty();
		private Optional<Music> backgroundMusic = Optional.empty();


		public BiomeSpecialEffects modify(final BiomeSpecialEffects val) {
			final BiomeSpecialEffects.Builder builder = this.toBuilder(val);

			this.fogColor.ifPresent(builder::fogColor);
			this.waterColor.ifPresent(builder::waterColor);
			this.waterFogColor.ifPresent(builder::waterFogColor);
			this.skyColor.ifPresent(builder::skyColor);
			this.foliageColorOverride.ifPresent(builder::foliageColorOverride);
			this.grassColorOverride.ifPresent(builder::grassColorOverride);
			builder.grassColorModifier(this.grassColorModifier == null ? val.getGrassColorModifier() : this.grassColorModifier);
			this.ambientSoundLoop.ifPresent(builder::ambientLoopSound);
			this.particle.ifPresent(builder::ambientParticle);
			this.additions.ifPresent(builder::ambientAdditionsSound);
			this.backgroundMusic.ifPresent(builder::backgroundMusic);
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

		private final Multimap<Decoration, Holder<PlacedFeature>> addedFeatures = MultimapBuilder.enumKeys(Decoration.class).arrayListValues().build();
		private final Multimap<Carving, Holder<ConfiguredWorldCarver<?>>> addedCarvers = MultimapBuilder.enumKeys(Carving.class).arrayListValues().build();

		public void removeFeature(final ResourceLocation feature, final Decoration... stages) {
			for (final Decoration stage : stages)
				this.removedFeatures.put(stage, feature);
		}

		public void removeAllFeatures(final Decoration stage) {
			this.allFeatures.add(stage);
		}

		public void addFeature(final Holder<PlacedFeature> feature, final Decoration... stages) {
			for (final Decoration stage : stages)
				this.addedFeatures.put(stage, feature);
		}

		public void removeCarver(final ResourceLocation feature, final Carving... stages) {
			for (final Carving stage : stages)
				this.removedCarvers.put(stage, feature);
		}

		public void removeAllCarvers(final Carving stage) {
			this.allCarvers.add(stage);
		}

		public void addCarver(final Holder<ConfiguredWorldCarver<?>> feature, final Carving... stages) {
			for (final Carving stage : stages)
				this.addedCarvers.put(stage, feature);
		}

		public void modify(final BiomeGenerationSettingsBuilder val) {
			this.allFeatures.forEach(stage -> val.getFeatures(stage).clear());
			this.removedFeatures.forEach((stage, loc) -> val.getFeatures(stage).removeIf(feature -> feature.is(loc)));
			this.allCarvers.forEach(stage -> val.getCarvers(stage).clear());
			this.removedCarvers.forEach((stage, loc) -> val.getCarvers(stage).removeIf(carver -> carver.is(loc)));

			this.addedFeatures.forEach((stage, holder) -> val.getFeatures(stage).add(holder));
			this.addedCarvers.forEach((stage, holder) -> val.getCarvers(stage).add(holder));
		}

	}


	public static record MobEffectModification(MobEffect effect, int amplifier, int duration, int interval, float chance, boolean visible, boolean showIcon) {

		public MobEffectInstance createInstance() {
			return new MobEffectInstance(this.effect, this.duration, this.amplifier, this.showIcon, this.visible, this.showIcon);
		}

		public static Builder builder(final ResourceLocation effect) {
			return new Builder(effect);
		}

		@Data
		@Accessors(fluent = true)
		public static class Builder{

			private final ResourceLocation effect;
			private int amplifier = 0;
			private int duration = 200;
			private int interval = 198;
			private float chance = 1;
			private boolean visible = false;
			private boolean showIcon = true;

			public MobEffectModification build() throws IllegalArgumentException {
				if(!ForgeRegistries.MOB_EFFECTS.containsKey(this.effect))
					throw new IllegalArgumentException(String.format("No mob effect %s found!", this.effect));
				return new MobEffectModification(ForgeRegistries.MOB_EFFECTS.getValue(this.effect), this.amplifier, this.duration, this.interval, this.chance, this.visible, this.showIcon);
			}

		}

	}

}
