package me.superckl.biometweaker.common.world.gen.feature;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import net.minecraft.block.state.IBlockState;

public class WorldGenSplotchBuilder extends WorldGeneratorBuilder<WorldGenSplotchWrapper<WorldGenSplotch>>{

	private int size = 4;
	private boolean requiresBase = true;
	private Predicate<IBlockState> basePredicate = Predicates.alwaysFalse();
	private Predicate<IBlockState> replacementPredicate = Predicates.alwaysFalse();

	@Override
	public WorldGenSplotchWrapper<WorldGenSplotch> build() {
		final WorldGenSplotch gen = new WorldGenSplotch(this.mainBlock, this.size, this.requiresBase, this.basePredicate, this.replacementPredicate);
		return new WorldGenSplotchWrapper<>(gen, this.count);
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public boolean requiresBase() {
		return this.requiresBase;
	}

	public void setRequiresBase(final boolean requiresBase) {
		this.requiresBase = requiresBase;
	}

	public Predicate<IBlockState> getBasePredicate() {
		return this.basePredicate;
	}

	public Predicate<IBlockState> getReplacementPredicate() {
		return this.replacementPredicate;
	}

	public void addBasePredicate(final Predicate<IBlockState> predicate){
		if(this.basePredicate == null)
			this.basePredicate = predicate;
		else
			this.basePredicate = Predicates.or(predicate, this.basePredicate);
	}

	public void addReplacementPredicate(final Predicate<IBlockState> predicate){
		if(this.replacementPredicate == null)
			this.replacementPredicate = predicate;
		else
			this.replacementPredicate = Predicates.or(predicate, this.replacementPredicate);
	}

}
