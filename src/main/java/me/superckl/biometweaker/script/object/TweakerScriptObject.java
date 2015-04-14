package me.superckl.biometweaker.script.object;

import me.superckl.biometweaker.script.ParameterType;
import me.superckl.biometweaker.script.ScriptCommandListing;
import me.superckl.biometweaker.script.command.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveBigTreeGen;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveTreeGen;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;
import me.superckl.biometweaker.script.command.ScriptCommandSetStage;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

public class TweakerScriptObject extends ScriptObject{

	@Override
	public void populateCommands() throws Exception {
		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(Integer.TYPE));
		this.validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING, ParameterType.SPAWN_TYPE, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING, ParameterType.SPAWN_TYPE)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, String.class, Type.class));
		this.validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.SPAWN_TYPE)
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(Integer.TYPE, Type.class));
		this.validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(Integer.TYPE, String.class, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(Integer.TYPE, String.class, Integer.TYPE));
		this.validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING, ParameterType.JSON_ELEMENT)
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(Integer.TYPE, String.class, JsonElement.class));
		this.validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER), ScriptCommandRemoveTreeGen.class.getDeclaredConstructor(Integer.TYPE));
		this.validCommands.put("removeTreeGen", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER), ScriptCommandRemoveBigTreeGen.class.getDeclaredConstructor(Integer.TYPE));
		this.validCommands.put("removeBigTreeGen", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING), ScriptCommandSetStage.class.getDeclaredConstructor(String.class));
		listing.setPerformInst(true);
		this.validCommands.put("setStage", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(Integer.TYPE, String.class));
		this.validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.STRING), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(Integer.TYPE, String.class));
		this.validCommands.put("removeDicType", listing);
	}

}
