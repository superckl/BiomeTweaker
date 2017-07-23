package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.APIInfo;
import me.superckl.api.superscript.util.CollectionHelper;
import net.minecraft.world.biome.Biome;

public class BasicBiomesPackage extends BiomePackage{

	private final int[] ids;

	public BasicBiomesPackage(final int ... ids) {
		this.ids = ids;
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> list = Lists.newArrayList();
		for(final int i:this.ids){
			final Biome gen = Biome.getBiome(i);
			if(gen == null){
				APIInfo.log.error("Error applying tweaks. Biome ID "+i+" does not correspond to a biome! Check the output files for the correct ID!");
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
