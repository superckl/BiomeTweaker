package me.superckl.biometweaker.common.world.gen;

import java.util.List;
import java.util.Random;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.WeightedBlockEntry;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class BlockReplacer {

	public static void runReplacement(final PlacementStage stage, final World world, final Random rand, final ChunkPos pos, final ChunkPrimer primer){
		try {
			final BlockReplacementManager manager = BlockReplacementManager.getManagerForWorld(world.provider.getDimension());
			if(!manager.hasReplacements(stage))
				return;
			final TIntObjectMap<BlockReplacementEntryList> previousReplacements = manager.findMap(pos);
			final Chunk chunk = primer == null ? world.getChunkFromChunkCoords(pos.x, pos.z):null;
			for (int x = 0; x < 16; ++x)
				for (int z = 0; z < 16; ++z)
				{
					final Biome biomegenbase = world.getBiome(new BlockPos((pos.x << 4)+x, 0, (pos.z << 4)+z));//e.biomeArray[l + (k * 16)];
					final int id = Biome.getIdForBiome(biomegenbase);
					if(!manager.hasReplacements(id, stage))
						continue;
					if(!previousReplacements.containsKey(id))
						previousReplacements.put(id, new BlockReplacementEntryList());
					final BlockReplacementEntryList previousReplacementsBiome = previousReplacements.get(id);
					final BlockReplacementEntryList list = manager.findReplacementEntryList(id, stage);
					//assuming height of 256 since blockArray no longer exposed, results in ((16*16)*256)/256=256
					final int k1 = 256;
					for(int y = 0; y < k1; y++){
						final IBlockState state = primer == null ? chunk.getBlockState(x, y, z):primer.getBlockState(x, y, z);
						final Block block = state.getBlock();
						WeightedBlockEntry toUse = null;
						final BlockReplacementEntry previousEntry = previousReplacementsBiome.findEntry(state);
						final int meta = block.getMetaFromState(state);
						if(previousEntry != null)
							toUse = previousEntry.findEntriesForMeta(meta).get(0);
						if(toUse == null){
							final BlockReplacementEntry entry = list.findEntry(state);
							if(entry != null){
								final List<WeightedBlockEntry> entries = entry.findEntriesForMeta(meta);
								if(entries == null || entries.isEmpty())
									continue;
								toUse = WeightedRandom.getRandomItem(rand, entries);
								previousReplacementsBiome.registerReplacement(toUse.itemWeight, state, toUse.getBlock());
							}
						}
						if(toUse != null)
							if(primer != null)
								primer.setBlockState(x, y, z, toUse.getBlock());
							else
								chunk.setBlockState(new BlockPos(x, y, z), toUse.getBlock());
					}
				}
			final TIntIterator it = previousReplacements.keySet().iterator();
			while(it.hasNext())
				if(!manager.isContiguousReplacement(it.next()))
					it.remove();
			manager.trackReplacement(pos, previousReplacements);
		} catch (final Exception e1) {
			LogHelper.error("Failed to process replace biome blocks event.");
			e1.printStackTrace();
		}
	}
}
