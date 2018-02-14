package me.superckl.biometweaker.common.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.WeightedBlockEntry;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
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
			final MutableBlockPos biomeCheckPos = new MutableBlockPos();
			final MutableBlockPos blockSetPos = new MutableBlockPos();
			for (int x = 0; x < 16; ++x)
				for (int z = 0; z < 16; ++z)
				{
					final Biome biomegenbase = world.getBiome(biomeCheckPos.setPos((pos.x << 4)+x, 0, (pos.z << 4)+z));//e.biomeArray[l + (k * 16)];
					final int id = Biome.getIdForBiome(biomegenbase);
					if(!manager.hasReplacements(id, stage))
						continue;
					if(!previousReplacements.containsKey(id))
						previousReplacements.put(id, new BlockReplacementEntryList());
					final BlockReplacementEntryList previousReplacementsBiome = previousReplacements.get(id);
					final BlockReplacementEntryList list = manager.findReplacementEntryList(id, stage);
					final Set<IBlockState> noReps = Sets.newIdentityHashSet();
					//assuming height of 256 since blockArray no longer exposed, results in ((16*16)*256)/256=256
					final int k1 = 256;
					for(int y = 0; y < k1; y++){
						final IBlockState state = primer == null ? chunk.getBlockState(x, y, z):primer.getBlockState(x, y, z);
						if(noReps.contains(state))
							continue;
						final Block block = state.getBlock();
						WeightedBlockEntry toUse = null;
						final BlockReplacementEntry previousEntry = previousReplacementsBiome.findEntry(state);
						final int meta = block.getMetaFromState(state);
						if(previousEntry != null) {
							toUse = previousEntry.findEntriesForMeta(meta).get(0);
							if(!BlockReplacer.verifyBoundaries(pos, x, y, z, toUse.getConstraints()))
								toUse = null;
						}
						if(toUse == null){
							final BlockReplacementEntry entry = list.findEntry(state);
							if(entry != null){
								final List<WeightedBlockEntry> entries = entry.findEntriesForMeta(meta);
								if(entries == null || entries.isEmpty())
									continue;
								toUse = WeightedRandom.getRandomItem(rand, entries);
								if(!BlockReplacer.verifyBoundaries(pos, x, y, z, toUse.getConstraints())) {
									final List<WeightedBlockEntry> copy = new ArrayList<>(entries);
									copy.remove(toUse);
									boolean isWholeChunk = BlockReplacer.isWholeChunk(pos, toUse.getConstraints(), world.getHeight());
									toUse = null;
									while(!copy.isEmpty()) {
										toUse = WeightedRandom.getRandomItem(rand, copy);
										if(BlockReplacer.verifyBoundaries(pos, x, y, z, toUse.getConstraints())) {
											previousReplacementsBiome.registerReplacement(toUse.itemWeight, state, toUse.getConstraints());
											break;
										}
										if(!BlockReplacer.isWholeChunk(pos, toUse.getConstraints(), world.getHeight()))
											isWholeChunk = false;
										toUse = null;
									}
									if(toUse == null && isWholeChunk)
										noReps.add(state);
								}
							}
						}
						if(toUse != null)
							if(primer != null)
								primer.setBlockState(x, y, z, toUse.getConstraints().getState());
							else
								chunk.setBlockState(blockSetPos.setPos(x, y, z), toUse.getConstraints().getState());
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

	private static boolean verifyBoundaries(final ChunkPos pos, final int x, final int y, final int z, final ReplacementConstraints constraints) {
		return !(y < constraints.getMinY() || y > constraints.getMaxY() ||
				x < constraints.getMinChunkX() || x > constraints.getMaxChunkX() ||
				z < constraints.getMinChunkZ() || z > constraints.getMaxChunkZ() ||
				(pos.x << 4)+x < constraints.getMinX() || (pos.x << 4)+x > constraints.getMaxX() ||
				(pos.z << 4)+z < constraints.getMinZ() || (pos.z << 4)+z > constraints.getMaxZ());
	}

	private static boolean isWholeChunk(final ChunkPos pos, final ReplacementConstraints constraints, final int worldHeight) {
		return !(constraints.getMinY() > 0 || constraints.getMaxY() < worldHeight ||
				constraints.getMinChunkX() > 0 || constraints.getMaxChunkX() < 15 ||
				constraints.getMinChunkZ() > 0 || constraints.getMaxChunkZ() < 15 ||
				constraints.getMinX() > (pos.x << 4) || constraints.getMaxX() < ((pos.x << 4)+16) ||
				constraints.getMinZ() > (pos.z << 4) || constraints.getMaxZ() < ((pos.z << 4)+16));
	}
}
