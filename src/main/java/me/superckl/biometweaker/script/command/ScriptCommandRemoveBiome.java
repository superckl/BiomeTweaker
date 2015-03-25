package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

@RequiredArgsConstructor
public class ScriptCommandRemoveBiome implements IScriptCommand{

	private final int biomeID;

	@Override
	public void perform() throws Exception {
		if(this.biomeID == -1){
			LogHelper.error("Request to remove all biomes was made! Check your scripts!");
			return;
		}
		for(final BiomeType type:BiomeType.values())
			for(final BiomeEntry entry:BiomeManager.getBiomes(type))
				if(entry.biome.biomeID == this.biomeID)
					BiomeManager.removeBiome(type, entry);
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
