package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.ReplacementStage;
import me.superckl.biometweaker.util.LogHelper;

@RequiredArgsConstructor
public class ScriptCommandSetReplacementStage implements IScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final ReplacementStage stage = ReplacementStage.valueOf(this.stage);
		if(stage == null){
			LogHelper.error("Invalid replacement stage specified!");
			return;
		}
		BlockReplacementManager.setCurrentStage(stage);
	}

}
