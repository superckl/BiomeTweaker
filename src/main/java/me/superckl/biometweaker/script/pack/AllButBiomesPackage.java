package me.superckl.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Lists;

public class AllButBiomesPackage implements IBiomePackage{

	private final IBiomePackage exclusions;

	public AllButBiomesPackage(final IBiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if((gen != null) && !ints.contains(gen.biomeID))
				list.add(gen);
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if((gen != null) && !ints.contains(gen.biomeID))
				list.add(gen.biomeID);
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

	@Override
	public List<Integer> getMergeIDExclusions() {
		return this.exclusions.getRawIds();
	}

	@Override
	public Iterator<BiomeGenBase> getExclusionsIterator() {
		return this.exclusions.getIterator();
	}

}
