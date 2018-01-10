package me.superckl.biometweaker.script.command.generation.feature.ore;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.biometweaker.script.object.decoration.OreDecorationScriptObject;

@AutoRegister(classes = OreDecorationScriptObject.class, name = "addBlockToReplace")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="oreGenBuilder", parameterIndex=0)})

public class ScriptCommandAddOreBlockToReplace extends ScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		this.builder.addReplacePredicate(new BlockEquivalencePredicate(this.block.build()));
	}

}
