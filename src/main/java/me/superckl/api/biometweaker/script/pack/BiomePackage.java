package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public abstract class BiomePackage{

	public abstract Iterator<ResourceLocation> locIterator(BiomeLookup lookup);

	public Iterator<Biome> iterator(final BiomeLookup lookup) {
		return Iterators.transform(this.locIterator(lookup), lookup::value);
	}

	public abstract boolean requiresRegistry();

}
