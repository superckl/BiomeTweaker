package me.superckl.api.biometweaker.script.pack;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import me.superckl.api.superscript.util.CollectionHelper;
import net.minecraft.world.biome.Biome;

public class IntersectBiomesPackage implements IBiomePackage{

	List<IBiomePackage> packs = Lists.newArrayList();

	public IntersectBiomesPackage(final IBiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<Biome> getIterator() {
		if(this.packs.size() == 0)
			return Collections.emptyIterator();
		final List<List<Biome>> lists = Lists.newArrayList();
		for(final IBiomePackage pack:this.packs){
			final List<Biome> list = Lists.newArrayList();
			Iterators.addAll(list, pack.getIterator());
			lists.add(list);
		}
		final List<Biome> intersect = Lists.newArrayList(lists.get(0));
		final Iterator<Biome> it = intersect.iterator();
		while(it.hasNext())
			if(!CollectionHelper.allContains(it.next(), lists))
				it.remove();
		return intersect.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		for(final IBiomePackage pack:this.packs)
			if(!pack.supportsEarlyRawIds())
				return false;
		return true;
	}

	@Override
	public List<Integer> getRawIds() {
		if(this.packs.size() == 0)
			return Collections.emptyList();
		final List<List<Integer>> ints = Lists.newArrayList();
		for(final IBiomePackage pack:this.packs)
			ints.add(pack.getRawIds());
		final List<Integer> intersect = Lists.newArrayList(ints.get(0));
		final Iterator<Integer> it = intersect.iterator();
		while(it.hasNext())
			if(!CollectionHelper.allContains(it.next(), ints))
				it.remove();
		return intersect;
	}

}
