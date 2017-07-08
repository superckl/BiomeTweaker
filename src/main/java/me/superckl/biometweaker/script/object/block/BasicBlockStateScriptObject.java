package me.superckl.biometweaker.script.object.block;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.block.BasicBlockStateBuilder;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BlockStateScriptObject;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.command.block.ScriptCommandSetBlockProperty;
import net.minecraft.util.ResourceLocation;

public class BasicBlockStateScriptObject extends BlockStateScriptObject<BasicBlockStateBuilder>{

	public BasicBlockStateScriptObject() {
		super(new BasicBlockStateBuilder());
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
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
	public void readArgs(final Object... args) throws Exception {
		if(args.length != 1 || !(args[0] instanceof String))
			throw new IllegalArgumentException("Invalid parameters to create a block object! Objects: "+Arrays.toString(args));
		this.builder.setrLoc(new ResourceLocation((String) args[0]));
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		final ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.JSON_ELEMENT.getSimpleWrapper()),
				ScriptCommandSetBlockProperty.class.getConstructor(BlockStateBuilder.class, String.class, JsonElement.class));
		validCommands.put("setProperty", listing);

		return validCommands;
	}

}
