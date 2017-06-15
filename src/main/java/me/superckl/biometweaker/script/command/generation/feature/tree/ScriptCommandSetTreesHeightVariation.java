package me.superckl.biometweaker.script.command.generation.feature.tree;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetTreesHeightVariation implements IScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final int heightVariation;

	@Override
	public void perform() throws Exception {
		this.builder.setHeightVariation(this.heightVariation);
	}

}
