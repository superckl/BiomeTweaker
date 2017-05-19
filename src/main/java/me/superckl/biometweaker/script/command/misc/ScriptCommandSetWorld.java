package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.TweakWorldManager;

@RequiredArgsConstructor
public class ScriptCommandSetWorld implements IScriptCommand{

	private final Integer dim;

	public ScriptCommandSetWorld() {
		this(null);
	}

	@Override
	public void perform() throws Exception {
		TweakWorldManager.setCurrentWorld(this.dim);
	}

}
