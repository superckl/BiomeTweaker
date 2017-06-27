package me.superckl.biometweaker.script.object.decoration;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.object.DecorationBuilderScriptObject;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetDecorationBlock;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetDecorationCount;

public abstract class DecorationScriptObject<V extends WorldGeneratorBuilder<?>> extends DecorationBuilderScriptObject<V>{

	public DecorationScriptObject(final V builder) {
		super(builder);
	}

	@Override
	public Pair<Constructor<? extends IScriptCommand>, Object[]> modifyConstructorPair(
			final Pair<Constructor<? extends IScriptCommand>, Object[]> pair, final String[] args, final ScriptHandler handler) {
		final Object[] newArgs = new Object[pair.getValue().length+1];
		System.arraycopy(pair.getValue(), 0, newArgs, 1, pair.getValue().length);
		newArgs[0] = this.builder;
		return Pair.of(pair.getKey(), newArgs);
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()),
				ScriptCommandSetDecorationBlock.class.getConstructor(WorldGeneratorBuilder.class, BlockStateBuilder.class));
		validCommands.put("setBlock", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetDecorationCount.class.getConstructor(WorldGeneratorBuilder.class, Integer.TYPE));
		validCommands.put("setCount", listing);

		return validCommands;
	}
}
