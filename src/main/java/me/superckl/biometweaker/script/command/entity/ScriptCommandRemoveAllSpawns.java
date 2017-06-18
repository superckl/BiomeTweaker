package me.superckl.biometweaker.script.command.entity;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveAllSpawns implements IScriptCommand{

	private final IBiomePackage pack;
	private final SpawnListType type;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveAllSpawns(this, gen, this.type)))
				continue;
			this.removeEntries(gen, this.type);
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

	private void removeEntries(final Biome gen, final SpawnListType type){
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
