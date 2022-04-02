package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AllButBiomesPackage extends BiomePackage{

	private final BiomePackage exclusions;

	public AllButBiomesPackage(final BiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<ResourceLocation> locIterator() {
		final Set<ResourceLocation> excludes = Streams.stream(this.exclusions.locIterator()).collect(Collectors.toSet());
		return Iterators.filter(ForgeRegistries.BIOMES.getKeys().iterator(), Predicates.not(excludes::contains));
	}

	@Override
	public boolean requiresRegistry() {
		return true;
	}

}
