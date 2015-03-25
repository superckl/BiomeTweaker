package me.superckl.biometweaker.script.command;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.FlowerEntry;

@RequiredArgsConstructor
public class ScriptCommandRemoveBiomeFlower implements IScriptCommand{

	private static Field field;

	private final int biomeID;
	private final String block;
	private final int meta;

	@Override
	public void perform() throws Exception {
		if(ScriptCommandRemoveBiomeFlower.field == null){
			ScriptCommandRemoveBiomeFlower.field = BiomeGenBase.class.getDeclaredField("flowers");
			ScriptCommandRemoveBiomeFlower.field.setAccessible(true);
		}
		final Block block = Block.getBlockFromName(this.block);
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
				if(gen == null)
					continue;
				final List<FlowerEntry> flowers = (List<FlowerEntry>) ScriptCommandRemoveBiomeFlower.field.get(gen);
				final Iterator<FlowerEntry> it = flowers.iterator();
				while(it.hasNext()){
					final FlowerEntry entry = it.next();
					if((entry.block == block) && (entry.metadata == this.meta))
						it.remove();
				}
			}
			Config.INSTANCE.onTweak(-1);
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		final List<FlowerEntry> flowers = (List<FlowerEntry>) ScriptCommandRemoveBiomeFlower.field.get(gen);
		final Iterator<FlowerEntry> it = flowers.iterator();
		while(it.hasNext()){
			final FlowerEntry entry = it.next();
			if((entry.block == block) && (entry.metadata == this.meta))
				it.remove();
		}
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
