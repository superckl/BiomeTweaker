package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;

public class AllButBiomesPackage extends BiomePackage{

	private final BiomePackage exclusions;

	public AllButBiomesPackage(final BiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<ResourceLocation> locIterator(final BiomeLookup lookup) {
		final Set<ResourceLocation> excludes = Streams.stream(this.exclusions.locIterator(lookup)).collect(Collectors.toSet());
		return Iterators.filter(lookup.allKeys(), Predicates.not(excludes::contains));
	}

	@Override
	public boolean requiresRegistry() {
		return true;
	}

}
