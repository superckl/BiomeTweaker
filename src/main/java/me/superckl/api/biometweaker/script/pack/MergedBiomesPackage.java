package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;

public class MergedBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public MergedBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<ResourceLocation> locIterator(final BiomeLookup lookup) {
		return this.packs.stream().map(pack -> pack.locIterator(lookup)).reduce(Iterators::concat).orElseGet(Collections::emptyIterator);
	}

	@Override
	public boolean requiresRegistry() {
		return this.packs.stream().anyMatch(BiomePackage::requiresRegistry);
	}

}
