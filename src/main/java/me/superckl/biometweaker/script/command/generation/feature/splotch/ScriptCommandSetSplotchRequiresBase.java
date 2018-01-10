package me.superckl.biometweaker.script.command.generation.feature.splotch;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenSplotchBuilder;
import me.superckl.biometweaker.script.object.decoration.SplotchDecorationScriptObject;

@AutoRegister(classes = SplotchDecorationScriptObject.class, name = "setRequiresBaseBlock")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="splotchGenBuilder", parameterIndex=0)})
public class ScriptCommandSetSplotchRequiresBase extends ScriptCommand{

	private final WorldGenSplotchBuilder builder;
	private final boolean requiresBase;

	@Override
	public void perform() throws Exception {
		this.builder.setRequiresBase(this.requiresBase);
	}

}
