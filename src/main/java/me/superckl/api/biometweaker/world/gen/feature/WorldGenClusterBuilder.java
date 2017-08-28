package me.superckl.api.biometweaker.world.gen.feature;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;

public class WorldGenClusterBuilder extends WorldGeneratorBuilder<WorldGeneratorWrapper<WorldGenCluster>>{

	private int radius = 8;
	private int height = 4;
	private final List<Predicate<IBlockState>> soilPredicates = Lists.newArrayList();

	@Override
	public WorldGeneratorWrapper<WorldGenCluster> build() {
		return new WorldGeneratorWrapper<>(new WorldGenCluster(false, this.mainBlock, this.radius, this.height, this.soilPredicates), this.count);
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
		this.soilPredicates.add(predicate);
	}

}
