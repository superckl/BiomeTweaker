package me.superckl.biometweaker.script.command.misc;

import java.util.Iterator;

import com.google.gson.JsonElement;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandSetBiomeProperty implements IScriptCommand{

	private final IBiomePackage pack;
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
