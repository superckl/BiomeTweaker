package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetDecorationCount implements IScriptCommand{

	private final WorldGeneratorBuilder<?> builder;
	private final int count;

	@Override
	public void perform() throws Exception {
		this.builder.setCount(this.count);
	}

}
