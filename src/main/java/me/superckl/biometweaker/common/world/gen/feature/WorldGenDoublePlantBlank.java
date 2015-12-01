package me.superckl.biometweaker.common.world.gen.feature;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;

public class WorldGenDoublePlantBlank extends WorldGenDoublePlant{

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        return true;
    }
	
}
