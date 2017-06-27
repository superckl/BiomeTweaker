package me.superckl.biometweaker.script.object.decoration;

import java.util.Map;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.script.command.generation.feature.cluster.ScriptCommandAddClusterSoilBlock;
import me.superckl.biometweaker.script.command.generation.feature.cluster.ScriptCommandSetClusterHeight;
import me.superckl.biometweaker.script.command.generation.feature.cluster.ScriptCommandSetClusterRadius;

public class ClusterDecorationScriptObject extends DecorationScriptObject<WorldGenClusterBuilder>{

	public ClusterDecorationScriptObject() {
		super(new WorldGenClusterBuilder());
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = DecorationScriptObject.populateCommands();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetClusterRadius.class.getConstructor(WorldGenClusterBuilder.class, Integer.TYPE));
		validCommands.put("setRadius", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetClusterHeight.class.getConstructor(WorldGenClusterBuilder.class, Integer.TYPE));
		validCommands.put("setHeight", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()),
				ScriptCommandAddClusterSoilBlock.class.getConstructor(WorldGenClusterBuilder.class, BlockStateBuilder.class));
		validCommands.put("addSoilBlock", listing);

		return validCommands;
	}

}
