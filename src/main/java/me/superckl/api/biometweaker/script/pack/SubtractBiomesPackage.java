package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.Biome;

public class SubtractBiomesPackage extends BiomePackage{

	private final BiomePackage main;
	private final BiomePackage subtract;

	public SubtractBiomesPackage(final BiomePackage main, final BiomePackage subtract) {
		this.main = main;
		this.subtract = subtract;
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> list = Lists.newArrayList(this.main.getIterator());
		final Iterator<Biome> it = this.subtract.getIterator();
		while(it.hasNext())
			list.remove(it.next());
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> ints = Lists.newArrayList(this.main.getRawIds());
		ints.removeAll(this.subtract.getRawIds());
		return ints;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return this.main.supportsEarlyRawIds() && this.subtract.supportsEarlyRawIds();
	}

}
