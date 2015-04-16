package me.superckl.biometweaker.script;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

public interface IBiomePackage{

	public Iterator<BiomeGenBase> getIterator();
	public List<Integer> getRawIds();

}
