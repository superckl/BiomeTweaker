package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ParameterType;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;
import me.superckl.biometweaker.script.command.ScriptCommandListing;

public abstract class ScriptObject {

	@Getter
	protected final Map<String, ScriptCommandListing> validCommands = new HashMap<String, ScriptCommandListing>();

	public ScriptObject() {
		try {
			this.populateCommands();
		} catch (final Exception e) {
			ModBiomeTweakerCore.logger.error("Failed to populate command listings! Some tweaks may not be applied.");
			e.printStackTrace();
		}
	}

	public void handleCall(final String call, final ScriptHandler handler) throws Exception{
		final String command = ScriptParser.getCommandCalled(call);
		if(!this.validCommands.containsKey(command)){
			ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
			return;
		}
		final String[] arguments = ScriptParser.trimAll(ScriptParser.parseArguments(call));
		final ScriptCommandListing listing = this.validCommands.get(command);
		for(final Entry<List<ParameterType>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
			if(arguments.length != entry.getKey().size())
				continue;
			final Object[] objs = new Object[arguments.length];
			final List<ParameterType> list = entry.getKey();
			boolean shouldCont = false;
			for(int i = 0; i < arguments.length; i++){
				objs[i] = list.get(i).tryParse(arguments[i]);
				if(objs[i] == null){
					shouldCont = true;
					break;
				}
			}
			if(shouldCont)
				continue;
			Config.INSTANCE.addCommand(entry.getValue().newInstance(objs));
			return;
		}
		ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}
	public abstract void populateCommands() throws Exception;

}
