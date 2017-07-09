package me.superckl.biometweaker.script.command.generation.feature.cluster;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.script.object.decoration.ClusterDecorationScriptObject;

@AutoRegister(classes = ClusterDecorationScriptObject.class, name = "setHeight")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="clusterGenBuilder", parameterIndex=0), @ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1)})
public class ScriptCommandSetClusterHeight implements IScriptCommand{

	private final WorldGenClusterBuilder builder;
	private final int height;

	@Override
	public void perform() throws Exception {
		this.builder.setHeight(this.height);
	}

}
