package me.superckl.biometweaker.script.command.generation;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveFeature implements IScriptCommand{

	private final IBiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds())
			for (final String type:this.types) {
				if (MinecraftForge.EVENT_BUS
						.post(new BiomeTweakEvent.RemoveFeature(this, Biome.getBiome(i), i, type)))
					continue;
				if (!BiomeEventHandler.getPopulateTypes().containsKey(i))
					BiomeEventHandler.getPopulateTypes().put(i, new ArrayList<String>());
				BiomeEventHandler.getPopulateTypes().get(i).add(type);
			}
	}

}
