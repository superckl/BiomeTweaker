package me.superckl.biometweaker.common.world.gen.layer;

import gnu.trove.map.hash.TIntIntHashMap;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerReplacement extends GenLayer{

	private final GenLayer parent;

	public GenLayerReplacement(final GenLayer parent) {
		super(1L);
		this.parent = parent;
	}

	@Override
	public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
		final int[] ints = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
		final TIntIntHashMap replacements = BiomeEventHandler.getBiomeReplacements();
		for(int i = 0; i < ints.length; i++)
			if(replacements.containsKey(ints[i]))
				ints[i] = replacements.get(ints[i]);
		return ints;
	}

}
