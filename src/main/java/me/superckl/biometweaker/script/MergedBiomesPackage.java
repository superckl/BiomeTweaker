package me.superckl.biometweaker.script;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class MergedBiomesPackage implements IBiomePackage{

	List<IBiomePackage> packs = Lists.newArrayList();

	public MergedBiomesPackage(final IBiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> gens = Lists.newArrayList();
		for(final IBiomePackage pack:this.packs)
			Iterators.addAll(gens, pack.getIterator());
		return gens.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		for(final IBiomePackage pack:this.packs)
			if(!pack.supportsEarlyRawIds())
				return false;
		return true;
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> ints = Lists.newArrayList();
		for(final IBiomePackage pack:this.packs)
			ints.addAll(pack.getRawIds());
		return ints;
	}

}
