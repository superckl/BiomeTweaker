package me.superckl.api.biometweaker.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

/**
 * Derived from {@link net.minecraft.world.gen.feature.WorldGenTrees}
 */
public class WorldGenGenericTree extends WorldGenAbstractTree{

	private final int minHeight;
	private final IBlockState trunkBlock;
	private final IBlockState leafBlock;
	private final IBlockState vineBlock;
	private final boolean growVines;
	private final boolean checkCanGrow;
	private final List<Predicate<IBlockState>> soilPredicates;

	public WorldGenGenericTree(final boolean notify, final int minHeight, final IBlockState trunkBlock, final IBlockState leafBlock,
			final IBlockState vineBlock, final boolean growVines, final boolean checkCanGrow, final List<Predicate<IBlockState>> soilPredicates) {
		super(notify);
		this.minHeight = minHeight;
		this.trunkBlock = trunkBlock;
		this.leafBlock = leafBlock;
		this.vineBlock = vineBlock;
		this.growVines = growVines;
		this.checkCanGrow = checkCanGrow;
		this.soilPredicates = soilPredicates;
	}

	@Override
	public boolean generate(final World world, final Random rand, final BlockPos position) {
		final int height = rand.nextInt(3) + this.minHeight;
		boolean shouldGrow = true;

		if (position.getY() >= 1 && position.getY() + height + 1 <= world.getHeight()){
			for (int y = position.getY(); y <= position.getY() + 1 + height; ++y){
				int radius = 1;

				if (y == position.getY())
					radius = 0;

				if (y >= position.getY() + 1 + height - 2)
					radius = 2;

				final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

				for (int x = position.getX() - radius; x <= position.getX() + radius && shouldGrow; ++x)
					for (int z = position.getZ() - radius; z <= position.getZ() + radius && shouldGrow; ++z)
						if (y >= 0 && y < world.getHeight()){
							if (!this.isReplaceable(world,mutableBlockPos.setPos(x, y, z)))
								shouldGrow = false;
						}else
							shouldGrow = false;
			}

			if (!shouldGrow)
				return false;
			else{
				IBlockState block = world.getBlockState(position.down());
				shouldGrow = false;
				if(this.soilPredicates.isEmpty())
					shouldGrow = this.checkCanGrow && block.getBlock().canSustainPlant(block, world, position.down(), EnumFacing.UP, (BlockSapling)Blocks.SAPLING);
				else{
					for(final Predicate<IBlockState> predicate:this.soilPredicates)
						if(predicate.apply(block)){
							shouldGrow = true;
							break;
						}
					if(shouldGrow == false)
						shouldGrow = this.checkCanGrow && block.getBlock().canSustainPlant(block, world, position.down(), EnumFacing.UP, (BlockSapling)Blocks.SAPLING);
				}
				if (shouldGrow && position.getY() < world.getHeight() - height - 1){
					if(this.checkCanGrow)
						block.getBlock().onPlantGrow(block, world, position.down(), position);

					for (int i3 = position.getY() - 3 + height; i3 <= position.getY() + height; ++i3){
						final int i4 = i3 - (position.getY() + height);
						final int j1 = 1 - i4 / 2;

						for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1){
							final int l1 = k1 - position.getX();

							for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2){
								final int j2 = i2 - position.getZ();

								if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0){
									final BlockPos blockpos = new BlockPos(k1, i3, i2);
									block = world.getBlockState(blockpos);

									if (block.getBlock().isAir(block, world, blockpos) || block.getBlock().isLeaves(block, world, blockpos) || block.getMaterial() == Material.VINE)
										this.setBlockAndNotifyAdequately(world, blockpos, this.leafBlock);
								}
							}
						}
					}

					for (int y = 0; y < height; ++y){
						final BlockPos up = position.up(y);
						block = world.getBlockState(up);

						if (block.getBlock().isAir(block, world, up) || block.getBlock().isLeaves(block, world, up) || block.getMaterial() == Material.VINE){
							this.setBlockAndNotifyAdequately(world, position.up(y), this.trunkBlock);

							if (this.growVines && y > 0){

								if (rand.nextInt(3) > 0 && world.isAirBlock(position.add(-1, y, 0)))
									this.addVine(world, position.add(-1, y, 0), BlockVine.EAST);

								if (rand.nextInt(3) > 0 && world.isAirBlock(position.add(1, y, 0)))
									this.addVine(world, position.add(1, y, 0), BlockVine.WEST);

								if (rand.nextInt(3) > 0 && world.isAirBlock(position.add(0, y, -1)))
									this.addVine(world, position.add(0, y, -1), BlockVine.SOUTH);

								if (rand.nextInt(3) > 0 && world.isAirBlock(position.add(0, y, 1)))
									this.addVine(world, position.add(0, y, 1), BlockVine.NORTH);
							}
						}
					}

					if (this.growVines)
						for (int k3 = position.getY() - 3 + height; k3 <= position.getY() + height; ++k3){
							final int j4 = k3 - (position.getY() + height);
							final int k4 = 2 - j4 / 2;
							final BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

							for (int l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4)
								for (int i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5){
									blockpos$mutableblockpos1.setPos(l4, k3, i5);

									block = world.getBlockState(blockpos$mutableblockpos1);
									if (block.getBlock().isLeaves(block, world, blockpos$mutableblockpos1)){
										final BlockPos blockpos2 = blockpos$mutableblockpos1.west();
										final BlockPos blockpos3 = blockpos$mutableblockpos1.east();
										final BlockPos blockpos4 = blockpos$mutableblockpos1.north();
										final BlockPos blockpos1 = blockpos$mutableblockpos1.south();

										if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos2))
											this.addHangingVine(world, blockpos2, BlockVine.EAST);

										if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos3))
											this.addHangingVine(world, blockpos3, BlockVine.WEST);

										if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos4))
											this.addHangingVine(world, blockpos4, BlockVine.SOUTH);

										if (rand.nextInt(4) == 0 && world.isAirBlock(blockpos1))
											this.addHangingVine(world, blockpos1, BlockVine.NORTH);
									}
								}
						}

					return true;
				}else
					return false;
			}
		}else
			return false;
	}

	private void addVine(final World worldIn, final BlockPos pos, final PropertyBool prop)
	{
		this.setBlockAndNotifyAdequately(worldIn, pos, this.vineBlock.withProperty(prop, Boolean.valueOf(true)));
	}

	private void addHangingVine(final World worldIn, BlockPos pos, final PropertyBool prop)
	{
		this.addVine(worldIn, pos, prop);
		int i = 4;

		for (pos = pos.down(); worldIn.isAirBlock(pos) && i > 0; --i)
		{
			this.addVine(worldIn, pos, prop);
			pos = pos.down();
		}
	}

}
