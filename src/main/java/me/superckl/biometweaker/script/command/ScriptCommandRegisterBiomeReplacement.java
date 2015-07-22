package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import net.minecraft.world.biome.BiomeGenBase;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.pack.IBiomePackage;

@RequiredArgsConstructor
public class ScriptCommandRegisterBiomeReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int replaceWith;
	
	@Override
	public void perform() throws Exception {
		Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			int id = it.next().biomeID;
			BiomeEventHandler.getBiomeReplacements().put(id, this.replaceWith);
			Config.INSTANCE.onTweak(id);
		}
	}

}
