package me.superckl.biometweaker.script.object.block;

import me.superckl.api.biometweaker.block.BasicBlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BlockStateScriptObject;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeTweaker;

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
	public void addCommand(final IScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

}
