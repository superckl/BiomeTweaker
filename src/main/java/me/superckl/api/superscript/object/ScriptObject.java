package me.superckl.api.superscript.object;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.superscript.APIInfo;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.WarningHelper;

public abstract class ScriptObject {

	protected final Map<String, ScriptCommandListing> validCommands = new LinkedHashMap<String, ScriptCommandListing>();

	public ScriptObject() {
		Class<?> clazz = this.getClass();
		while(ScriptObject.class.isAssignableFrom(clazz)){
			this.validCommands.putAll(ScriptCommandRegistry.INSTANCE.getListings(WarningHelper.uncheckedCast(clazz)));
			clazz = clazz.getSuperclass();
		}
	}

	public void handleCall(final String call, final ScriptHandler handler) throws Exception{
		final String command = ScriptParser.getCommandCalled(call);
		if(!this.validCommands.containsKey(command)){
			APIInfo.log.error("Failed to find meaning in command "+call+". It will be ignored.");
			return;
		}
		final ScriptCommandListing listing = this.validCommands.get(command);
		String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
		args = this.modifyArguments(args, handler);
		Pair<Constructor<? extends IScriptCommand>, Object[]> pair = ScriptParser.findConstructor(listing, args, handler);
		if(pair != null){
			pair = this.modifyConstructorPair(pair, args, handler);
			final IScriptCommand sCommand = pair.getKey().newInstance(pair.getValue());
			sCommand.setScriptHandler(handler);
			if(listing.isPerformInst())
				sCommand.perform();
			else
				this.addCommand(sCommand);
			return;
		}
		APIInfo.log.error("Failed to parse arguments for command "+call+". It will be ignored.");
	}

	/**
	 * You should implement to read any arguments you expetced to receive in scripts.
	 * @param args The args parsed from the script.
	 * @throws Exception An exception that arised from incorrect arguments or other parsing problems.
	 */
	public void readArgs(final Object ... args) throws Exception{

	}

	public abstract void addCommand(IScriptCommand command);

	public Map<String, ScriptCommandListing> getValidCommands() {
		return this.validCommands;
	}

	public Pair<Constructor<? extends IScriptCommand>, Object[]> modifyConstructorPair(final Pair<Constructor<? extends IScriptCommand>, Object[]> pair, final String[] args, final ScriptHandler handler){
		return pair;
	}

	public String[] modifyArguments(final String[] args, final ScriptHandler handler){
		return args;
	}

}
