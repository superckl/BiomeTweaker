package me.superckl.api.biometweaker.script.object;

import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.object.ScriptObject;

public abstract class DecorationBuilderScriptObject<V extends WorldGeneratorBuilder<?>> extends ScriptObject{

	protected final V builder;

	public DecorationBuilderScriptObject(final V builder) {
		this.builder = builder;
	}

	public V getBuilder() {
		return this.builder;
	}

}
