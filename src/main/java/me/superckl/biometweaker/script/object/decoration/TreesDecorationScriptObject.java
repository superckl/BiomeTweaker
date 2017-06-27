package me.superckl.biometweaker.script.object.decoration;

import java.util.Map;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandAddTreesSoilBlock;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesCheckGrow;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesGrowVines;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesHeight;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesHeightVariation;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesLeafBlock;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesLeafHeight;
import me.superckl.biometweaker.script.command.generation.feature.tree.ScriptCommandSetTreesVineBlock;

public class TreesDecorationScriptObject extends DecorationScriptObject<WorldGenTreesBuilder>{

	public TreesDecorationScriptObject() {
		super(new WorldGenTreesBuilder());
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = DecorationScriptObject.populateCommands();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.BOOLEAN.getSimpleWrapper()),
				ScriptCommandSetTreesGrowVines.class.getConstructor(WorldGenTreesBuilder.class, Boolean.TYPE));
		validCommands.put("setGrowVines", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.BOOLEAN.getSimpleWrapper()),
				ScriptCommandSetTreesCheckGrow.class.getConstructor(WorldGenTreesBuilder.class, Boolean.TYPE));
		validCommands.put("setCheckCanGrow", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetTreesHeight.class.getConstructor(WorldGenTreesBuilder.class, Integer.TYPE));
		validCommands.put("setHeight", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetTreesHeightVariation.class.getConstructor(WorldGenTreesBuilder.class, Integer.TYPE));
		validCommands.put("setHeightVariation", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetTreesLeafHeight.class.getConstructor(WorldGenTreesBuilder.class, Integer.TYPE));
		validCommands.put("setLeafHeight", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()),
				ScriptCommandSetTreesLeafBlock.class.getConstructor(WorldGenTreesBuilder.class, BlockStateBuilder.class));
		validCommands.put("setLeafBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()),
				ScriptCommandSetTreesVineBlock.class.getConstructor(WorldGenTreesBuilder.class, BlockStateBuilder.class));
		validCommands.put("setVineBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()),
				ScriptCommandAddTreesSoilBlock.class.getConstructor(WorldGenTreesBuilder.class, BlockStateBuilder.class));
		validCommands.put("addSoilBlock", listing);

		return validCommands;
	}

}
