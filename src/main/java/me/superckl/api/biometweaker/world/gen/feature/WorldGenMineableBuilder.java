package me.superckl.api.biometweaker.world.gen.feature;

import com.google.common.base.Predicate;

import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenMineableBuilder extends WorldGeneratorBuilder<WorldGenMineableWrapper>{

	private int size = 9;
	private int maxHeight = 128;
	private int minHeight = 0;
	private Predicate<IBlockState> predicate = new BlockEquivalencePredicate(Blocks.STONE.getDefaultState());

	@Override
	public WorldGenMineableWrapper build() {
		final WorldGenMinable gen = new WorldGenMinable(this.mainBlock, this.size, this.predicate);
		if (this.maxHeight < this.minHeight){
			final int i = this.minHeight;
			this.minHeight = this.maxHeight;
			this.maxHeight = i;
		}else if (this.maxHeight == this.minHeight)
			if (this.minHeight < 255)
				++this.maxHeight;
			else
				--this.minHeight;
		return new WorldGenMineableWrapper(gen, this.count, this.maxHeight, this.minHeight);
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public Predicate<IBlockState> getPredicate() {
		return this.predicate;
	}

	public void setPredicate(final Predicate<IBlockState> predicate) {
		this.predicate = predicate;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	public void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

}
