package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetTreesGrowVines implements IScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final boolean vines;

	@Override
	public void perform() throws Exception {
		this.builder.setGrowVines(this.vines);
	}

}
