package me.superckl.api.biometweaker.property;

import java.lang.reflect.Field;
import java.util.Optional;

import com.google.common.primitives.Primitives;

import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.util.ObfNameHelper;

public final class PropertyField<K, V> extends Property<K, V, Void>{

	private final ObfNameHelper.Fields info;
	private final Class<V> targetClass;
	private final boolean isOptional;

	private Field field;

	public PropertyField(final ObfNameHelper.Fields info, final Class<K> typeClass) throws ClassNotFoundException{
		this(info, typeClass, false);
	}

	public PropertyField(final ObfNameHelper.Fields info, final Class<K> typeClass, final boolean isOptional) throws ClassNotFoundException{
		super(typeClass);
		this.info = info;
		this.isOptional = isOptional;
		this.targetClass = WarningHelper.uncheckedCast(this.info.getClazz().get());
	}

	@Override
	public void set(final V obj, final K val){
		this.verifyField();
		try {
			this.field.set(obj, this.isOptional ? Optional.of(val) : val);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException("Unable to set field "+this.info.getName()+" in class "+this.targetClass.getCanonicalName() +" to value "+val.toString(), e);
		}
	}

	@Override
	public K get(final V obj, final Optional<Void> c) throws IllegalStateException, IllegalArgumentException{
		this.verifyField();
		try {
			Object val = this.field.get(obj);
			if(this.isOptional)
				val = ((Optional<?>) val).orElse(null);
			return WarningHelper.uncheckedCast(val);
		} catch (final IllegalArgumentException e1){
			throw new IllegalArgumentException("Unable to get field "+this.info.getName()+" in class "+this.targetClass.getCanonicalName(), e1);
		} catch (final Exception e2) {
			throw new IllegalStateException("Unable to get field "+this.info.getName()+" in class "+this.targetClass.getCanonicalName(), e2);
		}
	}

	public void verifyField() throws IllegalStateException, IllegalArgumentException{
		if(this.field == null && this.targetClass != null && this.info.getName() != null && !this.info.getName().isEmpty())
			try {
				this.field = this.info.get();
				if(this.isOptional) {
					if(!this.field.getType().equals(Optional.class))
						throw new IllegalStateException("Property is marked as optional but field type is not Optional!");
				}else {
					if(!this.field.getType().isAssignableFrom(this.field.getType().isPrimitive() ? Primitives.unwrap(this.getTypeClass()):this.getTypeClass()))
						throw new IllegalStateException("Generic Type of Property is not assignable to Field Type.");
					if(!this.getTypeClass().isAssignableFrom(this.field.getType().isPrimitive() ? Primitives.wrap(this.field.getType()):this.field.getType()))
						throw new IllegalStateException("Field Type is not assignable to Generic Type of Property.");
				}
				this.field.setAccessible(true);
			} catch (final IllegalArgumentException e1) {
				throw new IllegalArgumentException("Unable to find field "+this.info.getName()+" in class "+this.targetClass.getCanonicalName(), e1);
			} catch (final Exception e2) {
				throw new IllegalStateException("Unable to find field "+this.info.getName()+" in class "+this.targetClass.getCanonicalName(), e2);
			}
	}

	@Override
	public Class<V> getTargetClass(){
		return this.targetClass;
	}

	public String getFieldName() {
		return this.info.getName();
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
