package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.Biome;

public interface IBiomePackage{

	/**
	 * Generates an {@link Iterator} that will iterate through all biomes in this package. This is usually generated when the method is called, and not stored.
	 * @return An Iterator that iterates over the viable Biomes, not in any particular order.
	 */
	public Iterator<Biome> getIterator();
	public boolean supportsEarlyRawIds();
	public List<Integer> getRawIds();

}
