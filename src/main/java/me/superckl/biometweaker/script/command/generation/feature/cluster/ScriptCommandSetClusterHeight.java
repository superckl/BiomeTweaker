package me.superckl.biometweaker.script.command.generation.feature.cluster;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetClusterHeight implements IScriptCommand{

	private final WorldGenClusterBuilder builder;
	private final int height;

	@Override
	public void perform() throws Exception {
		this.builder.setHeight(this.height);
	}

}
