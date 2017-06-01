package me.superckl.api.biometweaker.property;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class PropertyBlockState extends Property<String>{

	private final PropertyField<IBlockState> wrappedProp;

	public PropertyBlockState(final Class<?> clazz, final String fieldName) {
		super(String.class);
		this.wrappedProp = new PropertyField<>(clazz, fieldName, IBlockState.class);
	}

	@Override
	public void set(final Object obj, final String val) throws IllegalStateException, IllegalArgumentException {
		final Block blockState = Block.getBlockFromName(val);
		if(blockState == null)
			throw new IllegalArgumentException("Failed to find block "+val);
		this.wrappedProp.set(obj, blockState.getDefaultState());
	}

	@Override
	public String get(final Object obj) throws IllegalStateException, IllegalArgumentException {
		final IBlockState blockState = this.wrappedProp.get(obj);
		if(blockState == null)
			return null;
		return Block.REGISTRY.getNameForObject(blockState.getBlock()).toString();
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

}
