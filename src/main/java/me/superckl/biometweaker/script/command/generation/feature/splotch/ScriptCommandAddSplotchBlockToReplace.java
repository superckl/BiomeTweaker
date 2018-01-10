package me.superckl.biometweaker.script.command.generation.feature.splotch;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenSplotchBuilder;
import me.superckl.biometweaker.script.object.decoration.SplotchDecorationScriptObject;

@AutoRegister(classes = SplotchDecorationScriptObject.class, name = "addBlockToReplace")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="splotchGenBuilder", parameterIndex=0)})
public class ScriptCommandAddSplotchBlockToReplace extends ScriptCommand{

	private final WorldGenSplotchBuilder builder;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		this.builder.addReplacementPredicate(new BlockEquivalencePredicate(this.block.build()));
	}

}
