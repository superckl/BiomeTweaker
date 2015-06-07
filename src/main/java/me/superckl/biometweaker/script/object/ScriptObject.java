package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.ScriptCommandRegistry;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;
import me.superckl.biometweaker.script.util.ScriptCommandListing;
import me.superckl.biometweaker.script.util.wrapper.ParameterWrapper;
import me.superckl.biometweaker.util.CollectionHelper;
import me.superckl.biometweaker.util.LogHelper;

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
			LogHelper.error("Failed to find meaning in command "+call+". It will be ignored.");
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
					if((parsed.getKey().length == 0) && !wrap.canReturnNothing())
						continue outer;
					Collections.addAll(objs, parsed.getKey());
					arguments = parsed.getValue();
					it.remove();
				}
				if(!params.isEmpty() || (arguments.length != 0))
					continue;
				final IScriptCommand sCommand = entry.getValue().newInstance(objs.toArray());
				if(listing.isPerformInst())
					sCommand.perform();
				else
					Config.INSTANCE.addCommand(sCommand);
				return;
			}
		LogHelper.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

}
