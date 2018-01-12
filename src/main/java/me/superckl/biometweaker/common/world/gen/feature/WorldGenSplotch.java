package me.superckl.biometweaker.common.world.gen.feature;

import java.util.Random;

import com.google.common.base.Predicate;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

@RequiredArgsConstructor
public class WorldGenSplotch extends WorldGenerator{

	private final IBlockState blockstate;
	private final int size;
	private final boolean requiresBase;
	private final Predicate<IBlockState> basePredicate;
	private final Predicate<IBlockState> replacementPredicate;

	@Override
	public boolean generate(final World world, final Random rand, BlockPos pos) {
		if(world.getBlockState(pos).getMaterial() == Material.AIR)
			pos = pos.down();
		if (this.requiresBase && !this.basePredicate.apply(world.getBlockState(pos)))
			return false;
		else {
			int i;
			switch(this.size) {
			case 0:
				 i = 0;
				 break;
			case 1:
				i = 1;
				break;
			case 2:
				i = rand.nextInt(1) + 1;
				break;
			default:
				i = rand.nextInt(this.size - 2) + 2;
			}
			for (int k = pos.getX() - i; k <= pos.getX() + i; ++k)
				for (int l = pos.getZ() - i; l <= pos.getZ() + i; ++l) {
					final int i1 = k - pos.getX();
					final int j1 = l - pos.getZ();

					if (i1 * i1 + j1 * j1 <= i * i)
						for (int k1 = pos.getY() - 1; k1 <= pos.getY() + 1; ++k1) {
							final BlockPos blockpos = new BlockPos(k, k1, l);
							final IBlockState blockstate = world.getBlockState(blockpos);

							if (this.replacementPredicate.apply(blockstate))
								world.setBlockState(blockpos, this.blockstate, 2);
						}
				}
			return true;
		}
	}

}
