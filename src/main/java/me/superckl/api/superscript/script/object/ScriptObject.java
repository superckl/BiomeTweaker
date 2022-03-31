package me.superckl.api.superscript.script.object;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Getter;
import me.superckl.api.superscript.APIInfo;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.ScriptParser.ScriptContext;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.command.ScriptCommandListing;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.WarningHelper;

public abstract class ScriptObject {

	protected final Map<String, ScriptCommandListing> validCommands = new LinkedHashMap<>();

	@Getter
	private ScriptContext context;

	public ScriptObject() {
		Class<?> clazz = this.getClass();
		while(ScriptObject.class.isAssignableFrom(clazz)){
			this.validCommands.putAll(ScriptCommandRegistry.INSTANCE.getListings(WarningHelper.uncheckedCast(clazz)));
			clazz = clazz.getSuperclass();
		}
	}

	public void handleCall(final String call, final ScriptContext context, final ScriptHandler handler) throws Exception{
		String command;
		try {
			command = ScriptParser.getCommandCalled(call);
		} catch (final IllegalArgumentException e) {
			APIInfo.log.error("Failed to parse command call: "+call+" @ "+context+". Reason: "+e.getMessage());
			return;
		}
		if(!this.validCommands.containsKey(command)){
			APIInfo.log.error("Failed to find meaning in command "+call+" @ "+context+". Reason: No commands found with that name.");
			return;
		}
		final ScriptCommandListing listing = this.validCommands.get(command);
		String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
		args = this.modifyArguments(args, handler);
		Pair<Constructor<? extends ScriptCommand>, Object[]> pair = ScriptParser.findConstructor(listing, args, handler);
		if(pair != null){
			pair = this.modifyConstructorPair(pair, args, handler);
			final ScriptCommand sCommand = pair.getKey().newInstance(pair.getValue());
			sCommand.setContext(context);
			sCommand.setScriptHandler(handler);
			if(listing.isPerformInst())
				sCommand.perform();
			else
				this.addCommand(sCommand);
			return;
		}
		APIInfo.log.error("Failed to parse arguments for command "+call+" @ "+context+". Reason: No constructors found with matching arguments.");
	}

	/**
	 * You should implement this to read any arguments you expect to receive in scripts.
	 * @param args The args parsed from the script.
	 * @throws Exception An exception that arised from incorrect arguments or other parsing problems.
	 */
	public void readArgs(final Object ... args) throws Exception{

	}

	public abstract void addCommand(ScriptCommand command);

	public Map<String, ScriptCommandListing> getValidCommands() {
		return this.validCommands;
	}

	public Pair<Constructor<? extends ScriptCommand>, Object[]> modifyConstructorPair(final Pair<Constructor<? extends ScriptCommand>, Object[]> pair, final String[] args, final ScriptHandler handler){
		return pair;
	}

	public String[] modifyArguments(final String[] args, final ScriptHandler handler){
		return args;
	}

	public void setContext(final ScriptContext context) {
		if(this.context != null)
			throw new IllegalStateException("Context has already been set!");
		this.context = context;
	}

}
