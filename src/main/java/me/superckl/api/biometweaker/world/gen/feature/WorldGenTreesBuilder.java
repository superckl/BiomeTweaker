package me.superckl.api.biometweaker.world.gen.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenTrees;

public class WorldGenTreesBuilder extends WorldGeneratorBuilder<WorldGenTreesWrapper>{

	private int minHeight = 4;
	private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();
	private boolean growVines = false;

	public WorldGenTreesBuilder() {
		this.setMainBlock(Blocks.LOG.getDefaultState());
	}

	@Override
	public WorldGenTreesWrapper build() {
		final WorldGenTrees gen = new WorldGenTrees(false, this.minHeight, this.mainBlock, this.leafBlock, this.growVines);
		return new WorldGenTreesWrapper(gen, this.count);
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

	public IBlockState getLeafBlock() {
		return this.leafBlock;
	}

	public void setLeafBlock(final IBlockState leafBlock) {
		this.leafBlock = leafBlock;
	}

	public boolean isGrowVines() {
		return this.growVines;
	}

	public void setGrowVines(final boolean vines) {
		this.growVines = vines;
	}

}
