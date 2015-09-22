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

}
