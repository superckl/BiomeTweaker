package me.superckl.biometweaker.common.world.gen.layer;

import java.util.Arrays;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import lombok.Setter;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerReplacement extends GenLayer{

	private static final TIntIntMap biomeReplacements = new TIntIntHashMap();
	@Setter
	private static boolean mapChanged;

	private final GenLayer parent;
	private static int[] bakedMap = null;

	public GenLayerReplacement(final GenLayer parent) {
		super(1L);
		this.parent = parent;
	}

	@Override
	public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
		if(GenLayerReplacement.bakedMap == null || GenLayerReplacement.mapChanged)
			GenLayerReplacement.bakeMap();
		final int[] ints = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
		if(GenLayerReplacement.bakedMap.length == 0)
			return ints;
		for(int i = 0; i < ints.length; i++)
			if(ints[i] >= 0 && ints[i] < GenLayerReplacement.bakedMap.length && GenLayerReplacement.bakedMap[ints[i]] != -1)
				ints[i] = GenLayerReplacement.bakedMap[ints[i]];
		return ints;
	}

	private static void bakeMap(){
		final int[] keys = GenLayerReplacement.biomeReplacements.keys();
		if(keys.length == 0)
			GenLayerReplacement.bakedMap = new int[0];
		else{
			Arrays.sort(keys);
			GenLayerReplacement.bakedMap = new int[keys[keys.length-1] + 1];
			Arrays.fill(GenLayerReplacement.bakedMap, -1);
			for(final int key:keys)
				GenLayerReplacement.bakedMap[key] = GenLayerReplacement.biomeReplacements.get(key);
		}
		GenLayerReplacement.mapChanged = false;
	}

	public static void registerReplacement(final int toReplace, final int replacement){
		GenLayerReplacement.biomeReplacements.put(toReplace, replacement);
		GenLayerReplacement.mapChanged = true;
	}

}
