package me.superckl.biometweaker.script.command.generation.feature.tree;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.script.object.decoration.TreesDecorationScriptObject;

@AutoRegister(classes = TreesDecorationScriptObject.class, name = "setCheckCanGrow")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="treeGenBuilder", parameterIndex=0)})
public class ScriptCommandSetTreesCheckGrow implements IScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final boolean vines;

	@Override
	public void perform() throws Exception {
		this.builder.setCheckCanGrow(this.vines);
	}

}
