package me.superckl.api.biometweaker.property;

public abstract class Property <K> {

	private final Class<K> typeClass;

	public Property(final Class<K> typeClass){
		this.typeClass = typeClass;
	}

	public abstract void set(Object obj, K val);
	public abstract K get(Object obj);

	public void copy(final Object from, final Object to){
		this.set(to, this.get(from));
	}

	public Class<K> getTypeClass() {
		return this.typeClass;
	}

}
