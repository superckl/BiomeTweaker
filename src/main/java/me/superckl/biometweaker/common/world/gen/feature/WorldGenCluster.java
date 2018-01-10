package me.superckl.biometweaker.common.world.gen.feature;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCluster extends WorldGenerator{

	private final IBlockState block;
	private final int radius;
	private final int height;
	private final Predicate<IBlockState> soilPredicate;

	public WorldGenCluster(final boolean notify, final IBlockState block, final int radius, final int height, final Predicate<IBlockState> soilPredicate) {
		super(notify);
		this.block = block;
		this.radius = radius;
		this.height = height;
		this.soilPredicate = soilPredicate;
	}

	@Override
	public boolean generate(final World world, final Random rand, final BlockPos position){
		for (int i = 0; i < 64; ++i){
			final BlockPos blockpos = position.add(rand.nextInt(this.radius) - rand.nextInt(this.radius),
					rand.nextInt(Math.round(this.height/2F)) - rand.nextInt(Math.round(this.height/2F)), rand.nextInt(this.radius) - rand.nextInt(this.radius));
			boolean validSoil = false;
			if(this.soilPredicate == null && !world.isAirBlock(blockpos.down()))
				validSoil = true;
			else if(this.soilPredicate != null){
				final IBlockState down = world.getBlockState(blockpos.down());
				validSoil = this.soilPredicate.apply(down);
			}
			if (validSoil && world.isAirBlock(blockpos) && this.block.getBlock().canPlaceBlockAt(world, blockpos))
				world.setBlockState(blockpos, this.block, 2);
		}

		return true;
	}

}
