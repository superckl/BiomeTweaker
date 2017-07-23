package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;

public class AllButBiomesPackage extends BiomePackage{

	private final BiomePackage exclusions;

	public AllButBiomesPackage(final BiomePackage exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if((gen != null) && !ints.contains(Biome.getIdForBiome(gen)))
				list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		final List<Integer> ints = this.exclusions.getRawIds();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen == null)
				continue;
			final int id = Biome.getIdForBiome(gen);
			if(!ints.contains(id))
				list.add(id);
		}
		return list;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

}
