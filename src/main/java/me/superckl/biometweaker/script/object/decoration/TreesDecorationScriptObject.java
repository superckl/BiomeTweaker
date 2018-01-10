package me.superckl.biometweaker.script.object.decoration;

import me.superckl.biometweaker.common.world.gen.feature.WorldGenTreesBuilder;

public class TreesDecorationScriptObject extends DecorationScriptObject<WorldGenTreesBuilder>{

	public TreesDecorationScriptObject() {
		super(new WorldGenTreesBuilder());
	}

}
