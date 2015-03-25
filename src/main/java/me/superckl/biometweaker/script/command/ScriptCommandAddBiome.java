package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

@RequiredArgsConstructor
public class ScriptCommandAddBiome implements IScriptCommand{

	private final int biomeID;
	private final String type;
	private final int weight;

	@Override
	public void perform() throws Exception {
		if(BiomeGenBase.getBiomeGenArray()[this.biomeID] != null){
			ModBiomeTweakerCore.logger.error("Request to register new biome has conflicting biome ID! It will not be registered.");
			return;
		}
		final BiomeTweakerBiome biome = new BiomeTweakerBiome(this.biomeID);
		BiomeManager.addBiome(BiomeType.getType(this.type), new BiomeEntry(biome, this.weight));
	}

}
