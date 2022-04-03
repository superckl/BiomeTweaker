package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.TweakerScriptObject;

@AutoRegister(classes = TweakerScriptObject.class, name = "setStage")
@AutoRegister(classes = TweakerScriptObject.class, name = "setScriptStage")
@RequiredArgsConstructor
public class ScriptCommandSetScriptStage extends ScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final ApplicationStage stage = ApplicationStage.valueOf(this.stage);
		if(stage == null){
			BiomeTweaker.LOG.error("Invalid application stage specified!");
			return;
		}
		BiomeTweaker.getINSTANCE().getCommandManager().setCurrentStage(stage);
	}

	@Override
	public boolean performInst() {
		return true;
	}

}
