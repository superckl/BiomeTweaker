package me.superckl.biometweaker.script.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveBiome implements IScriptCommand{

	private final int biomeID;
	private final boolean remove;
	private final String type;
	private final int weight;

	public ScriptCommandAddRemoveBiome(final int biomeID) {
		this(biomeID, true, null, 0);
	}

	public ScriptCommandAddRemoveBiome(final int biomeID, final String type, final int weight) {
		this(biomeID, false, type, weight);
	}

	@Override
	public void perform() throws Exception {
		if(this.remove){
			if(this.biomeID == -1){
				LogHelper.error("Request to remove all biomes was made! Check your scripts!");
				return;
			}
			for(final BiomeType type:BiomeType.values())
				for(final BiomeEntry entry:BiomeManager.getBiomes(type))
					if(entry.biome.biomeID == this.biomeID)
						BiomeManager.removeBiome(type, entry);
		}else{
			if(BiomeGenBase.getBiomeGenArray()[this.biomeID] != null){
				ModBiomeTweakerCore.logger.error("Request to register new biome has conflicting biome ID! It will not be registered.");
				return;
			}
			final BiomeTweakerBiome biome = new BiomeTweakerBiome(this.biomeID);
			BiomeManager.addBiome(BiomeType.getType(this.type), new BiomeEntry(biome, this.weight));
		}
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
