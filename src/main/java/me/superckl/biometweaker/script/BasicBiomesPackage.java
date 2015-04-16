package me.superckl.biometweaker.script;

import java.util.Iterator;
import java.util.List;

import me.superckl.biometweaker.util.CollectionHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.Lists;

public class BasicBiomesPackage implements IBiomePackage{

	private final int[] ids;

	public BasicBiomesPackage(final int ... ids) {
		this.ids = ids;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		for(final int i:this.ids){
			final BiomeGenBase gen = BiomeGenBase.getBiome(i);
			if(gen == null){
				LogHelper.info("Error applying tweaks. Biome ID "+i+" does not correspond to a biome! Check the output files for the correct ID!");
				continue;
			}
			list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> ids = Lists.newArrayList();
		CollectionHelper.addAll(ids, this.ids);
		return ids;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return true;
	}



}
