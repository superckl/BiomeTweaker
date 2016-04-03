package me.superckl.biometweaker.common.world.gen.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;

public class WorldGenDoublePlantBlank extends WorldGenDoublePlant{

	@Override
	public boolean generate(final World worldIn, final Random rand, final BlockPos position)
	{
		return true;
	}

}
