package me.superckl.biometweaker.script.command.generation.feature.ore;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.biometweaker.script.object.decoration.OreDecorationScriptObject;

@AutoRegister(classes = OreDecorationScriptObject.class, name = "setBlockToReplace")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="oreGenBuilder", parameterIndex=0)})

public class ScriptCommandSetOreBlockToReplace implements IScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		this.builder.setPredicate(new BlockEquivalencePredicate(this.block.build()));
	}

}
