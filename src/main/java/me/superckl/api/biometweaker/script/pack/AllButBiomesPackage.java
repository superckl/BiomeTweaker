package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class AllButBiomesPackage extends BiomePackage{

	private final BiomePackage exclusions;

	public AllButBiomesPackage(final BiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> list = new ArrayList<>();
		final Set<ResourceLocation> excludes = Streams.stream(this.exclusions).map(Biome::getRegistryName).collect(Collectors.toCollection(HashSet::new));
		final Iterator<Biome> it = ForgeRegistries.BIOMES.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen != null && !excludes.contains(gen.getRegistryName()))
				list.add(gen);
		}
		return list.iterator();
	}

}
