package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

import com.google.gson.JsonElement;

@RequiredArgsConstructor
public class ScriptCommandSetBiomeProperty implements IScriptCommand{

	private final IBiomePackage pack;
	private final String key;
	private final JsonElement value;

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.SetProperty(this, gen, this.key, this.value)))
				continue;
			BiomeHelper.setBiomeProperty(this.key, this.value, gen);
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
