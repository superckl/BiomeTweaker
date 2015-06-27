package me.superckl.biometweaker.script.object;

import java.util.Map;

import me.superckl.biometweaker.script.command.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandAddToGeneration;
import me.superckl.biometweaker.script.command.ScriptCommandAverageBiomeSize;
import me.superckl.biometweaker.script.command.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.command.ScriptCommandSetStage;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.script.util.ParameterType;
import me.superckl.biometweaker.script.util.ScriptCommandListing;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

public class TweakerScriptObject extends ScriptObject{

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper(),
				ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class));
		validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, Type.class));
		validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(),
				ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper()), ScriptCommandSetStage.class.getDeclaredConstructor(String.class));
		listing.setPerformInst(true);
		validCommands.put("setStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeDecoration", listing);
		
		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		validCommands.put("removeFeature", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);
		
		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING.getSimpleWrapper(), ParameterType.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(String.class, Byte.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(Byte.TYPE));
		validCommands.put("setAverageBiomeSize", listing);
		
		return validCommands;
	}

}
