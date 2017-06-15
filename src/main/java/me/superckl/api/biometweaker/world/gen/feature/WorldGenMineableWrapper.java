package me.superckl.api.biometweaker.world.gen.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenMineableWrapper extends WorldGeneratorWrapper<WorldGenMinable>{

	private final int maxHeight;
	private final int minHeight;

	public WorldGenMineableWrapper(final WorldGenMinable generator, final int count, final int maxHeight, final int minHeight) {
		super(generator, count);
		this.maxHeight = maxHeight;
		this.minHeight = minHeight;
	}

	@Override
	public void generate(final World world, final Random rand, final BlockPos chunkPos) {
		for (int j = 0; j < this.count; ++j){
			final BlockPos blockpos = chunkPos.add(rand.nextInt(16), rand.nextInt(this.maxHeight - this.minHeight) + this.minHeight, rand.nextInt(16));
			this.generator.generate(world, rand, blockpos);
		}
	}

}
