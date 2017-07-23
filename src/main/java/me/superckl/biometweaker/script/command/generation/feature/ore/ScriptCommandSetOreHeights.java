package me.superckl.biometweaker.script.command.generation.feature.ore;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.script.object.decoration.OreDecorationScriptObject;

@AutoRegister(classes = OreDecorationScriptObject.class, name = "setHeights")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="oreGenBuilder", parameterIndex=0),
		@ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1), @ParameterOverride(exceptionKey="nonNegInt", parameterIndex=2)})
public class ScriptCommandSetOreHeights extends ScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final int max;
	private final int min;

	@Override
	public void perform() throws Exception {
		this.builder.setMaxHeight(this.max);
		this.builder.setMinHeight(this.min);
	}

}
