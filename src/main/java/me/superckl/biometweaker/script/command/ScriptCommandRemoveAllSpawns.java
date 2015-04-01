package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandRemoveAllSpawns implements IScriptCommand{

	private final int biomeID;
	private final Type type;

	@Override
	public void perform() throws Exception {
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
				if(gen != null){
					this.removeEntries(gen, this.type);
					Config.INSTANCE.onTweak(gen.biomeID);
				}
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		this.removeEntries(gen, this.type);
		Config.INSTANCE.onTweak(this.biomeID);
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
