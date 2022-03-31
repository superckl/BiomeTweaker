package me.superckl.biometweaker.common.world.gen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockReplacementEntry {

	private final Block block;
	private final Object2ObjectMap<BlockState, List<WeightedEntry.Wrapper<ReplacementConstraints>>> replacements = new Object2ObjectOpenHashMap<>();
	private final ObjectSet<WeightedEntry.Wrapper<ReplacementConstraints>> wildcardReplacements = new ObjectOpenHashSet<>();

	public BlockReplacementEntry(final Block toReplace){
		this.block = toReplace;
	}

	@Nullable
	public List<WeightedEntry.Wrapper<ReplacementConstraints>> findEntriesForState(final BlockState state){
		if(!this.wildcardReplacements.isEmpty()) {
			final List<WeightedEntry.Wrapper<ReplacementConstraints>> entries = new ArrayList<>(this.wildcardReplacements);
			if(this.replacements.containsKey(state))
				entries.addAll(this.replacements.get(state));
			return entries;
		}
		return this.replacements.get(state);
	}

	public void registerReplacement(final int weight, final BlockState toReplace, final ReplacementConstraints replacement){
		if(replacement.isIgnoreMeta()) {
			this.wildcardReplacements.add(WeightedEntry.wrap(replacement, weight));
			return;
		}
		this.replacements.computeIfAbsent(toReplace, obj -> new ArrayList<>()).add(WeightedEntry.wrap(replacement, weight));
	}

	public boolean matches(final BlockState toReplace){
		return this.block == toReplace.getBlock() && (!this.wildcardReplacements.isEmpty() || this.replacements.containsKey(toReplace));
	}

	public boolean matches(final Block toReplace){
		return this.block == toReplace;
	}

}
