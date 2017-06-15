package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetOreCount implements IScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final int count;

	@Override
	public void perform() throws Exception {
		this.builder.setCount(this.count);
	}

}
