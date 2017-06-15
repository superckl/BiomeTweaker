package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetOreHeights implements IScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final int max;
	private final int min;

	@Override
	public void perform() throws Exception {
		this.builder.setMaxHeight(this.max);
		this.builder.setMinHeight(this.min);
	}

}
