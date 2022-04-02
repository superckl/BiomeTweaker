package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import net.minecraft.resources.ResourceLocation;

public class SubtractBiomesPackage extends BiomePackage{

	private final BiomePackage main;
	private final BiomePackage subtract;

	public SubtractBiomesPackage(final BiomePackage main, final BiomePackage subtract) {
		this.main = main;
		this.subtract = subtract;
	}

	@Override
	public Iterator<ResourceLocation> locIterator() {
		final Set<ResourceLocation> sub = Sets.newHashSet(this.subtract.locIterator());
		return Iterators.filter(this.main.locIterator(), Predicates.not(sub::contains));
	}

	@Override
	public boolean requiresRegistry() {
		return this.main.requiresRegistry() || this.subtract.requiresRegistry();
	}

}
