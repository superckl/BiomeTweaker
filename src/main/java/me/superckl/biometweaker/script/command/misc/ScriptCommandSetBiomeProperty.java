package me.superckl.biometweaker.script.command.misc;

import java.util.Iterator;

import com.google.gson.JsonElement;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "set")
@RequiredArgsConstructor
public class ScriptCommandSetBiomeProperty extends ScriptCommand{

	private final BiomePackage pack;
	private final String key;
	private final JsonElement value;

	private ScriptHandler handler;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.SetProperty(this, gen, this.key, this.value)))
				continue;
			BiomePropertyManager.setProperty(gen, this.key.toLowerCase(), this.value, this.handler);
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

	@Override
	public void setScriptHandler(final ScriptHandler handler) {
		this.handler = handler;
	}

}
