package me.superckl.api.superscript.script;

public abstract class ParameterType<T> implements IStringParser<T>{

	private final Class<T> typeClass;

	private final ParameterWrapper<T> simpleWrapper;
	private final ParameterWrapper<T> varArgsWrapper;
	private ParameterWrapper<T> specialWrapper = null;

	public ParameterType(final Class<T> typeClass) {
		this.typeClass = typeClass;
		this.simpleWrapper = ParameterWrapper.<T>builder().type(this).minNum(1).maxNum(1).build();
		this.varArgsWrapper = ParameterWrapper.<T>builder().type(this).varArgs(true).build();
	}

	public ParameterType(final Class<T> typeClass, final ParameterWrapper<T> wrapper){
		this(typeClass);
		this.specialWrapper = wrapper;
	}

	public boolean hasSpecialWrapper(){
		return this.specialWrapper != null;
	}

	public ParameterWrapper<T> getSimpleWrapper() {
		return this.simpleWrapper;
	}

	public ParameterWrapper<T> getVarArgsWrapper() {
		return this.varArgsWrapper;
	}

	public ParameterWrapper<T> getSpecialWrapper() {
		return this.specialWrapper;
	}

	public Class<T> getTypeClass(){
		return this.typeClass;
	}

}
