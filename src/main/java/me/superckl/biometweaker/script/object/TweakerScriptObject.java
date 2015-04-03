package me.superckl.biometweaker.script.object;

import me.superckl.biometweaker.script.ParameterType;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandListing;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveBigTreeGen;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveTreeGen;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;

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
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.SPAWN_TYPE, ParameterType.STRING, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, Type.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.NON_NEG_INTEGER, ParameterType.SPAWN_TYPE, ParameterType.STRING)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, Type.class, String.class));
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
	}

}
