package me.superckl.biometweaker.common.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class BlankWorldGenTrees extends WorldGenTrees{

	public BlankWorldGenTrees(final boolean blockNotify) {
		super(blockNotify);
	}

	@Override
	public boolean generate(final World p_76484_1_, final Random p_76484_2_, final int p_76484_3_, final int p_76484_4_, final int p_76484_5_) {
		return true;
	}



}
