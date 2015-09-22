package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.biome.BiomeGenBase;

public class SubtractBiomesPackage implements IBiomePackage{

	private final IBiomePackage main;
	private final IBiomePackage subtract;

	public SubtractBiomesPackage(final IBiomePackage main, final IBiomePackage subtract) {
		this.main = main;
		this.subtract = subtract;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList(this.main.getIterator());
		final Iterator<BiomeGenBase> it = this.subtract.getIterator();
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
