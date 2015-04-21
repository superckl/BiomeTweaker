package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveAllSpawns implements IScriptCommand{

	private final IBiomePackage pack;
	private final Type type;

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveAllSpawns(this, gen, this.type)))
				continue;
			this.removeEntries(gen, this.type);
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

	private void removeEntries(final BiomeGenBase gen, final Type type){
		switch(this.type){
		case CAVE_CREATURE:{
			gen.spawnableCaveCreatureList.clear();
			break;
		}
		case CREATURE:{
			gen.spawnableCreatureList.clear();
			break;
		}
		case MONSTER:{
			gen.spawnableMonsterList.clear();
			break;
		}
		case WATER_CREATURE:{
			gen.spawnableWaterCreatureList.clear();
		}
		default:
			break;
		}
	}

}
