package me.superckl.api.superscript.util;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;

public class BlockEquivalencePredicate implements Predicate<IBlockState>{

	private final IBlockState state;

	public BlockEquivalencePredicate(final IBlockState state) {
		this.state = state;
	}

	@Override
	public boolean apply(final IBlockState input) {
		return (input.getBlock() == this.state.getBlock()) && input.getBlock().getMetaFromState(input) == (this.state.getBlock().getMetaFromState(this.state));
	}

}
