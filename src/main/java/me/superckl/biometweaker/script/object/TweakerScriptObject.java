package me.superckl.biometweaker.script.object;

import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.command.ScriptCommandAddActualFillerBlock;
import me.superckl.biometweaker.script.command.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddToGeneration;
import me.superckl.biometweaker.script.command.ScriptCommandAverageBiomeSize;
import me.superckl.biometweaker.script.command.ScriptCommandRegisterBiomeReplacement;
import me.superckl.biometweaker.script.command.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.ScriptCommandRegisterVillageBlockReplacement;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.command.ScriptCommandSetStage;

public class TweakerScriptObject extends ScriptObject{

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper(),
				ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class));
		validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, SpawnListType.class));
		validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(),
				ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandSetStage.class.getDeclaredConstructor(String.class));
		listing.setPerformInst(true);
		validCommands.put("setStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("addActualFillerBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDecoration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeFeature", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		validCommands.put("registerGenVillageBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(String.class, Byte.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(Byte.TYPE));
		validCommands.put("setAverageBiomeSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBiomeReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("registerGenBiomeRep", listing);

		return validCommands;
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		Config.INSTANCE.addCommand(command);
	}

}
