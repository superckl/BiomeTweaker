package me.superckl.biometweaker.script.object.decoration;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorWrapper;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetDecorationBlock;
import me.superckl.biometweaker.util.LogHelper;

public abstract class DecorationScriptObject<T extends WorldGeneratorWrapper<?>, V extends WorldGeneratorBuilder<T>> extends ScriptObject{

	@Nonnull
	@Getter
	protected final V builder;

	public DecorationScriptObject(final V builder) {
		this.builder = builder;
	}

	@Override
	public void handleCall(final String call, final ScriptHandler handler) throws Exception{
		final String command = ScriptParser.getCommandCalled(call);
		if(!this.validCommands.containsKey(command)){
			LogHelper.error("Failed to find meaning in command "+call+". It will be ignored.");
			return;
		}
		final ScriptCommandListing listing = this.validCommands.get(command);
		outer:
			for(final Entry<List<ParameterWrapper>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
				String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
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
				//ParamterType list does not contain WorldGeneratorBuilders, so insert them.
				final Object[] args = new Object[objs.size()+1];
				System.arraycopy(objs.toArray(), 0, args, 1, objs.size());
				args[0] = this.builder;
				final IScriptCommand sCommand = entry.getValue().newInstance(args);
				if(listing.isPerformInst())
					sCommand.perform();
				else
					Config.INSTANCE.addCommand(sCommand);
				return;
			}
		LogHelper.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		Config.INSTANCE.addCommand(command);
	}

	public T createDecoration(){
		return this.builder.build();
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		final ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetDecorationBlock.class.getConstructor(WorldGeneratorBuilder.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()),
				ScriptCommandSetDecorationBlock.class.getConstructor(WorldGeneratorBuilder.class, String.class));
		validCommands.put("setBlock", listing);

		return validCommands;
	}
}
