package me.superckl.biometweaker.script.object;

import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeTweaker;

public class BiomesScriptObject extends BiomePackScriptObject{

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

	@Override
	public void readArgs(final Object... packs) throws Exception {
		final IBiomePackage[] bPacks = new IBiomePackage[packs.length];
		System.arraycopy(packs, 0, bPacks, 0, packs.length);
		if(bPacks.length == 1)
			this.pack = bPacks[0];
		else
			this.pack = new MergedBiomesPackage(bPacks);
	}



}
