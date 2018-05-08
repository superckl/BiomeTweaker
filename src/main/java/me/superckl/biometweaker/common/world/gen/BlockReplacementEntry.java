package me.superckl.biometweaker.common.world.gen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.WeightedBlockEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockReplacementEntry {

	private final Block block;
	private final TIntObjectMap<List<WeightedBlockEntry>> replacements = new TIntObjectHashMap<>();

	public BlockReplacementEntry(final Block toReplace){
		this.block = toReplace;
	}

	@Nullable
	public List<WeightedBlockEntry> findEntriesForMeta(final int meta){
		if(this.replacements.containsKey(-1)) {
			final List<WeightedBlockEntry> entries = new ArrayList<>(this.replacements.get(-1));
			if(this.replacements.containsKey(meta))
				entries.addAll(this.replacements.get(meta));
			return entries;
		}
		return this.replacements.get(meta);
	}

	public void registerReplacement(final int weight, final IBlockState toReplace, final ReplacementConstraints replacement){
		if(replacement.isIgnoreMeta()) {
			if(!this.replacements.containsKey(-1)){
				this.replacements.put(-1, Lists.newArrayList(new WeightedBlockEntry(weight, replacement)));
				return;
			}
			this.replacements.get(-1).add(new WeightedBlockEntry(weight, replacement));
		}
		final int meta = toReplace.getBlock().getMetaFromState(toReplace);
		if(!this.replacements.containsKey(meta)){
			this.replacements.put(meta, Lists.newArrayList(new WeightedBlockEntry(weight, replacement)));
			return;
		}
		this.replacements.get(meta).add(new WeightedBlockEntry(weight, replacement));
	}

	public boolean matches(final IBlockState toReplace){
		return this.block == toReplace.getBlock() && (this.replacements.containsKey(-1) || this.replacements.containsKey(toReplace.getBlock().getMetaFromState(toReplace)));
	}

	public boolean matches(final Block toReplace){
		return this.block == toReplace;
	}

}
