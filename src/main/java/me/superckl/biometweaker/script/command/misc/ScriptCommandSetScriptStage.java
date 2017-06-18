package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.ScriptCommandManager.ApplicationStage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.util.LogHelper;

@RequiredArgsConstructor
public class ScriptCommandSetScriptStage implements IScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final ApplicationStage stage = ApplicationStage.valueOf(this.stage);
		if(stage == null){
			LogHelper.error("Invalid application stage specified!");
			return;
		}
		BiomeTweaker.getInstance().getCommandManager().setCurrentStage(stage);
	}

}
