package me.superckl.biometweaker.common.world.gen;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import me.superckl.biometweaker.util.NumberHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.ChunkPos;

public class BlockReplacementManager {

	private static final TIntObjectMap<BlockReplacementManager> managers = new TIntObjectHashMap<>();

	@Getter
	private static PlacementStage defaultStage = PlacementStage.BIOME_BLOCKS;

	private static final boolean[] globalContigReplaces = new boolean[256];

	private static final Map<PlacementStage, TIntObjectMap<BlockReplacementEntryList>> globalBlockReplacements = new EnumMap<>(PlacementStage.class);

	private final boolean[] contigReplaces = new boolean[256];

	private final Map<PlacementStage, TIntObjectMap<BlockReplacementEntryList>> blockReplacements = new EnumMap<>(PlacementStage.class);

	private final Map<ChunkPos, TIntObjectMap<BlockReplacementEntryList>> replacedBiomes = Maps.newHashMap();

	@Getter
	@Setter
	private static PlacementStage currentStage = BlockReplacementManager.defaultStage;


	public static BlockReplacementManager getManagerForWorld(final int worldId){
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

	public boolean hasReplacements(final PlacementStage stage){
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

	public boolean hasReplacements(final int biome, final PlacementStage stage){
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

	public void registerBlockReplacement(final int biome, final int weight, final IBlockState toReplace, final IBlockState replacement){
		if(!this.blockReplacements.containsKey(BlockReplacementManager.currentStage))
			this.blockReplacements.put(BlockReplacementManager.currentStage, new TIntObjectHashMap<BlockReplacementEntryList>());
		if(!this.blockReplacements.get(BlockReplacementManager.currentStage).containsKey(biome))
			this.blockReplacements.get(BlockReplacementManager.currentStage).put(biome, new BlockReplacementEntryList());
		this.blockReplacements.get(BlockReplacementManager.currentStage).get(biome).registerReplacement(weight, toReplace, replacement);
	}

	public static void registerGlobalBlockReplacement(final int biome, final int weight,  final IBlockState toReplace, final IBlockState replacement){
		if(!BlockReplacementManager.globalBlockReplacements.containsKey(BlockReplacementManager.currentStage))
			BlockReplacementManager.globalBlockReplacements.put(BlockReplacementManager.currentStage, new TIntObjectHashMap<BlockReplacementEntryList>());
		if(!BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).containsKey(biome))
			BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).put(biome, new BlockReplacementEntryList());
		BlockReplacementManager.globalBlockReplacements.get(BlockReplacementManager.currentStage).get(biome).registerReplacement(weight, toReplace, replacement);
	}

	@Nullable
	public BlockReplacementEntryList findReplacementEntryList(final int biome, final PlacementStage stage){
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

		private final IBlockState blockState;

		public WeightedBlockEntry(final int weight, final IBlockState blockState) {
			super(weight);
			this.blockState = blockState;
		}

	}

}
