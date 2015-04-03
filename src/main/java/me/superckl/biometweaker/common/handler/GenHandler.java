package me.superckl.biometweaker.common.handler;

import java.lang.reflect.Field;

import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GenHandler {

	private Field actualFillerBlock;
	private Field liquidFillerBlock;

	@SubscribeEvent
	public void onReplaceBlocks(final ReplaceBiomeBlocks e){
		try {
			if(this.actualFillerBlock == null)
				this.actualFillerBlock = BiomeGenBase.class.getDeclaredField("actualFillerBlock");
			if(this.liquidFillerBlock == null)
				this.liquidFillerBlock = BiomeGenBase.class.getDeclaredField("liquidFillerBlock");
			for (int k = 0; k < 16; ++k)
				for (int l = 0; l < 16; ++l)
				{
					final BiomeGenBase biomegenbase = e.biomeArray[l + (k * 16)];
					final Block actualS = (Block) this.actualFillerBlock.get(biomegenbase);
					final Block actualL = (Block) this.liquidFillerBlock.get(biomegenbase);
					if((actualS == Blocks.stone) && (actualL == Blocks.water))
						continue;
					final int i1 = k;
					final int j1 = l;
					final int k1 = e.blockArray.length / 256;
					for (int l1 = 255; l1 >= 0; --l1)
					{
						final int i2 = (((j1 * 16) + i1) * k1) + l1;
						final Block block2 = e.blockArray[i2];
						if(block2 == Blocks.stone)
							e.blockArray[i2] = actualS;
						else if(block2 == Blocks.water)
							e.blockArray[i2] = actualL;
					}
				}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process replace biome blocks event.");
			e1.printStackTrace();
		}
	}

}
