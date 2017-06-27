package me.superckl.biometweaker.common.world.gen;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockReplacementEntryList {

	private final Map<Block, BlockReplacementEntry> replacements = Maps.newIdentityHashMap();

	@Nullable
	public BlockReplacementEntry findEntry(final IBlockState toReplace){
		final BlockReplacementEntry entry = this.replacements.get(toReplace.getBlock());
		if(entry != null && entry.matches(toReplace))
			return entry;
		else
			return null;
	}

	@Nullable
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

	public void registerReplacement(final int weight, final IBlockState toReplace, final IBlockState replacement){
		if(!this.replacements.containsKey(toReplace.getBlock())){
			final BlockReplacementEntry entry = new BlockReplacementEntry(toReplace.getBlock());
			entry.registerReplacement(weight, toReplace, replacement);
			this.replacements.put(toReplace.getBlock(), entry);
			return;
		}
		this.replacements.get(toReplace.getBlock()).registerReplacement(weight, toReplace, replacement);
	}

}
