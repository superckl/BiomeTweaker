package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.gson.JsonElement;

@RequiredArgsConstructor
public class ScriptCommandSetBiomeProperty implements IScriptCommand{

	private final int biomeID;
	private final String key;
	private final JsonElement value;
	@Override
	public void perform() throws Exception {
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
				if(gen == null)
					continue;
				BiomeHelper.setBiomeProperty(this.key, this.value, gen);
			}
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		BiomeHelper.setBiomeProperty(this.key, this.value, gen);
	}

	@Override
	public String toString(){
		return "Property set ("+this.key+", "+this.value+") for biome ID "+this.biomeID;
	}

}
