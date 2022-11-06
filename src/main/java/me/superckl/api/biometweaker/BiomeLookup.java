package me.superckl.api.biometweaker;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public interface BiomeLookup {

	ResourceLocation key(Biome biome);
	Biome value(ResourceLocation loc);
	Iterator<Biome> allValues();
	Iterator<ResourceLocation> allKeys();

	static BiomeLookup fromForge() {
		return ForgeBiomeLookup.INSTANCE;
	}

	static BiomeLookup fromRegistry(final Registry<Biome> registry) {
		return new RegistryBiomeLookup(registry);
	}

	public static class ForgeBiomeLookup implements BiomeLookup{

		private static final ForgeBiomeLookup INSTANCE = new ForgeBiomeLookup();

		private ForgeBiomeLookup() {}

		@Override
		public ResourceLocation key(final Biome biome) {
			return ForgeRegistries.BIOMES.getKey(biome);
		}

		@Override
		public Biome value(final ResourceLocation loc) {
			return ForgeRegistries.BIOMES.getValue(loc);
		}

		@Override
		public Iterator<Biome> allValues() {
			return ForgeRegistries.BIOMES.iterator();
		}

		@Override
		public Iterator<ResourceLocation> allKeys() {
			return ForgeRegistries.BIOMES.getKeys().iterator();
		}

	}

	@RequiredArgsConstructor
	public static class RegistryBiomeLookup implements BiomeLookup{

		private final Registry<Biome> registry;

		@Override
		public ResourceLocation key(final Biome biome) {
			return this.registry.getKey(biome);
		}

		@Override
		public Biome value(final ResourceLocation loc) {
			return this.registry.get(loc);
		}

		@Override
		public Iterator<Biome> allValues() {
			return this.registry.iterator();
		}

		@Override
		public Iterator<ResourceLocation> allKeys() {
			return this.registry.keySet().iterator();
		}

	}

}
