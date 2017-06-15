package me.superckl.biometweaker.script.object.decoration;

import java.util.Map;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.script.command.generation.feature.ore.ScriptCommandSetOreBlockToReplace;
import me.superckl.biometweaker.script.command.generation.feature.ore.ScriptCommandSetOreHeights;
import me.superckl.biometweaker.script.command.generation.feature.ore.ScriptCommandSetOreSize;

public class OreDecorationScriptObject extends DecorationScriptObject<WorldGenMineableBuilder>{

	public OreDecorationScriptObject() {
		super(new WorldGenMineableBuilder());
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = DecorationScriptObject.populateCommands();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetOreSize.class.getConstructor(WorldGenMineableBuilder.class, Integer.TYPE));
		validCommands.put("setSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetOreHeights.class.getConstructor(WorldGenMineableBuilder.class, Integer.TYPE, Integer.TYPE));
		validCommands.put("setHeights", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetOreBlockToReplace.class.getConstructor(WorldGenMineableBuilder.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()),
				ScriptCommandSetOreBlockToReplace.class.getConstructor(WorldGenMineableBuilder.class, String.class));
		validCommands.put("setBlockToReplace", listing);


		return validCommands;
	}

}
