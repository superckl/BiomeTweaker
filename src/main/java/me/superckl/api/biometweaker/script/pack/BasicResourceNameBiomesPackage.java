package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.superckl.api.biometweaker.APIInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class BasicResourceNameBiomesPackage extends BiomePackage{

	private final ResourceLocation[] rLocs;

	public BasicResourceNameBiomesPackage(final String ... names) {
		this.rLocs = new ResourceLocation[names.length];
		for(int i = 0; i < names.length; i++)
			this.rLocs[i] = new ResourceLocation(names[i]);
	}

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> biomes = new ArrayList<>();
		for(final ResourceLocation rLoc:this.rLocs){
			final Biome biome = ForgeRegistries.BIOMES.getValue(rLoc);
			if(biome == null){
				APIInfo.log.error("Error applying tweaks. Resource location "+rLoc+" does not correspond to a biome! Check the output files for the correct location!");
				continue;
			}
			biomes.add(biome);
		}
		return biomes.iterator();
	}

}
