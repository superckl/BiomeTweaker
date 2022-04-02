package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import net.minecraft.resources.ResourceLocation;

public class IntersectBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public IntersectBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<ResourceLocation> locIterator() {
		final Set<ResourceLocation> allLocs = new HashSet<>();
		this.packs.stream().map(BiomePackage::locIterator).forEach(it -> Iterators.addAll(allLocs, it));
		return this.packs.stream().map(BiomePackage::locIterator).map(Sets::newHashSet).reduce(allLocs, Sets::intersection, Sets::intersection).iterator();
	}

	@Override
	public boolean requiresRegistry() {
		return this.packs.stream().anyMatch(BiomePackage::requiresRegistry);
	}

}
