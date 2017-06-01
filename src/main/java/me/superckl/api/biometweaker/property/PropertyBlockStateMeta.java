package me.superckl.api.biometweaker.property;

import net.minecraft.block.state.IBlockState;

public class PropertyBlockStateMeta extends Property<Integer>{

	private final PropertyField<IBlockState> wrappedProp;

	public PropertyBlockStateMeta(final Class<?> clazz, final String fieldName) {
		super(Integer.class);
		this.wrappedProp = new PropertyField<>(clazz, fieldName, IBlockState.class);
	}

	@Override
	public void set(final Object obj, final Integer val) throws IllegalStateException, IllegalArgumentException {
		final IBlockState blockState = this.wrappedProp.get(obj);
		if(blockState == null)
			throw new IllegalStateException("Field "+this.wrappedProp.getFieldName()+" in class "+this.wrappedProp.getClazz().getCanonicalName() + "is null!");
		this.wrappedProp.set(obj, blockState.getBlock().getStateFromMeta(val));
	}

	@Override
	public Integer get(final Object obj) throws IllegalStateException, IllegalArgumentException {
		final IBlockState blockState = this.wrappedProp.get(obj);
		if(blockState == null)
			return null;
		return blockState.getBlock().getMetaFromState(blockState);
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
