package me.superckl.api.biometweaker.property;

import java.lang.reflect.Field;

import com.google.common.primitives.Primitives;

public final class PropertyField<K> extends Property<K>{

	private final Class<?> clazz;
	private final String fieldName;

	private Field field;

	public PropertyField(final Class<?> clazz, final String fieldName, final Class<K> typeClass){
		super(typeClass);
		this.clazz = clazz;
		this.fieldName = fieldName;
	}

	@Override
	public void set(final Object obj, final K val) throws IllegalStateException, IllegalArgumentException{
		this.verifyField();
		try {
			this.field.set(obj, val);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException("Unable to set field "+this.fieldName+" in class "+this.clazz.getCanonicalName() +" to value "+val.toString(), e);
		}
	}

	@Override
	public K get(final Object obj) throws IllegalStateException, IllegalArgumentException{
		this.verifyField();
		try {
			return (K) this.field.get(obj);
		} catch (final IllegalArgumentException e1){
			throw new IllegalArgumentException("Unable to get field "+this.fieldName+" in class "+this.clazz.getCanonicalName(), e1);
		} catch (final Exception e2) {
			throw new IllegalStateException("Unable to get field "+this.fieldName+" in class "+this.clazz.getCanonicalName(), e2);
		}
	}

	public void verifyField() throws IllegalStateException, IllegalArgumentException{
		if(this.field == null && this.clazz != null && this.fieldName != null && !this.fieldName.isEmpty())
			try {
				this.field = this.clazz.getDeclaredField(this.fieldName);
				if(!this.field.getType().isAssignableFrom(this.field.getType().isPrimitive() ? Primitives.unwrap(this.getTypeClass()):this.getTypeClass()))
					throw new IllegalStateException("Generic Type of Property is not assignable to Field Type.");
				if(!this.getTypeClass().isAssignableFrom(this.field.getType().isPrimitive() ? Primitives.wrap(this.field.getType()):this.field.getType()))
					throw new IllegalStateException("Field Type is not assignable to Generic Type of Property.");
				this.field.setAccessible(true);
			} catch (final IllegalArgumentException e1) {
				throw new IllegalArgumentException("Unable to find field "+this.fieldName+" in class "+this.clazz.getCanonicalName(), e1);
			} catch (final Exception e2) {
				throw new IllegalStateException("Unable to find field "+this.fieldName+" in class "+this.clazz.getCanonicalName(), e2);
			}
	}

	public Class<?> getClazz() {
		return this.clazz;
	}

	public String getFieldName() {
		return this.fieldName;
	}

}
