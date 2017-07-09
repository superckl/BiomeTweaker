package me.superckl.biometweaker.script.object;

import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.biometweaker.BiomeTweaker;

public class TweakerScriptObject extends ScriptObject{

	@Override
	public void addCommand(final IScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

}
