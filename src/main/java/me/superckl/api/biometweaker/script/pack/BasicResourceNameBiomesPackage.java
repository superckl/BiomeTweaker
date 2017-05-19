package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.APIInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BasicResourceNameBiomesPackage implements IBiomePackage{

	private final ResourceLocation[] rLocs;

	public BasicResourceNameBiomesPackage(final String ... names) {
		this.rLocs = new ResourceLocation[names.length];
		for(int i = 0; i < names.length; i++)
			this.rLocs[i] = new ResourceLocation(names[i]);
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> biomes = Lists.newArrayList();
		for(final ResourceLocation rLoc:this.rLocs){
			final Biome biome = Biome.REGISTRY.getObject(rLoc);
			if(biome == null){
				APIInfo.log.error("Error applying tweaks. Resource location "+rLoc+" does not correspond to a biome! Check the output files for the correct location!");
				continue;
			}
			biomes.add(biome);
		}
		return biomes.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> biomes = Lists.newArrayList();
		for(final ResourceLocation rLoc:this.rLocs){
			final Biome biome = Biome.REGISTRY.getObject(rLoc);
			if(biome == null){
				APIInfo.log.error("Error applying tweaks. Resource location "+rLoc+" does not correspond to a biome! Check the output files for the correct location!");
				continue;
			}
			biomes.add(Biome.getIdForBiome(biome));
		}
		return biomes;
	}

}
