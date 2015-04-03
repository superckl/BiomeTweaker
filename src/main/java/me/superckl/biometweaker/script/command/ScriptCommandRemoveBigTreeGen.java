package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.world.gen.BlankWorldGenBigTree;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandRemoveBigTreeGen implements IScriptCommand{

	private final int biomeID;

	@Override
	public void perform() throws Exception {
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
				if(gen != null){
					gen.worldGeneratorBigTree = new BlankWorldGenBigTree(false);
					Config.INSTANCE.onTweak(gen.biomeID);
				}
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		gen.worldGeneratorBigTree = new BlankWorldGenBigTree(false);
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
