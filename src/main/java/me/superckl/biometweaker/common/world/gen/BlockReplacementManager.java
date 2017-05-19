package me.superckl.biometweaker.common.world.gen;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import me.superckl.biometweaker.util.NumberHelper;
import net.minecraft.block.Block;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.DimensionManager;

public class BlockReplacementManager {

	private static TIntObjectMap<BlockReplacementManager> managers = new TIntObjectHashMap<>();

	public static enum ReplacementStage{
		BIOME_BLOCKS, POPULATE, DECORATE;

	}

	@Getter
	private static ReplacementStage defaultStage = ReplacementStage.BIOME_BLOCKS;

	private static final boolean[] globalContigReplaces = new boolean[256];

	private static final Map<ReplacementStage, TIntObjectMap<BlockReplacementEntryList>> globalBlockReplacements = new EnumMap<>(ReplacementStage.class);

	private final boolean[] contigReplaces = new boolean[256];

	private final Map<ReplacementStage, TIntObjectMap<BlockReplacementEntryList>> blockReplacements = new EnumMap<>(ReplacementStage.class);

	private final Map<ChunkPos, TIntObjectMap<BlockReplacementEntryList>> replacedBiomes = Maps.newHashMap();

	@Getter
	@Setter
	private static ReplacementStage currentStage = BlockReplacementManager.defaultStage;


	public static BlockReplacementManager getManagerForWorld(final int worldId){
		DimensionManager.getWorld(0);
		BlockReplacementManager manager = BlockReplacementManager.managers.get(worldId);
		if(manager == null){
			manager = new BlockReplacementManager();
			BlockReplacementManager.managers.put(worldId, manager);
		}
		return manager;
	}

	public static boolean hasGlobalReplacements(){
		return !BlockReplacementManager.globalBlockReplacements.isEmpty();
	}

	public boolean hasReplacements(){
		return BlockReplacementManager.hasGlobalReplacements() || !this.blockReplacements.isEmpty();
	}

	public boolean hasReplacements(final ReplacementStage stage){
		if(!this.hasReplacements())
			return false;
		final TIntObjectMap<BlockReplacementEntryList> entries = BlockReplacementManager.globalBlockReplacements.get(stage);
		if(entries == null || entries.isEmpty()){
			final TIntObjectMap<BlockReplacementEntryList> localEntries = this.blockReplacements.get(stage);
			return localEntries != null && !localEntries.isEmpty();
		}else if(entries != null && !entries.isEmpty())
			return true;
		return false;
	}

	public boolean hasReplacements(final int biome, final ReplacementStage stage){
		if(!this.hasReplacements())
			return false;
		final TIntObjectMap<BlockReplacementEntryList> entries = BlockReplacementManager.globalBlockReplacements.get(stage);
		if(entries == null || !entries.containsKey(biome)){
			final TIntObjectMap<BlockReplacementEntryList> localEntries = this.blockReplacements.get(stage);
			return localEntries != null && localEntries.containsKey(biome);
		}else if(entries != null && entries.containsKey(biome))
			return true;
		return false;
	}

	public void registerBlockReplacement(final int biome, final int weight, final Block toReplace, final int toReplaceMeta, final Block replacement, final int replacementMeta){
		if(!this.blockReplacements.containsKey(BlockReplacementManager.currentStage))
			this.blockReplacements.put(BlockReplacementManager.currentStage, new TIntObjectHashMap<BlockReplacementEntryList>());
		if(!this.blockReplacements.get(BlockReplacementManager.currentStage).containsKey(biome))
			this.blockReplacements.get(BlockReplacementManager.currentStage).put(biome, new BlockReplacementEntryList());
		this.blockReplacements.get(BlockReplacementManager.currentStage).get(biome).registerReplacement(weight, toReplace, toReplaceMeta, replacement, replacementMeta);
	}

	public static void registerGlobalBlockReplacement(final int biome, final int weight, final Block toReplace, final int toReplaceMeta, final Block replacement, final int replacementMeta){
		if(!BlockReplacementManager.globalBlockReplacements.containsKey(BlockReplacementManager.currentStage))
			BlockReplacementManager.globalBlockReplacements.put(BlockReplacementManager.currentStage, new TIntObjectHashMap<BlockReplacementEntryList>());
		if(!BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).containsKey(biome))
			BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).put(biome, new BlockReplacementEntryList());
		BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).get(biome).registerReplacement(weight, toReplace, toReplaceMeta, replacement, replacementMeta);
	}

	@Nullable
	public BlockReplacementEntryList findReplacementEntryList(final int biome, final ReplacementStage stage){
		TIntObjectMap<BlockReplacementEntryList> entryMap = this.blockReplacements.get(stage);
		if(entryMap != null)
			return entryMap.get(biome);
		else{
			entryMap = BlockReplacementManager.globalBlockReplacements.get(stage);
			if(entryMap != null)
				return entryMap.get(biome);
		}
		return null;
	}

	public TIntObjectMap<BlockReplacementEntryList> findMap(final ChunkPos pair){
		final ChunkPos[] pairs = NumberHelper.fillGrid(4, pair);

		for(final ChunkPos search:pairs)
			if(this.replacedBiomes.containsKey(search))
				return this.replacedBiomes.get(search);

		return new TIntObjectHashMap<>();
	}

	public void trackReplacement(final ChunkPos pos, final TIntObjectMap<BlockReplacementEntryList> map){
		this.replacedBiomes.put(pos, map);
	}

	public boolean isContiguousReplacement(final int biome){
		return this.contigReplaces[biome] || BlockReplacementManager.globalContigReplaces[biome];
	}

	@Getter
	public static class WeightedBlockEntry extends WeightedRandom.Item{

		private final Pair<Block, Integer> block;

		public WeightedBlockEntry(final int weight, final Pair<Block, Integer> block) {
			super(weight);
			this.block = block;
		}

	}

}
