package me.superckl.biometweaker.common.world.gen;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import lombok.Setter;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockReplacementManager {

	private static final Object2ObjectMap<ResourceLocation, BlockReplacementManager> managers = new Object2ObjectOpenHashMap<>();

	@Getter
	private static PlacementStage defaultStage = PlacementStage.PRE_SURFACE;

	private static final Set<ResourceLocation> globalContigReplaces = new ObjectOpenHashSet<>();

	private static final Map<PlacementStage, Map<ResourceLocation, BlockReplacementEntryList>> globalBlockReplacements = new EnumMap<>(PlacementStage.class);

	private final Set<ResourceLocation> contigReplaces = new ObjectOpenHashSet<>();

	private final Map<PlacementStage, Map<ResourceLocation, BlockReplacementEntryList>> blockReplacements = new EnumMap<>(PlacementStage.class);

	private final Map<ChunkPos, Map<ResourceLocation, BlockReplacementEntryList>> replacedBiomes = new Object2ObjectOpenHashMap<>();

	@Getter
	@Setter
	private static PlacementStage currentStage = BlockReplacementManager.defaultStage;


	public static BlockReplacementManager getManagerForWorld(final ResourceLocation worldId){
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
		final Map<ResourceLocation, BlockReplacementEntryList> entries = BlockReplacementManager.globalBlockReplacements.get(stage);
		if(entries == null || entries.isEmpty()){
			final Map<ResourceLocation, BlockReplacementEntryList> localEntries = this.blockReplacements.get(stage);
			return localEntries != null && !localEntries.isEmpty();
		}
		if(entries != null && !entries.isEmpty())
			return true;
		return false;
	}

	public boolean hasReplacements(final ResourceLocation biome, final PlacementStage stage){
		if(!this.hasReplacements())
			return false;
		final Map<ResourceLocation, BlockReplacementEntryList> entries = BlockReplacementManager.globalBlockReplacements.get(stage);
		if(entries == null || !entries.containsKey(biome)){
			final Map<ResourceLocation, BlockReplacementEntryList> localEntries = this.blockReplacements.get(stage);
			return localEntries != null && localEntries.containsKey(biome);
		}
		if(entries != null && entries.containsKey(biome))
			return true;
		return false;
	}

	public void registerBlockReplacement(final ResourceLocation biome, final int weight, final BlockState toReplace, final ReplacementConstraints replacement){
		this.blockReplacements.computeIfAbsent(BlockReplacementManager.currentStage, stage -> new Object2ObjectOpenHashMap<>())
		.computeIfAbsent(biome, id -> new BlockReplacementEntryList()).registerReplacement(weight, toReplace, replacement);
	}

	public static void registerGlobalBlockReplacement(final ResourceLocation biome, final int weight,  final BlockState toReplace, final ReplacementConstraints replacement){
		BlockReplacementManager.globalBlockReplacements.computeIfAbsent(BlockReplacementManager.currentStage, stage -> new Object2ObjectOpenHashMap<>())
		.computeIfAbsent(biome, id -> new BlockReplacementEntryList()).registerReplacement(weight, toReplace, replacement);
	}

	@Nullable
	public BlockReplacementEntryList findReplacementEntryList(final ResourceLocation biome, final PlacementStage stage){
		Map<ResourceLocation, BlockReplacementEntryList> entryMap = this.blockReplacements.get(stage);
		if(entryMap != null)
			return entryMap.get(biome);
		entryMap = BlockReplacementManager.globalBlockReplacements.get(stage);
		if(entryMap != null)
			return entryMap.get(biome);
		return null;
	}

	public Map<ResourceLocation, BlockReplacementEntryList> findMap(final ChunkPos pair){
		final Stream<ChunkPos> pairs = ChunkPos.rangeClosed(pair, 5);
		return pairs.filter(this.replacedBiomes::containsKey).findAny().map(this.replacedBiomes::get).orElseGet(Object2ObjectOpenHashMap::new);
	}

	public void trackReplacement(final ChunkPos pos, final Map<ResourceLocation, BlockReplacementEntryList> map){
		this.replacedBiomes.put(pos, map);
	}

	public boolean isContiguousReplacement(final ResourceLocation biome){
		return this.contigReplaces.contains(biome) || BlockReplacementManager.globalContigReplaces.contains(biome);
	}

	public static boolean isGlobalContiguousReplacement(final ResourceLocation biome) {
		return BlockReplacementManager.globalContigReplaces.contains(biome);
	}

	public void setContiguousReplacement(final ResourceLocation biome, final boolean contigReplace) {
		if (contigReplace)
			this.contigReplaces.add(biome);
		else
			this.contigReplaces.remove(biome);
	}

	public static void setGlobalContiguousReplacement(final ResourceLocation biome, final boolean contigReplace) {
		if (contigReplace)
			BlockReplacementManager.globalContigReplaces.add(biome);
		else
			BlockReplacementManager.globalContigReplaces.remove(biome);
	}

}
