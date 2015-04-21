package me.superckl.biometweaker.script.command;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveDecoration implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveDecoration(this, BiomeGenBase.getBiome(i), i, this.type)))
				continue;
			if(!BiomeEventHandler.getDecorateTypes().containsKey(i))
				BiomeEventHandler.getDecorateTypes().put(i, new ArrayList<String>());
			BiomeEventHandler.getDecorateTypes().get(i).add(this.type);
		}
	}

}
