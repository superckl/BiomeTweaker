package me.superckl.biometweaker.script.object;

import me.superckl.biometweaker.script.IBiomePackage;
import me.superckl.biometweaker.script.ParameterType;
import me.superckl.biometweaker.script.ScriptCommandListing;
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
import me.superckl.biometweaker.script.command.ScriptCommandSetStage;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

public class TweakerScriptObject extends ScriptObject{

	@Override
	public void populateCommands() throws Exception {
		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE)
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		this.validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING, ParameterType.SPAWN_TYPE, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING, ParameterType.SPAWN_TYPE)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, Type.class));
		this.validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.SPAWN_TYPE)
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, Type.class));
		this.validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		this.validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING, ParameterType.JSON_ELEMENT)
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		this.validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING), ScriptCommandSetStage.class.getDeclaredConstructor(String.class));
		listing.setPerformInst(true);
		this.validCommands.put("setStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		this.validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOME_PACKAGE, ParameterType.STRING), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		this.validCommands.put("removeDecoration", listing);
	}

}
