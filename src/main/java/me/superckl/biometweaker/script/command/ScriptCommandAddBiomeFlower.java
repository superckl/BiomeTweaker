package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandAddBiomeFlower implements IScriptCommand{

	private final int biomeID;
	private final String block;
	private final int meta;
	private final int weight;

	@Override
	public void perform() throws Exception {
		final Block block = Block.getBlockFromName(this.block);
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
				if(gen == null)
					continue;
				gen.addFlower(block, this.meta, this.weight);
			}
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		gen.addFlower(block, this.meta, this.weight);
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
