package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class AllBiomesPackage extends BiomePackage{

	@Override
	public Iterator<ResourceLocation> locIterator(final BiomeLookup lookup) {
		return lookup.allKeys();
	}

	@Override
	public Iterator<Biome> iterator(final BiomeLookup lookup) {
		return lookup.allValues();
	}

	@Override
	public boolean requiresRegistry() {
		return true;
	}



}
