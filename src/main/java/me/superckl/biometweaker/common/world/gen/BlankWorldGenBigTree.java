package me.superckl.biometweaker.common.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;

public class BlankWorldGenBigTree extends WorldGenBigTree{

	public BlankWorldGenBigTree(final boolean p_i2008_1_) {
		super(p_i2008_1_);
	}

	@Override
	public boolean generate(final World p_76484_1_, final Random p_76484_2_,
			final int p_76484_3_, final int p_76484_4_, final int p_76484_5_) {
		return true;
	}



}
