package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import me.superckl.api.superscript.util.CollectionHelper;
import net.minecraft.world.level.biome.Biome;

public class IntersectBiomesPackage extends BiomePackage{

	List<BiomePackage> packs = new ArrayList<>();

	public IntersectBiomesPackage(final BiomePackage ... packs) {
		Collections.addAll(this.packs, packs);
	}

	@Override
	public Iterator<Biome> iterator() {
		if(this.packs.size() == 0)
			return Collections.emptyIterator();
		final List<List<Biome>> lists = new ArrayList<>();
		for(final BiomePackage pack:this.packs){
			final List<Biome> list = new ArrayList<>();
			Iterators.addAll(list, pack.iterator());
			lists.add(list);
		}
		final List<Biome> intersect = new ArrayList<>(lists.get(0));
		final Iterator<Biome> it = intersect.iterator();
		while(it.hasNext())
			if(!CollectionHelper.allContains(it.next(), lists))
				it.remove();
		return intersect.iterator();
	}

}
