package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.command.entity.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.entity.ScriptCommandMaxSpawnPackSize;
import me.superckl.biometweaker.script.command.entity.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddActualFillerBlock;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddToGeneration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBiomeReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterVillageBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.misc.ScriptCommandDisableBonemealUse;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.util.LogHelper;

public class BiomesScriptObject extends BiomePackScriptObject{

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
				final IScriptCommand sCommand = entry.getValue().newInstance(args);
				if(listing.isPerformInst())
					sCommand.perform();
				else
					Config.INSTANCE.addCommand(sCommand);
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
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()
				, ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class));
		validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, SpawnListType.class));
		validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addActualFillerBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterWrapper>(), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDecoration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeFeature", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.TYPE, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, String.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.TYPE, String.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		validCommands.put("registerGenVillageBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("setMaxSpawnPackSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		listing.addEntry(Lists.<ParameterWrapper>newArrayList()
				, ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("disableBonemealUse", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBiomeReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("registerGenBiomeRep", listing);

		return validCommands;
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		Config.INSTANCE.addCommand(command);
	}

	@Override
	public void readArgs(final Object... packs) throws Exception {
		final IBiomePackage[] bPacks = new IBiomePackage[packs.length];
		System.arraycopy(packs, 0, bPacks, 0, packs.length);
		if(bPacks.length == 1)
			this.pack = bPacks[0];
		else
			this.pack = new MergedBiomesPackage(bPacks);
	}



}
