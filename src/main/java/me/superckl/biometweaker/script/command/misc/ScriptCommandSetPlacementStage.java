package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import me.superckl.biometweaker.common.world.gen.feature.DecorationManager;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.LogHelper;

@AutoRegister(classes = TweakerScriptObject.class, name = "setPlacementStage")
@RequiredArgsConstructor
public class ScriptCommandSetPlacementStage extends ScriptCommand{

	private final String stage;

	@Override
	public void perform() throws Exception {
		final PlacementStage stage = PlacementStage.valueOf(this.stage);
		if(stage == null){
			LogHelper.error("Invalid replacement stage specified!");
			return;
		}
		BlockReplacementManager.setCurrentStage(stage);
		DecorationManager.setCurrentStage(stage);
	}

}
