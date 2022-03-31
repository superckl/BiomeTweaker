package me.superckl.api.superscript.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.superckl.api.superscript.script.ParameterWrapper;

public class ConstructorListing<T> {

	protected final Map<List<ParameterWrapper<?>>, Constructor<? extends T>> constructors = new HashMap<>();

	public ConstructorListing() {}

	public ConstructorListing(final List<ParameterWrapper<?>> parameters, final Constructor<? extends T> constructor){
		this.addEntry(parameters, constructor);
	}

	public void addEntry(final List<ParameterWrapper<?>> list, final Constructor<? extends T> construct){
		this.constructors.put(list, construct);
	}

	public Map<List<ParameterWrapper<?>>, Constructor<? extends T>> getConstructors() {
		return this.constructors;
	}

}
