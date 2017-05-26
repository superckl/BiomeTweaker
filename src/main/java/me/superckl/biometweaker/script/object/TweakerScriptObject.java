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
import me.superckl.biometweaker.script.command.generation.ScriptCommandAverageBiomeSize;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBiomeReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterVillageBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.misc.ScriptCommandDisableBonemealUse;
import me.superckl.biometweaker.script.command.misc.ScriptCommandInheritProperties;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetReplacementStage;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetScriptStage;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetWorld;

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
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandSetScriptStage.class.getDeclaredConstructor(String.class));
		listing.setPerformInst(true);
		validCommands.put("setStage", listing);
		validCommands.put("setScriptStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandSetReplacementStage.class.getDeclaredConstructor(String.class));
		validCommands.put("setReplacementStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.INTEGER.getSimpleWrapper()), ScriptCommandSetWorld.class.getDeclaredConstructor(Integer.class));
		listing.addEntry(Lists.<ParameterWrapper>newArrayList(), ScriptCommandSetWorld.class.getDeclaredConstructor());
		validCommands.put("setWorld", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addActualFillerBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeDecoration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeFeature", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);


		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.TYPE, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, Integer.TYPE, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, String.class, String.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.class, String.class, Integer.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, String.class, String.class));
		validCommands.put("registerGenVillageBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("setMaxSpawnPackSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper())
				, ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("disableBonemealUse", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(String.class, Byte.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_BYTE.getSimpleWrapper()), ScriptCommandAverageBiomeSize.class.getDeclaredConstructor(Byte.TYPE));
		validCommands.put("setAverageBiomeSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBiomeReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("registerGenBiomeRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper())
				, ScriptCommandInheritProperties.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class, String[].class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper())
				, ScriptCommandInheritProperties.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
		validCommands.put("inheritProperties", listing);

		return validCommands;
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		Config.INSTANCE.addCommand(command);
	}

}
