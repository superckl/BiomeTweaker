package me.superckl.biometweaker.script.command;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.FlowerEntry;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveBiomeFlower implements IScriptCommand{

	private static Field field;

	private final int biomeID;
	private final boolean remove;
	private final String block;
	private final int meta;
	private final int weight;

	public ScriptCommandAddRemoveBiomeFlower(final int biomeID, final String block, final int meta) {
		this(biomeID, true, block, meta, 0);
	}

	public ScriptCommandAddRemoveBiomeFlower(final int biomeID, final String block, final int meta, final int weight) {
		this(biomeID, false, block, meta, weight);
	}

	@Override
	public void perform() throws Exception {
		if(this.remove){
			if(ScriptCommandAddRemoveBiomeFlower.field == null){
				ScriptCommandAddRemoveBiomeFlower.field = BiomeGenBase.class.getDeclaredField("flowers");
				ScriptCommandAddRemoveBiomeFlower.field.setAccessible(true);
			}
			final Block block = Block.getBlockFromName(this.block);
			if(this.biomeID == -1){
				for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
					if(gen == null)
						continue;
					final List<FlowerEntry> flowers = (List<FlowerEntry>) ScriptCommandAddRemoveBiomeFlower.field.get(gen);
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
			final List<FlowerEntry> flowers = (List<FlowerEntry>) ScriptCommandAddRemoveBiomeFlower.field.get(gen);
			final Iterator<FlowerEntry> it = flowers.iterator();
			while(it.hasNext()){
				final FlowerEntry entry = it.next();
				if((entry.block == block) && (entry.metadata == this.meta))
					it.remove();
			}
		}else{
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
		}
		Config.INSTANCE.onTweak(this.biomeID);
	}

}
