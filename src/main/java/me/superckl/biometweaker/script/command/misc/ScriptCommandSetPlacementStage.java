package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import me.superckl.biometweaker.script.object.TweakerScriptObject;

@AutoRegister(classes = TweakerScriptObject.class, name = "setPlacementStage")
@RequiredArgsConstructor
public class ScriptCommandSetPlacementStage extends ScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final PlacementStage stage = PlacementStage.valueOf(this.stage);
		if(stage == null){
			BiomeTweaker.LOG.error("Invalid replacement stage specified!");
			return;
		}
		BlockReplacementManager.setCurrentStage(stage);
	}

}
