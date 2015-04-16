package me.superckl.biometweaker.script;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

public interface IBiomePackage{

	/**
	 * Generates an {@link Iterator} that will iterate through all biomes in this package. This is usually generated when the method is called, and not stored.
	 * @return An Iterator that iterates over the viable BiomeGenBases, not in any particular order.
	 */
	public Iterator<BiomeGenBase> getIterator();
	public List<Integer> getRawIds();

}
