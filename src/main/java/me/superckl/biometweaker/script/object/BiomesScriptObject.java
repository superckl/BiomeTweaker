package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;
import me.superckl.biometweaker.script.command.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.script.util.ParameterType;
import me.superckl.biometweaker.script.util.ParameterWrapper;
import me.superckl.biometweaker.script.util.ScriptCommandListing;
import me.superckl.biometweaker.util.CollectionHelper;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

public class BiomesScriptObject extends ScriptObject{

	@Getter
	private final IBiomePackage pack;

	public BiomesScriptObject(final IBiomePackage pack) {
		super();
		this.pack = pack;
	}

	@Override
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
			final List<ParameterWrapper> params = Lists.newArrayList(entry.getKey());
			final Iterator<ParameterWrapper> it = params.iterator();
			while(it.hasNext()){
				final ParameterWrapper wrap = it.next();
				final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
				Collections.addAll(objs, parsed.getKey());
				arguments = parsed.getValue();
				it.remove();
			}
			if(!params.isEmpty() || (arguments.length != 0))
				continue;
			//ParamterType list does not contain BiomePackages, so insert them.
			final Object[] args = new Object[objs.size()+1];
			System.arraycopy(objs.toArray(), 0, args, 1, objs.size());
			args[0] = this.pack;
			Config.INSTANCE.addCommand(entry.getValue().newInstance(args));
			return;
		}
		ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	@Override
	public void populateCommands() throws Exception {
		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterWrapper>()
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		this.validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper()
				, ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class));
		this.validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, Type.class));
		this.validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		this.validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		this.validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterWrapper>(), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		this.validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("removeDecoration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		this.validCommands.put("create", listing);
	}

}
