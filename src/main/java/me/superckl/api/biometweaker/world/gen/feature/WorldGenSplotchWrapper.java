package me.superckl.api.biometweaker.world.gen.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSplotchWrapper<K extends WorldGenerator> extends WorldGeneratorWrapper<K>{

	public WorldGenSplotchWrapper(final K generator, final int count) {
		super(generator, count);
	}

	@Override
	public void generate(final World world, final Random rand, final BlockPos chunkPos) {
		for (int i = 0; i < this.count; i++) {
			final int x = rand.nextInt(16) + 8;
			final int z = rand.nextInt(16) + 8;
			final BlockPos blockpos = world.getTopSolidOrLiquidBlock(chunkPos.add(x, 0, z));
			this.generator.generate(world, rand, blockpos);
		}
	}

}
