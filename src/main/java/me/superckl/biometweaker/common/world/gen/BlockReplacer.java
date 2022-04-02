package me.superckl.biometweaker.common.world.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.biometweaker.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class BlockReplacer {


	public static void runReplacement(final PlacementStage stage, final WorldGenLevel level, final ChunkAccess chunk){
		try {
			final BlockReplacementManager manager = BlockReplacementManager.getManagerForWorld(level.getLevel().dimension().getRegistryName());
			if(!manager.hasReplacements(stage))
				return;
			final WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(RandomSupport.seedUniquifier()));
			final ChunkPos pos = chunk.getPos();
			rand.setLargeFeatureSeed(level.getSeed()+BiomeTweakerAPI.MOD_ID.hashCode()+chunk.getPos().hashCode(), pos.x, pos.z);
			final Map<ResourceLocation, BlockReplacementEntryList> previousReplacements = manager.findMap(pos);
			for (final LevelChunkSection section : chunk.getSections()) {
				if(section.hasOnlyAir())
					return;
				section.acquire();
				final Iterable<BlockPos> biome_positions = BlockPos.betweenClosed(0, 0, 0, 3, 3, 3);
				//Loop over the quarts first to obtain the biome efficiently
				for(final BlockPos biome_pos : biome_positions) {
					final int bx = biome_pos.getX(); final int by = biome_pos.getY(); final int bz = biome_pos.getZ();
					final Biome biome = section.getNoiseBiome(bx, by, bz).value();
					final ResourceLocation rLoc = biome.getRegistryName();
					if(!manager.hasReplacements(rLoc, stage))
						continue;
					final BlockReplacementEntryList previousReplacementsBiome = previousReplacements.computeIfAbsent(rLoc, loc -> new BlockReplacementEntryList());
					final BlockReplacementEntryList list = manager.findReplacementEntryList(rLoc, stage);

					//All section-local block positions in this quart
					final Iterable<BlockPos> block_positions = BlockPos.betweenClosed(QuartPos.toBlock(bx), QuartPos.toBlock(by), QuartPos.toBlock(bz),
							QuartPos.toBlock(bx+1)-1, QuartPos.toBlock(by+1)-1, QuartPos.toBlock(bz+1)-1);
					for (final BlockPos block_pos : block_positions) {
						final int x = block_pos.getX(); final int y = block_pos.getY(); final int z = block_pos.getZ();
						final BlockState state = section.getBlockState(x, y, z);
						WeightedEntry.Wrapper<ReplacementConstraints> toUse = null;
						final BlockReplacementEntry previousEntry = previousReplacementsBiome.findEntry(state);
						if(previousEntry != null) {
							toUse = previousEntry.findEntriesForState(state).get(0);
							if(!BlockReplacer.verifyBoundaries(pos, x, y+section.bottomBlockY(), z, toUse.getData()))
								toUse = null;
						}
						if(toUse == null){
							final BlockReplacementEntry entry = list.findEntry(state);
							if(entry != null){
								final List<Wrapper<ReplacementConstraints>> entries = entry.findEntriesForState(state);
								if(entries == null || entries.isEmpty())
									continue;
								toUse = WeightedRandom.getRandomItem(rand, entries).orElseThrow();
								if(!BlockReplacer.verifyBoundaries(pos, x, y+section.bottomBlockY(), z, toUse.getData())) {
									final List<WeightedEntry.Wrapper<ReplacementConstraints>> copy = new ArrayList<>(entries);
									copy.remove(toUse);
									toUse = null;
									while(!copy.isEmpty()) {
										toUse = WeightedRandom.getRandomItem(rand, copy).orElseThrow();
										if(BlockReplacer.verifyBoundaries(pos, x, y+section.bottomBlockY(), z, toUse.getData())) {
											if(toUse.getData().isContiguous())
												previousReplacementsBiome.registerReplacement(toUse.getWeight().asInt(), state, toUse.getData());
											break;
										}
										copy.remove(toUse);
										toUse = null;
									}
								}else if(toUse.getData().isContiguous())
									previousReplacementsBiome.registerReplacement(toUse.getWeight().asInt(), state, toUse.getData());
							}
						}
						if(toUse != null)
							section.setBlockState(x, y, z, toUse.getData().getState(), false);
					}
				}
				section.release();
			}
			final Iterator<ResourceLocation> it = previousReplacements.keySet().iterator();
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
		return y > constraints.getMinY() && y < constraints.getMaxY() && x > constraints.getMinChunkX() && x < constraints.getMaxChunkX()
				&& z > constraints.getMinChunkZ() && z < constraints.getMaxChunkZ() && pos.getBlockX(x) > constraints.getMinX() && pos.getBlockX(x) < constraints.getMaxX()
				&& pos.getBlockZ(z) > constraints.getMinZ() && pos.getBlockZ(z) < constraints.getMaxZ();
	}
}
