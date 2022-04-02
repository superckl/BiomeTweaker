package me.superckl.api.biometweaker.property;

public abstract class Property <K, V> {

	private final Class<K> typeClass;

	public Property(final Class<K> typeClass){
		this.typeClass = typeClass;
	}

	public abstract void set(V obj, K val);
	public abstract K get(V obj);
	public abstract boolean isReadable();
	public abstract boolean isSettable();
	public abstract Class<V> getTargetClass();

	public boolean isCopyable(){
		return this.isReadable() && this.isSettable();
	}

	public void copy(final V from, final V to){
		if(!this.isCopyable())
			throw new UnsupportedOperationException("Property cannot be copied!");
		this.set(to, this.get(from));
	}

	public Class<K> getTypeClass() {
		return this.typeClass;
	}

}
