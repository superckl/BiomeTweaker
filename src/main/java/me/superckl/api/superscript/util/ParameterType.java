package me.superckl.api.superscript.util;

import me.superckl.api.superscript.ScriptHandler;

public abstract class ParameterType {

	private final ParameterWrapper simpleWrapper;
	private final ParameterWrapper varArgsWrapper;
	private ParameterWrapper specialWrapper = null;

	public ParameterType() {
		this.simpleWrapper = ParameterWrapper.builder().type(this).minNum(1).maxNum(1).build();
		this.varArgsWrapper = ParameterWrapper.builder().type(this).varArgs(true).build();
	}

	public ParameterType(final ParameterWrapper wrapper){
		this();
		this.specialWrapper = wrapper;
	}

	/**
	 * Attempts to parse the given string argument into a type given by this ParameterType instance.
	 * This should not be used for biome packs. Use their special wrapper instead.
	 */
	public Object tryParse(final String parameter) throws Exception{
		return this.tryParse(parameter, null);
	}

	/**
	 * Attempts to parse the given string argument into a type given by this ParameterType instance.
	 * This should not be used for biome packs. Use their special wrapper instead.
	 */
	public abstract Object tryParse(final String parameter, final ScriptHandler handler) throws Exception;

	public boolean hasSpecialWrapper(){
		return this.specialWrapper != null;
	}

	public ParameterWrapper getSimpleWrapper() {
		return simpleWrapper;
	}

	public ParameterWrapper getVarArgsWrapper() {
		return varArgsWrapper;
	}

	public ParameterWrapper getSpecialWrapper() {
		return specialWrapper;
	}

}
