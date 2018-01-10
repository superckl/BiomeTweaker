package me.superckl.biometweaker.common.world.gen.feature;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class WorldGenTreesBuilder extends WorldGeneratorBuilder<WorldGenTreesWrapper<WorldGenGenericTree>>{

	private int minHeight = 4;
	private int leafHeight = 4;
	private int heightVariation = 3;
	private IBlockState leafBlock = Blocks.LEAVES.getDefaultState();
	private IBlockState vineBlock = Blocks.VINE.getDefaultState();
	private boolean growVines = false;
	private boolean checkCanGrow = true;
	private Predicate<IBlockState> soilPredicate;

	public WorldGenTreesBuilder() {
		this.setMainBlock(Blocks.LOG.getDefaultState());
	}

	@Override
	public WorldGenTreesWrapper<WorldGenGenericTree> build() {
		final WorldGenGenericTree gen = new WorldGenGenericTree(false, this.minHeight, this.heightVariation, this.leafHeight, this.mainBlock, this.leafBlock, this.vineBlock, this.growVines, this.checkCanGrow, this.soilPredicate);
		return new WorldGenTreesWrapper<>(gen, this.count);
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
		if(this.soilPredicate == null)
			this.soilPredicate = predicate;
		else
			this.soilPredicate = Predicates.or(predicate, this.soilPredicate);
	}

}
