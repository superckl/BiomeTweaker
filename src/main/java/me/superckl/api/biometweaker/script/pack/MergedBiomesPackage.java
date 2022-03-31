package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import net.minecraft.world.level.biome.Biome;

public class MergedBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public MergedBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> gens = new ArrayList<>();
		for(final BiomePackage pack:this.packs)
			Iterators.addAll(gens, pack.iterator());
		return gens.iterator();
	}

}
