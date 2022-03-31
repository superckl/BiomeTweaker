package me.superckl.api.biometweaker.script.object;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.superscript.script.object.ScriptObject;

public abstract class BlockStateScriptObject<V extends BlockStateBuilder<?>> extends ScriptObject{

	protected final V builder;

	public BlockStateScriptObject(final V builder) {
		this.builder = builder;
	}

	public V getBuilder() {
		return this.builder;
	}

}
