package me.superckl.biometweaker.script.command.effects;

import com.google.gson.JsonPrimitive;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyHelper;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeModificationManager.MobEffectModification;
import me.superckl.biometweaker.MobEffectPropertyManager;
import me.superckl.biometweaker.script.object.effect.MobEffectScriptObject;

@AutoRegister(classes = MobEffectScriptObject.class, name = "set")
@RequiredArgsConstructor
public class ScriptCommandSetMobEffectProperty extends ScriptCommand{

	private final MobEffectModification.Builder effect;
	private final String key;
	private final JsonPrimitive value;

	private ScriptHandler handler;

	@Override
	public void perform() throws Exception {
		final Property<?, ?, ?> prop = MobEffectPropertyManager.findProperty(this.key.toLowerCase());
		if(prop == null)
			throw new IllegalArgumentException("No property found for "+this.key);
		PropertyHelper.setProperty(this.effect, prop, this.value, this.handler);
	}

	@Override
	public void setScriptHandler(final ScriptHandler handler) {
		this.handler = handler;
	}

}
