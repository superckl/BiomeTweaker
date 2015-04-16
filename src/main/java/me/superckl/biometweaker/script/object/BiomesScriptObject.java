package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ParameterType;
import me.superckl.biometweaker.script.ScriptCommandListing;
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
import me.superckl.biometweaker.util.CollectionHelper;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;

public class BiomesScriptObject extends ScriptObject{

	private final int[] biomes;

	public BiomesScriptObject(final int ... biomes) {
		super();
		this.biomes = biomes;
	}

	@Override
	public void handleCall(final String call, final ScriptHandler handler) throws Exception{
		final String command = ScriptParser.getCommandCalled(call);
		if(!this.validCommands.containsKey(command)){
			ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
			return;
		}
		final String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(call));
		final ScriptCommandListing listing = this.validCommands.get(command);
		for(final Entry<List<ParameterType>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
			if(arguments.length != entry.getKey().size())
				continue;
			final Object[] objs = new Object[arguments.length];
			final List<ParameterType> list = entry.getKey();
			boolean shouldCont = false;
			for(int i = 0; i < arguments.length; i++){
				objs[i] = list.get(i).tryParse(arguments[i]);
				if(objs[i] == null){
					shouldCont = true;
					break;
				}
			}
			if(shouldCont)
				continue;
			//ParamterType list does not contain biomeIDs, so insert them.
			final Object[] args = new Object[objs.length+1];
			System.arraycopy(objs, 0, args, 1, objs.length);
			for(final int i:this.biomes){
				args[0] = i;
				Config.INSTANCE.addCommand(entry.getValue().newInstance(args));
			}
			return;
		}
		ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	@Override
	public void populateCommands() throws Exception {
		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterType>()
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(Integer.TYPE));
		this.validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING, ParameterType.SPAWN_TYPE, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, String.class, Type.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING, ParameterType.SPAWN_TYPE)
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(Integer.TYPE, String.class, Type.class));
		this.validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.SPAWN_TYPE)
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(Integer.TYPE, Type.class));
		this.validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING, ParameterType.NON_NEG_INTEGER, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(Integer.TYPE, String.class, Integer.TYPE, Integer.TYPE));
		this.validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING, ParameterType.NON_NEG_INTEGER)
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(Integer.TYPE, String.class, Integer.TYPE));
		this.validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING, ParameterType.JSON_ELEMENT)
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(Integer.TYPE, String.class, JsonElement.class));
		this.validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(Integer.TYPE, String.class));
		this.validCommands.put("addDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(Integer.TYPE, String.class));
		this.validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(new ArrayList<ParameterType>(), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(Integer.TYPE));
		this.validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterType.STRING), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(Integer.TYPE, String.class));
		this.validCommands.put("removeDecoration", listing);
	}

}
