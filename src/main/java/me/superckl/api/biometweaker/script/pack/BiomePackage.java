package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.Biome;

public abstract class BiomePackage{

	/**
	 * Generates an {@link Iterator} that will iterate through all biomes in this package. This is usually generated when the method is called, and not stored.
	 * @return An Iterator that iterates over the viable Biomes, not in any particular order.
	 */
	public abstract Iterator<Biome> getIterator();
	public abstract boolean supportsEarlyRawIds();
	public abstract List<Integer> getRawIds();

}
