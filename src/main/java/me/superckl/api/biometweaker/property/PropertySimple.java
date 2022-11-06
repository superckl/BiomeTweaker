package me.superckl.api.biometweaker.property;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PropertySimple<K, V> extends Property<K, V, Void> {

	private final Class<V> target;
	private final Function<? super V, ? extends K> getter;
	private final BiConsumer<? super V, ? super K> setter;

	public PropertySimple(final Class<K> typeClass, final Class<V> target, final Function<? super V, ? extends K> getter, final BiConsumer<? super V, ? super K> setter) {
		super(typeClass);
		this.target = target;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void set(final V obj, final K val) {
		this.setter.accept(obj, val);
	}

	@Override
	public K get(final V obj, final Optional<Void> c) {
		return this.getter.apply(obj);
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

	@Override
	public Class<V> getTargetClass() {
		return this.target;
	}

}
