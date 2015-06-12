package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;
import me.superckl.biometweaker.script.command.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandAddToGeneration;
import me.superckl.biometweaker.script.command.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.script.util.ParameterType;
import me.superckl.biometweaker.script.util.ScriptCommandListing;
import me.superckl.biometweaker.script.util.wrapper.ParameterWrapper;
import me.superckl.biometweaker.util.CollectionHelper;
import me.superckl.biometweaker.util.LogHelper;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
				//ParamterType list does not contain BiomePackages, so insert them.
				final Object[] args = new Object[objs.size()+1];
				System.arraycopy(objs.toArray(), 0, args, 1, objs.size());
				args[0] = this.pack;
				Config.INSTANCE.addCommand(entry.getValue().newInstance(args));
				return;
			}
		LogHelper.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterWrapper>()
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper()
				, ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class));
		validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, Type.class));
		validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterWrapper>(), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDecoration", listing);
		
		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeFeature", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);
		
		return validCommands;
	}

}
