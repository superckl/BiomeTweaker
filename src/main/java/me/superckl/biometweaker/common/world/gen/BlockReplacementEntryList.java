package me.superckl.biometweaker.common.world.gen;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;

public class BlockReplacementEntryList {

	private final Map<Block, BlockReplacementEntry> replacements = Maps.newIdentityHashMap();

	public BlockReplacementEntry findEntry(final Block toReplace, final int meta){
		final BlockReplacementEntry entry = this.replacements.get(toReplace);
		if(entry != null && entry.matches(toReplace, meta))
			return entry;
		else
			return null;
	}

	public BlockReplacementEntry findEntry(final Block toReplace){
		final BlockReplacementEntry entry = this.replacements.get(toReplace);
		if(entry != null && entry.matches(toReplace))
			return entry;
		else
			return null;
	}

	public boolean matches(final Block block){
		return this.replacements.containsKey(block);
	}

	public void registerReplacement(final int weight, final Block toReplace, final int toReplaceMeta, final Block replacement, final int replacementMeta){
		if(!this.replacements.containsKey(toReplace)){
			this.replacements.put(toReplace, new BlockReplacementEntry(weight, toReplace, toReplaceMeta, replacement, replacementMeta));
			return;
		}
		this.replacements.get(toReplace).registerReplacement(weight, toReplaceMeta, replacement, replacementMeta);
	}

}
