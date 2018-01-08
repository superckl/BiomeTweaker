package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import me.superckl.api.superscript.util.CollectionHelper;
import net.minecraft.world.biome.Biome;

public class IntersectBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public IntersectBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<Biome> getIterator() {
		if(this.packs.size() == 0)
			return Collections.emptyIterator();
		final List<List<Biome>> lists = new ArrayList<>();
		for(final BiomePackage pack:this.packs){
			final List<Biome> list = new ArrayList<>();
			Iterators.addAll(list, pack.getIterator());
			lists.add(list);
		}
		final List<Biome> intersect = new ArrayList<>(lists.get(0));
		final Iterator<Biome> it = intersect.iterator();
		while(it.hasNext())
			if(!CollectionHelper.allContains(it.next(), lists))
				it.remove();
		return intersect.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		for(final BiomePackage pack:this.packs)
			if(!pack.supportsEarlyRawIds())
				return false;
		return true;
	}

	@Override
	public List<Integer> getRawIds() {
		if(this.packs.size() == 0)
			return Collections.emptyList();
		final List<List<Integer>> ints = new ArrayList<>();
		for(final BiomePackage pack:this.packs)
			ints.add(pack.getRawIds());
		final List<Integer> intersect = new ArrayList<>(ints.get(0));
		final Iterator<Integer> it = intersect.iterator();
		while(it.hasNext())
			if(!CollectionHelper.allContains(it.next(), ints))
				it.remove();
		return intersect;
	}

}
