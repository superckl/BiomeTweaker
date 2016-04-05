package me.superckl.biometweaker;

import net.minecraft.block.state.IBlockState;

public class BiomeHooks {

	public static boolean contains(final IBlockState[] blocks, final IBlockState block){
		//LogHelper.info("Called for "+block+"in "+Arrays.toString(blocks));
		for(final IBlockState search:blocks)
			if(search.getBlock() == block.getBlock() && search.getBlock().getMetaFromState(search) == block.getBlock().getMetaFromState(block))
				return true;
		return false;
	}

}
