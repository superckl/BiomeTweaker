package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.level.biome.Biome;

public class SubtractBiomesPackage extends BiomePackage{

	private final BiomePackage main;
	private final BiomePackage subtract;

	public SubtractBiomesPackage(final BiomePackage main, final BiomePackage subtract) {
		this.main = main;
		this.subtract = subtract;
	}

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> list = Lists.newArrayList(this.main.iterator());
		final Iterator<Biome> it = this.subtract.iterator();
		while(it.hasNext())
			list.remove(it.next());
		return list.iterator();
	}

}
