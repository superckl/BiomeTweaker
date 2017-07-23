package me.superckl.biometweaker.script.object.decoration;

import me.superckl.api.biometweaker.script.object.DecorationBuilderScriptObject;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeTweaker;

public abstract class DecorationScriptObject<V extends WorldGeneratorBuilder<?>> extends DecorationBuilderScriptObject<V>{

	public DecorationScriptObject(final V builder) {
		super(builder);
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
	public void addCommand(final ScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

}
