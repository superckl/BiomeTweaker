package me.superckl.biometweaker.common.handler;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GenHandler {

	private Field actualFillerBlock;

	//TODO remove throws
	@SubscribeEvent
	public void onReplaceBlocks(final ReplaceBiomeBlocks e) throws Exception{
		if(this.actualFillerBlock == null)
			this.actualFillerBlock = BiomeGenBase.class.getDeclaredField("actualFillerBlock");
		for (int k = 0; k < 16; ++k)
			for (int l = 0; l < 16; ++l)
			{
				final BiomeGenBase biomegenbase = e.biomeArray[l + (k * 16)];
				final Block actual = (Block) this.actualFillerBlock.get(biomegenbase);
				if(actual == Blocks.stone)
					continue;
				final int i1 = k;
				final int j1 = l;
				final int k1 = e.blockArray.length / 256;
				for (int l1 = 255; l1 >= 0; --l1)
				{
					final int i2 = (((j1 * 16) + i1) * k1) + l1;
					final Block block2 = e.blockArray[i2];
					if(block2 == Blocks.stone)
						e.blockArray[i2] = actual;
				}
			}
	}

}
