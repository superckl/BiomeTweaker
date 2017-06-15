package me.superckl.api.biometweaker.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import me.superckl.api.superscript.util.BlockEquivalencePredicate;
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
	private final int heightVariation;
	private final int leafHeight;
	private final IBlockState trunkBlock;
	private final IBlockState leafBlock;
	private final IBlockState vineBlock;
	private final boolean growVines;
	private final boolean checkCanGrow;
	private final List<Predicate<IBlockState>> soilPredicates;
	private final Predicate<IBlockState> leafPredicate;

	public WorldGenGenericTree(final boolean notify, final int minHeight, final int heightVariation, final int leafHeight, final IBlockState trunkBlock, final IBlockState leafBlock,
			final IBlockState vineBlock, final boolean growVines, final boolean checkCanGrow, final List<Predicate<IBlockState>> soilPredicates) {
		super(notify);
		this.minHeight = minHeight;
		this.heightVariation = heightVariation;
		this.leafHeight = leafHeight;
		this.trunkBlock = trunkBlock;
		this.leafBlock = leafBlock;
		this.vineBlock = vineBlock;
		this.growVines = growVines;
		this.checkCanGrow = checkCanGrow;
		this.soilPredicates = soilPredicates;
		this.leafPredicate = new BlockEquivalencePredicate(this.leafBlock);
	}

	@Override
	public boolean generate(final World world, final Random rand, final BlockPos position) {
		final int height = rand.nextInt(this.heightVariation) + this.minHeight;
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

					for (int y = position.getY() - (this.leafHeight-1) + height; y <= position.getY() + height; ++y){
						final int ly = y - (position.getY() + height);
						final int leafRadius = 1 - ly / 2;

						for (int x = position.getX() - leafRadius; x <= position.getX() + leafRadius; ++x){
							final int lx = x - position.getX();

							for (int z = position.getZ() - leafRadius; z <= position.getZ() + leafRadius; ++z){
								final int lz = z - position.getZ();

								if (Math.abs(lx) != leafRadius || Math.abs(lz) != leafRadius || rand.nextInt(2) != 0 && ly != 0){
									final BlockPos blockpos = new BlockPos(x, y, z);
									block = world.getBlockState(blockpos);

									if (block.getBlock().isAir(block, world, blockpos) || (this.leafBlock.getBlock().isLeaves(block, world, blockpos) &&
											block.getBlock().isLeaves(block, world, blockpos)) || this.leafPredicate.apply(block) || block.getMaterial() == Material.VINE)
										this.setBlockAndNotifyAdequately(world, blockpos, this.leafBlock);
								}
							}
						}
					}

					for (int y = 0; y < height; ++y){
						final BlockPos up = position.up(y);
						block = world.getBlockState(up);

						if (block.getBlock().isAir(block, world, up) || (this.leafBlock.getBlock().isLeaves(block, world, up) &&
								block.getBlock().isLeaves(block, world, up)) || this.leafPredicate.apply(block) || block.getMaterial() == Material.VINE){
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
						for (int y = position.getY() - 3 + height; y <= position.getY() + height; ++y){
							final int ly = y - (position.getY() + height);
							final int vineRadius = 2 - ly / 2;
							final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

							for (int x = position.getX() - vineRadius; x <= position.getX() + vineRadius; ++x)
								for (int z = position.getZ() - vineRadius; z <= position.getZ() + vineRadius; ++z){
									mutableBlockPos.setPos(x, y, z);

									block = world.getBlockState(mutableBlockPos);
									if (block.getBlock().isLeaves(block, world, mutableBlockPos)){
										final BlockPos west = mutableBlockPos.west();
										final BlockPos east = mutableBlockPos.east();
										final BlockPos north = mutableBlockPos.north();
										final BlockPos south = mutableBlockPos.south();

										if (rand.nextInt(4) == 0 && world.isAirBlock(west))
											this.addHangingVine(world, west, BlockVine.EAST);

										if (rand.nextInt(4) == 0 && world.isAirBlock(east))
											this.addHangingVine(world, east, BlockVine.WEST);

										if (rand.nextInt(4) == 0 && world.isAirBlock(north))
											this.addHangingVine(world, north, BlockVine.SOUTH);

										if (rand.nextInt(4) == 0 && world.isAirBlock(south))
											this.addHangingVine(world, south, BlockVine.NORTH);
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
		int vineLength = 4;

		for (pos = pos.down(); worldIn.isAirBlock(pos) && vineLength > 0; --vineLength)
		{
			this.addVine(worldIn, pos, prop);
			pos = pos.down();
		}
	}

}
