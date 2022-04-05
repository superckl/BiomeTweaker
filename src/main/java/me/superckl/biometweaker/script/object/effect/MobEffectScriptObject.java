package me.superckl.biometweaker.script.object.effect;

import java.util.Arrays;

import lombok.Getter;
import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.object.ScriptObject;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.BiomeModificationManager.MobEffectModification;
import net.minecraft.resources.ResourceLocation;

public class MobEffectScriptObject extends ScriptObject{

	@Getter
	private MobEffectModification.Builder builder;

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
		this.builder = MobEffectModification.builder(new ResourceLocation((String) args[0]));
	}

	@Override
	public void addCommand(final ScriptCommand command) {
		BiomeTweakerAPI.getCommandAdder().accept(command);
	}

}
