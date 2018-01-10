package me.superckl.biometweaker.script.object.decoration;

import me.superckl.biometweaker.common.world.gen.feature.WorldGenSplotchBuilder;

public class SplotchDecorationScriptObject extends DecorationScriptObject<WorldGenSplotchBuilder>{

	public SplotchDecorationScriptObject() {
		super(new WorldGenSplotchBuilder());
	}

}
