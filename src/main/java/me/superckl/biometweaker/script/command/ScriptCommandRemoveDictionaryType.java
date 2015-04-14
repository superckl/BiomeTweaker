package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

@RequiredArgsConstructor
public class ScriptCommandRemoveDictionaryType implements IScriptCommand{

	private final int biomeID;
	private final String type;

	@Override
	public void perform() throws Exception {
		final BiomeDictionary.Type bType = Type.getType(this.type);
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
				BiomeHelper.modifyBiomeDicType(gen, bType, true);
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		BiomeHelper.modifyBiomeDicType(gen, bType, true);
	}

}
