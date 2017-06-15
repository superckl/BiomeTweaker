package me.superckl.api.biometweaker.world.gen.feature;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class WorldGenTreesBuilder extends WorldGeneratorBuilder<WorldGenTreesWrapper>{

	private int minHeight = 4;
	private int leafHeight = 4;
	private int heightVariation = 3;
	private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();
	private IBlockState vineBlock = Blocks.VINE.getDefaultState();
	private boolean growVines = false;
	private boolean checkCanGrow = true;
	private final List<Predicate<IBlockState>> soilPredicates = Lists.newArrayList();

	public WorldGenTreesBuilder() {
		this.setMainBlock(Blocks.LOG.getDefaultState());
	}

	@Override
	public WorldGenTreesWrapper build() {
		final WorldGenGenericTree gen = new WorldGenGenericTree(false, this.minHeight, this.heightVariation, this.leafHeight, this.mainBlock, this.leafBlock, this.vineBlock, this.growVines, this.checkCanGrow, this.soilPredicates);
		return new WorldGenTreesWrapper(gen, this.count);
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

	public int getLeafHeight() {
		return this.leafHeight;
	}

	public void setLeafHeight(final int leafHeight) {
		this.leafHeight = leafHeight;
	}

	public int getHeightVariation() {
		return this.heightVariation;
	}

	public void setHeightVariation(final int heightVariation) {
		this.heightVariation = heightVariation;
	}

	public IBlockState getLeafBlock() {
		return this.leafBlock;
	}

	public void setLeafBlock(final IBlockState leafBlock) {
		this.leafBlock = leafBlock;
	}

	public IBlockState getVineBlock() {
		return this.vineBlock;
	}

	public void setVineBlock(final IBlockState vineBlock) {
		this.vineBlock = vineBlock;
	}

	public boolean isGrowVines() {
		return this.growVines;
	}

	public void setGrowVines(final boolean vines) {
		this.growVines = vines;
	}

	public boolean isCheckCanGrow() {
		return this.checkCanGrow;
	}

	public void setCheckCanGrow(final boolean checkCanGrow) {
		this.checkCanGrow = checkCanGrow;
	}

	public void addSoilPredicate(final Predicate<IBlockState> predicate){
		this.soilPredicates.add(predicate);
	}

}
