package me.superckl.api.superscript.util;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class ConstructorListing<T> {

	protected final Map<List<ParameterWrapper>, Constructor<? extends T>> constructors = Maps.newHashMap();

	public ConstructorListing() {}

	public ConstructorListing(final Entry<List<ParameterWrapper>, Constructor<? extends T>> ... entries){
		this.addEntries(entries);
	}

	public void addEntries(final Entry<List<ParameterWrapper>, Constructor<? extends T>> ... entries){
		for(final Entry<List<ParameterWrapper>, Constructor<? extends T>> entry:entries)
			this.constructors.put(entry.getKey(), entry.getValue());
	}

	public void addEntry(final List<ParameterWrapper> list, final Constructor<? extends T> construct){
		this.constructors.put(list, construct);
	}

	public Map<List<ParameterWrapper>, Constructor<? extends T>> getConstructors() {
		return this.constructors;
	}

}
