package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.BiomeGenBase;

public class AllBiomesPackage implements IBiomePackage{

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(gen != null)
				list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(gen != null)
				list.add(BiomeGenBase.getIdForBiome(gen));
		}
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

}
