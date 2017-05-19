package me.superckl.biometweaker.common.world.gen;

import java.util.List;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.ReplacementStage;
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

	public static void runReplacement(final ReplacementStage stage, final World world, final ChunkPos pos, final ChunkPrimer primer){
		try {
			final BlockReplacementManager manager = BlockReplacementManager.getManagerForWorld(world.provider.getDimension());
			if(!manager.hasReplacements(stage))
				return;
			final TIntObjectMap<BlockReplacementEntryList> previousReplacements = manager.findMap(pos);
			final Chunk chunk = primer == null ? world.getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos):null;
			for (int x = 0; x < 16; ++x)
				for (int z = 0; z < 16; ++z)
				{
					//TODO verify adding x and z works
					final Biome biomegenbase = world.getBiome(new BlockPos(pos.chunkXPos << 4 + x, 0, pos.chunkZPos << 4 + z));//e.biomeArray[l + (k * 16)];
					final int id = Biome.getIdForBiome(biomegenbase);
					if(!manager.hasReplacements(id, stage))
						continue;
					if(!previousReplacements.containsKey(id))
						previousReplacements.put(id, new BlockReplacementEntryList());
					final BlockReplacementEntryList previousReplacementsBiome = previousReplacements.get(id);
					final BlockReplacementEntryList list = manager.findReplacementEntryList(id, stage);
					//TODO assuming height of 256 since blockArray no longer exposed, results in ((16*16)*256)/256=256
					final int k1 = 256;
					for(int y = 0; y < k1; y++){
						final IBlockState state = primer == null ? chunk.getBlockState(x, y, z):primer.getBlockState(x, y, z);
						final Block block = state.getBlock();
						WeightedBlockEntry toUse = null;
						final BlockReplacementEntry previousEntry = previousReplacementsBiome.findEntry(block, block.getMetaFromState(state));
						if(previousEntry != null)
							toUse = previousEntry.findEntriesForMeta(previousEntry.checkWildcardMeta(block.getMetaFromState(state))).get(0);
						int meta = block.getMetaFromState(state);
						if(toUse == null){
							final BlockReplacementEntry entry = list.findEntry(block, meta);
							if(entry != null){
								meta = entry.checkWildcardMeta(meta);
								final List<WeightedBlockEntry> entries = entry.findEntriesForMeta(meta);
								if(entries == null || entries.isEmpty())
									continue;
								toUse = WeightedRandom.getRandomItem(world.rand, entries);
								previousReplacementsBiome.registerReplacement(toUse.itemWeight, block, meta, toUse.getBlock().getKey(), toUse.getBlock().getValue().intValue());
							}
						}
						if(toUse != null){
							final Block block2 = toUse.getBlock().getKey();
							meta = toUse.getBlock().getValue();
							if(primer != null)
								primer.setBlockState(x, y, z, meta == -1 ? block2.getDefaultState():block2.getStateFromMeta(meta));
							else
								chunk.setBlockState(new BlockPos(x, y, z), meta == -1 ? block2.getDefaultState():block2.getStateFromMeta(meta));
						}
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
