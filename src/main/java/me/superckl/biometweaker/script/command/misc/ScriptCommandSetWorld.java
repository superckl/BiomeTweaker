package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.script.object.TweakerScriptObject;

@AutoRegister(classes = TweakerScriptObject.class, name = "setWorld")
@RequiredArgsConstructor
public class ScriptCommandSetWorld extends ScriptCommand{

	private final Integer dim;

	public ScriptCommandSetWorld() {
		this(null);
	}

	@Override
	public void perform() throws Exception {
		TweakWorldManager.setCurrentWorld(this.dim);
	}

}
