package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import net.minecraft.resources.ResourceLocation;

public class MergedBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public MergedBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<ResourceLocation> locIterator() {
		return this.packs.stream().map(BiomePackage::locIterator).reduce(Iterators::concat).orElseGet(Collections::emptyIterator);
	}

	@Override
	public boolean requiresRegistry() {
		return this.packs.stream().anyMatch(BiomePackage::requiresRegistry);
	}

}
