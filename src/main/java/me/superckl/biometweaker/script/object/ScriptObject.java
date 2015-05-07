package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;
import me.superckl.biometweaker.script.util.ParameterWrapper;
import me.superckl.biometweaker.script.util.ScriptCommandListing;
import me.superckl.biometweaker.util.CollectionHelper;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.Pair;

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
		String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
		final ScriptCommandListing listing = this.validCommands.get(command);
		for(final Entry<List<ParameterWrapper>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
			final List<Object> objs = Lists.newArrayList();
			final List<ParameterWrapper> params = entry.getKey();
			final Iterator<ParameterWrapper> it = params.iterator();
			while(it.hasNext()){
				final ParameterWrapper wrap = it.next();
				final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
				Collections.addAll(objs, parsed.first());
				arguments = parsed.second();
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
		ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}
	public abstract void populateCommands() throws Exception;

}
