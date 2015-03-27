package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.command.IScriptCommand;

public abstract class  ScriptObject {

	@Getter
	private final Map<String, List<Constructor<IScriptCommand>>> validCommands = new HashMap<String, List<Constructor<IScriptCommand>>>();
	
	public ScriptObject() {
		this.populateCommands();
	}
	
	public abstract void handleCall(final String call, final ScriptHandler handler);
	public abstract void populateCommands();
	
}
