package me.superckl.biometweaker.script.object;

import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.object.ScriptObject;
import me.superckl.biometweaker.BiomeTweaker;

public class TweakerScriptObject extends ScriptObject{

	@Override
	public void addCommand(final ScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

}
