package me.superckl.biometweaker.script.command.generation.feature;

import com.google.gson.JsonElement;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyHelper;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenPropertyManager;
import me.superckl.biometweaker.script.object.decoration.DecorationScriptObject;

@AutoRegister(classes = DecorationScriptObject.class, name = "set")
@RequiredArgsConstructor
public class ScriptCommandSetDecorationProperty extends ScriptCommand{

	private final WorldGeneratorBuilder<?> builder;
	private final String key;
	private final JsonElement value;

	private ScriptHandler handler;

	@Override
	public void perform() throws Exception {
		final Property<?> prop = WorldGenPropertyManager.findProperty(this.builder.getClass(), this.key.toLowerCase());
		if(prop == null)
			throw new IllegalArgumentException("No property found for "+this.key);
		PropertyHelper.setProperty(this.builder, prop, this.value, this.handler);
	}

	@Override
	public void setScriptHandler(final ScriptHandler handler) {
		this.handler = handler;
	}

}
