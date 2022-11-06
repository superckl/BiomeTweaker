package me.superckl.api.biometweaker.property;

import java.util.Optional;
import java.util.function.Function;

public class PropertyWrapper<K, V, C, T> extends Property<K, V, C>{

	private final Class<V> targetClass;
	private final Function<V, T> mapper;
	private final Property<K, T, C> wrapped;

	public PropertyWrapper(final Class<V> target, final Function<V, T> targetMapper, final Property<K, T, C> wrapped) {
		super(wrapped.getTypeClass());
		this.targetClass = target;
		this.mapper = targetMapper;
		this.wrapped = wrapped;
	}

	@Override
	public void set(final V obj, final K val) {
		this.wrapped.set(this.mapper.apply(obj), val);
	}

	@Override
	public K get(final V obj, final Optional<C> context) {
		return this.wrapped.get(this.mapper.apply(obj), context);
	}

	@Override
	public boolean isReadable() {
		return this.wrapped.isReadable();
	}

	@Override
	public boolean isSettable() {
		return this.wrapped.isSettable();
	}

	@Override
	public Class<V> getTargetClass() {
		return this.targetClass;
	}

}
