package me.superckl.biometweaker.script.command.generation.feature.cluster;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.biometweaker.script.object.decoration.ClusterDecorationScriptObject;

@AutoRegister(classes = ClusterDecorationScriptObject.class, name = "addSoilBlock")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="clusterGenBuilder", parameterIndex=0)})
public class ScriptCommandAddClusterSoilBlock implements IScriptCommand{

	private final WorldGenClusterBuilder builder;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		this.builder.addSoilPredicate(new BlockEquivalencePredicate(this.block.build()));
	}

}
