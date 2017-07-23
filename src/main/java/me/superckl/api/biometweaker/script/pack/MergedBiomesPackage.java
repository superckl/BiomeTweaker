package me.superckl.api.biometweaker.script.pack;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;

public class MergedBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = Lists.newArrayList();

	public MergedBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> gens = Lists.newArrayList();
		for(final BiomePackage pack:this.packs)
			Iterators.addAll(gens, pack.getIterator());
		return gens.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		for(final BiomePackage pack:this.packs)
			if(!pack.supportsEarlyRawIds())
				return false;
		return true;
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> ints = Lists.newArrayList();
		for(final BiomePackage pack:this.packs)
			ints.addAll(pack.getRawIds());
		return ints;
	}

}
