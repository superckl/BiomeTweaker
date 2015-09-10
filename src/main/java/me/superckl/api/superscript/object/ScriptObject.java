package me.superckl.api.superscript.object;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.api.superscript.APIInfo;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.ParameterWrapper;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public abstract class ScriptObject {

	@Getter
	protected final Map<String, ScriptCommandListing> validCommands = new LinkedHashMap<String, ScriptCommandListing>();

	public ScriptObject() {
		this.validCommands.putAll(ScriptCommandRegistry.INSTANCE.getListings(this.getClass()));
	}

	public void handleCall(final String call, final ScriptHandler handler) throws Exception{
		final String command = ScriptParser.getCommandCalled(call);
		if(!this.validCommands.containsKey(command)){
			APIInfo.log.error("Failed to find meaning in command "+call+". It will be ignored.");
			return;
		}
		String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
		final ScriptCommandListing listing = this.validCommands.get(command);
		outer:
			for(final Entry<List<ParameterWrapper>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
				final List<Object> objs = Lists.newArrayList();
				final List<ParameterWrapper> params = Lists.newArrayList(entry.getKey());
				final Iterator<ParameterWrapper> it = params.iterator();
				while(it.hasNext()){
					final ParameterWrapper wrap = it.next();
					final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
					if((parsed.getKey().length == 0) && !wrap.canReturnNothing()){
						continue outer;
					}
					Collections.addAll(objs, parsed.getKey());
					arguments = parsed.getValue();
					it.remove();
				}
				if(!params.isEmpty() || (arguments.length != 0)){
					continue;
				}
				final IScriptCommand sCommand = entry.getValue().newInstance(objs.toArray());
				if(listing.isPerformInst())
					sCommand.perform();
				else
					this.addCommand(sCommand);
				return;
			}
		APIInfo.log.error("Failed to find meaning in command "+call+". It will be ignored.");
	}
	
	/**
	 * You should implement to read any arguments you expetced to receive in scripts.
	 * @param args The args parsed from the script.
	 * @throws Exception An exception that arised from incorrect arguments or other parsing problems.
	 */
	public void readArgs(Object ... args) throws Exception{
		
	}
	
	public abstract void addCommand(IScriptCommand command);

}
