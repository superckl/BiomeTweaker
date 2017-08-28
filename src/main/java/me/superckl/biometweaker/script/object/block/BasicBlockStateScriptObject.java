package me.superckl.biometweaker.script.object.block;

import java.util.Arrays;

import me.superckl.api.biometweaker.block.BasicBlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BlockStateScriptObject;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.util.ResourceLocation;

public class BasicBlockStateScriptObject extends BlockStateScriptObject<BasicBlockStateBuilder>{

	public BasicBlockStateScriptObject() {
		super(new BasicBlockStateBuilder());
	}

	@Override
	public String[] modifyArguments(final String[] args, final ScriptHandler handler) {
		final String name = CollectionHelper.reverseLookup(handler.getObjects(), this);
		final String[] newArgs = new String[args.length+1];
		newArgs[0] = name;
		System.arraycopy(args, 0, newArgs, 1, args.length);
		return newArgs;
	}

	@Override
	public void readArgs(final Object... args) throws Exception {
		if(args.length != 1 || !(args[0] instanceof String))
			throw new IllegalArgumentException("Invalid parameters to create a block object! Objects: "+Arrays.toString(args));
		this.builder.setrLoc(new ResourceLocation((String) args[0]));
	}

	@Override
	public void addCommand(final ScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

}
