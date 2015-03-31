package me.superckl.biometweaker.script.command;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.script.ParameterType;

import com.google.common.collect.Maps;

public class ScriptCommandListing {

	@Getter
	private final Map<List<ParameterType>, Constructor<? extends IScriptCommand>> constructors = Maps.newHashMap();

	public ScriptCommandListing() {}

	public ScriptCommandListing(final Entry<List<ParameterType>, Constructor<? extends IScriptCommand>> ... entries){
		this.addEntries(entries);
	}

	public void addEntries(final Entry<List<ParameterType>, Constructor<? extends IScriptCommand>> ... entries){
		for(final Entry<List<ParameterType>, Constructor<? extends IScriptCommand>> entry:entries)
			this.constructors.put(entry.getKey(), entry.getValue());
	}

	public void addEntry(final List<ParameterType> list, final Constructor<? extends IScriptCommand> construct){
		this.constructors.put(list, construct);
	}

}
