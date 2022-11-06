package me.superckl.api.biometweaker.property;

import java.util.Optional;

public abstract class Property <K, V, C> {

	private final Class<K> typeClass;

	public Property(final Class<K> typeClass){
		this.typeClass = typeClass;
	}

	public abstract void set(V obj, K val);
	public abstract K get(V obj, Optional<C> context);
	public abstract boolean isReadable();
	public abstract boolean isSettable();
	public abstract Class<V> getTargetClass();

	public boolean isCopyable(){
		return this.isReadable() && this.isSettable();
	}

	public void copy(final V from, final V to, final Optional<C> context){
		if(!this.isCopyable())
			throw new UnsupportedOperationException("Property cannot be copied!");
		this.set(to, this.get(from, context));
	}

	public Class<K> getTypeClass() {
		return this.typeClass;
	}

}
