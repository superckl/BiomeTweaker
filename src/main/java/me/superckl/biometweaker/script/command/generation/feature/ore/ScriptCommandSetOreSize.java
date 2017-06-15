package me.superckl.biometweaker.script.command.generation.feature.ore;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetOreSize implements IScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final int size;

	@Override
	public void perform() throws Exception {
		this.builder.setSize(this.size);
	}

}
