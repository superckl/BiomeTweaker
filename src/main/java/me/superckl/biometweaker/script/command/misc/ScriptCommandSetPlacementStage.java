package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import me.superckl.biometweaker.common.world.gen.feature.DecorationManager;
import me.superckl.biometweaker.util.LogHelper;

@RequiredArgsConstructor
public class ScriptCommandSetPlacementStage implements IScriptCommand{

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
