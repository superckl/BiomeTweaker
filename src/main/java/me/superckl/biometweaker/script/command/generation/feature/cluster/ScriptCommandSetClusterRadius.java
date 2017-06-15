package me.superckl.biometweaker.script.command.generation.feature.cluster;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.api.superscript.command.IScriptCommand;

@RequiredArgsConstructor
public class ScriptCommandSetClusterRadius implements IScriptCommand{

	private final WorldGenClusterBuilder builder;
	private final int radius;

	@Override
	public void perform() throws Exception {
		this.builder.setRadius(this.radius);
	}

}
