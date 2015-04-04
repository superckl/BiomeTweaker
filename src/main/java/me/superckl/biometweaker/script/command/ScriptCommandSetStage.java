package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.ScriptCommandManager.ApplicationStage;
import me.superckl.biometweaker.util.LogHelper;

@RequiredArgsConstructor
public class ScriptCommandSetStage implements IScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final ApplicationStage stage = ApplicationStage.valueOf(this.stage);
		if(stage == null){
			LogHelper.info("Invalid application stage specified!");
			return;
		}
		Config.INSTANCE.getCommandManager().setCurrentStage(stage);
	}

}
