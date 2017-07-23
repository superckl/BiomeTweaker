package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.script.object.decoration.DecorationScriptObject;

@AutoRegister(classes = DecorationScriptObject.class, name = "setCount")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1)})
public class ScriptCommandSetDecorationCount extends ScriptCommand{

	private final WorldGeneratorBuilder<?> builder;
	private final int count;

	@Override
	public void perform() throws Exception {
		this.builder.setCount(this.count);
	}

}
