package me.superckl.biometweaker.common.world.gen.feature;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorWrapper;
import me.superckl.biometweaker.common.world.gen.PlacementStage;

public class DecorationManager {

	private static final TIntObjectMap<DecorationManager> managers = new TIntObjectHashMap<>();

	@Getter
	private static PlacementStage defaultStage = PlacementStage.POST_DECORATE;

	private static final Map<PlacementStage, TIntObjectMap<List<WorldGeneratorWrapper<?>>>> globalGenerators = new EnumMap<>(PlacementStage.class);

	private final Map<PlacementStage, TIntObjectMap<List<WorldGeneratorWrapper<?>>>> generators = new EnumMap<>(PlacementStage.class);

	@Getter
	@Setter
	private static PlacementStage currentStage = DecorationManager.defaultStage;

	public static DecorationManager getManagerForWorld(final int worldId){
		DecorationManager manager = DecorationManager.managers.get(worldId);
		if(manager == null){
			manager = new DecorationManager();
			DecorationManager.managers.put(worldId, manager);
		}
		return manager;
	}

	public static boolean hasGlobalReplacements(){
		return !DecorationManager.globalGenerators.isEmpty();
	}

	public boolean hasReplacements(){
		return DecorationManager.hasGlobalReplacements() || !this.generators.isEmpty();
	}

	public boolean hasReplacements(final PlacementStage stage){
		if(!this.hasReplacements())
			return false;
		final TIntObjectMap<List<WorldGeneratorWrapper<?>>> entries = DecorationManager.globalGenerators.get(stage);
		if(entries == null || entries.isEmpty()){
			final TIntObjectMap<List<WorldGeneratorWrapper<?>>> localEntries = this.generators.get(stage);
			return localEntries != null && !localEntries.isEmpty();
		}else if(entries != null && !entries.isEmpty())
			return true;
		return false;
	}

	public boolean hasReplacements(final int biome, final PlacementStage stage){
		if(!this.hasReplacements())
			return false;
		final TIntObjectMap<List<WorldGeneratorWrapper<?>>> entries = DecorationManager.globalGenerators.get(stage);
		if(entries == null || !entries.containsKey(biome)){
			final TIntObjectMap<List<WorldGeneratorWrapper<?>>> localEntries = this.generators.get(stage);
			return localEntries != null && localEntries.containsKey(biome);
		}else if(entries != null && entries.containsKey(biome))
			return true;
		return false;
	}

	public void registerDecoration(final int biome, final WorldGeneratorWrapper<?> generator){
		if(!this.generators.containsKey(DecorationManager.currentStage))
			this.generators.put(DecorationManager.currentStage, new TIntObjectHashMap<List<WorldGeneratorWrapper<?>>>());
		if(!this.generators.get(DecorationManager.currentStage).containsKey(biome))
			this.generators.get(DecorationManager.currentStage).put(biome, Lists.newArrayList());
		this.generators.get(DecorationManager.currentStage).get(biome).add(generator);
	}

	public static void registerGlobalDecoration(final int biome, final WorldGeneratorWrapper<?> gen){
		if(!DecorationManager.globalGenerators.containsKey(DecorationManager.currentStage))
			DecorationManager.globalGenerators.put(DecorationManager.currentStage, new TIntObjectHashMap<List<WorldGeneratorWrapper<?>>>());
		if(!DecorationManager.globalGenerators.get(DecorationManager.currentStage).containsKey(biome))
			DecorationManager.globalGenerators.get(DecorationManager.currentStage).put(biome, Lists.newArrayList());
		DecorationManager.globalGenerators.get(DecorationManager.currentStage).get(biome).add(gen);
	}

	@Nullable
	public List<WorldGeneratorWrapper<?>> findDecorationList(final int biome, final PlacementStage stage){
		TIntObjectMap<List<WorldGeneratorWrapper<?>>> entryMap = this.generators.get(stage);
		if(entryMap != null)
			return entryMap.get(biome);
		else{
			entryMap = DecorationManager.globalGenerators.get(stage);
			if(entryMap != null)
				return entryMap.get(biome);
		}
		return null;
	}

}
