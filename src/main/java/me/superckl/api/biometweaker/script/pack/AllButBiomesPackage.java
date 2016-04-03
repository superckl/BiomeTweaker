package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.BiomeGenBase;

public class AllButBiomesPackage implements IBiomePackage{

	private final IBiomePackage exclusions;

	public AllButBiomesPackage(final IBiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if((gen != null) && !ints.contains(BiomeGenBase.getIdForBiome(gen)))
				list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(gen == null)
				continue;
			final int id = BiomeGenBase.getIdForBiome(gen);
			if(!ints.contains(id))
				list.add(id);
		}
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

}
