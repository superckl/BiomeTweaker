package me.superckl.biometweaker.script.command.misc;

import java.util.Iterator;

import com.google.gson.JsonPrimitive;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.EarlyBiomeProperty;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyHelper;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "set")
@RequiredArgsConstructor
public class ScriptCommandSetBiomeProperty extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String key;
	private final JsonPrimitive value;

	private ScriptHandler handler;

	@Override
	public void perform() throws Exception {
		final Iterator<ResourceLocation> it = this.pack.locIterator();
		while(it.hasNext()){
			final ResourceLocation loc = it.next();
			final Property<?, ?> prop = BiomePropertyManager.findProperty(this.key.toLowerCase());
			if(prop == null)
				throw new IllegalArgumentException("No property found for "+this.key);
			if(prop instanceof EarlyBiomeProperty)
				PropertyHelper.setProperty(loc, prop, this.value, this.handler);
			else
				PropertyHelper.setProperty(ForgeRegistries.BIOMES.getValue(loc), prop, this.value, this.handler);
		}
	}

	@Override
	public void setScriptHandler(final ScriptHandler handler) {
		this.handler = handler;
	}

}
