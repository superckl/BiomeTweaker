package me.superckl.biometweaker.common.world.gen;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.WeightedBlockEntry;
import net.minecraft.block.Block;

public class BlockReplacementEntry {

	private final Block block;
	private final TIntObjectMap<List<WeightedBlockEntry>> replacements;

	public BlockReplacementEntry(final Block toReplace, final int toReplaceMeta){
		this.block = toReplace;
		this.replacements = new TIntObjectHashMap<>();
		this.replacements.put(toReplaceMeta, Lists.newArrayList());
	}

	public BlockReplacementEntry(final int weight, final Block toReplace, final Block replacement) {
		this(weight, toReplace, -1, replacement, 0);
	}

	public BlockReplacementEntry(final int weight, final Block toReplace, final int toReplaceMeta, final Block replacement) {
		this(weight, toReplace, -1, replacement, 0);
	}

	public BlockReplacementEntry(final int weight, final Block toReplace, final int toReplaceMeta, final Block replacement, final int replaceMeta) {
		this.block = toReplace;
		this.replacements = new TIntObjectHashMap<>();
		this.replacements.put(toReplaceMeta, Lists.newArrayList(new WeightedBlockEntry(weight, Pair.of(replacement, Integer.valueOf(replaceMeta)))));
	}

	public int checkWildcardMeta(final int meta){
		if(this.replacements.containsKey(meta))
			return meta;
		else
			return -1;
	}

	@Nullable
	public List<WeightedBlockEntry> findEntriesForMeta(final int meta){
		if(this.replacements.containsKey(meta))
			return this.replacements.get(meta);
		else
			return this.replacements.get(-1);
	}

	public void registerReplacement(final int weight, final int toReplaceMeta, final Block replacement, final int replacementMeta){
		if(!this.replacements.containsKey(toReplaceMeta)){
			this.replacements.put(toReplaceMeta, Lists.newArrayList(new WeightedBlockEntry(weight, Pair.of(replacement, replacementMeta))));
			return;
		}
		this.replacements.get(toReplaceMeta).add(new WeightedBlockEntry(weight, Pair.of(replacement, replacementMeta)));
	}

	public boolean matches(final Block toReplace, final int meta){
		return this.block == toReplace && (this.replacements.containsKey(meta) || this.replacements.containsKey(-1));
	}

	public boolean matches(final Block toReplace){
		return this.block == toReplace;
	}

}
