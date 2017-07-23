package me.superckl.biometweaker.script.command.generation;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeDecoration")
@RequiredArgsConstructor
public class ScriptCommandRemoveDecoration extends ScriptCommand{

	private final BiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds())
			for (final String type:this.types) {
				if (MinecraftForge.EVENT_BUS
						.post(new BiomeTweakEvent.RemoveDecoration(this, Biome.getBiome(i), i, type)))
					continue;
				if (!BiomeEventHandler.getDecorateTypes().containsKey(i))
					BiomeEventHandler.getDecorateTypes().put(i, new ArrayList<String>());
				BiomeEventHandler.getDecorateTypes().get(i).add(type);
			}
	}

}
