package me.superckl.biometweaker.script.command.block;

import com.google.gson.JsonPrimitive;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyHelper;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.ReplacementPropertyManager;
import me.superckl.biometweaker.script.object.block.BlockReplacementScriptObject;

@AutoRegister(classes = BlockReplacementScriptObject.class, name = "set")
@RequiredArgsConstructor
public class ScriptCommandSetReplacementProperty extends ScriptCommand{

	private final ReplacementConstraints constraints;
	private final String key;
	private final JsonPrimitive value;

	private ScriptHandler handler;

	@Override
	public void perform() throws Exception {
		final Property<?, ?, ?> prop = ReplacementPropertyManager.findProperty(this.key.toLowerCase());
		if(prop == null)
			throw new IllegalArgumentException("No property found for "+this.key);
		PropertyHelper.setProperty(this.constraints, prop, this.value, this.handler);
	}

	@Override
	public void setScriptHandler(final ScriptHandler handler) {
		this.handler = handler;
	}

}
