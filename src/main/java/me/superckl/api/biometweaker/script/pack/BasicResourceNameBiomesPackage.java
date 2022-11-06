package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;

public class BasicResourceNameBiomesPackage extends BiomePackage{

	private final ResourceLocation[] rLocs;

	public BasicResourceNameBiomesPackage(final String ... names) {
		this.rLocs = new ResourceLocation[names.length];
		for(int i = 0; i < names.length; i++)
			this.rLocs[i] = new ResourceLocation(names[i]);
	}

	@Override
	public Iterator<ResourceLocation> locIterator(final BiomeLookup lookup) {
		return Iterators.forArray(this.rLocs);
	}

	@Override
	public boolean requiresRegistry() {
		return false;
	}

}
