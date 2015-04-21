package me.superckl.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Lists;

public class AllBiomesPackage implements IBiomePackage{

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if(gen != null)
				list.add(gen);
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if(gen != null)
				list.add(gen.biomeID);
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

}
