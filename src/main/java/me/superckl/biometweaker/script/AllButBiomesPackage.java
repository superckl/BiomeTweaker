package me.superckl.biometweaker.script;

import java.util.Iterator;
import java.util.List;

import me.superckl.biometweaker.util.CollectionHelper;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Lists;

public class AllButBiomesPackage implements IBiomePackage{

	private final int[] exclusions;

	public AllButBiomesPackage(final int ... exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if((gen != null) && (CollectionHelper.find(gen.biomeID, this.exclusions) == -1))
				list.add(gen);
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> list = Lists.newArrayList();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if((gen != null) && (CollectionHelper.find(gen.biomeID, this.exclusions) == -1))
				list.add(gen.biomeID);
		return list;
	}

}
