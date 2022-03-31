package me.superckl.biometweaker.script.object;

import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.object.ScriptObject;

public class TweakerScriptObject extends ScriptObject{

	@Override
	public void addCommand(final ScriptCommand command) {
		BiomeTweakerAPI.getCommandAdder().accept(command);
	}

}
