package me.superckl.biometweaker;

import net.minecraft.block.Block;

public class BiomeHooks {

	public static boolean contains(final Block[] blocks, final Block block){
		//LogHelper.info("Called for "+block+"in "+Arrays.toString(blocks));
		for(final Block search:blocks)
			if(search == block)
				return true;
		return false;
	}

}
