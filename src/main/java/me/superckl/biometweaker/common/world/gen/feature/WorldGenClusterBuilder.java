package me.superckl.biometweaker.common.world.gen.feature;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorWrapper;
import net.minecraft.block.state.IBlockState;

public class WorldGenClusterBuilder extends WorldGeneratorBuilder<WorldGeneratorWrapper<WorldGenCluster>>{

	private int radius = 8;
	private int height = 4;
	private Predicate<IBlockState> soilPredicate = null;

	@Override
	public WorldGeneratorWrapper<WorldGenCluster> build() {
		return new WorldGeneratorWrapper<>(new WorldGenCluster(false, this.mainBlock, this.radius, this.height, this.soilPredicate), this.count);
	}

	public int getRadius() {
		return this.radius;
	}

	public void setRadius(final int radius) {
		this.radius = radius;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public void addSoilPredicate(final Predicate<IBlockState> predicate){
		if(this.soilPredicate == null)
			this.soilPredicate = predicate;
		else
			this.soilPredicate = Predicates.or(predicate, this.soilPredicate);
	}

}
