package me.superckl.biometweaker.script.command;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveFeature implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveDecoration(this, BiomeGenBase.getBiome(i), i, this.type)))
				continue;
			if(!BiomeEventHandler.getPopulateTypes().containsKey(i))
				BiomeEventHandler.getPopulateTypes().put(i, new ArrayList<String>());
			BiomeEventHandler.getPopulateTypes().get(i).add(this.type);
		}
	}

}
