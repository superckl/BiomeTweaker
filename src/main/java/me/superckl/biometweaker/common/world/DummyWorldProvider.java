package me.superckl.biometweaker.common.world;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class DummyWorldProvider extends WorldProvider{

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.OVERWORLD;
	}

}
