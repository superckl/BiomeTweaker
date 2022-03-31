package me.superckl.biometweaker.common.world.gen;

import java.util.Map;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockReplacementEntryList {

	private final Map<Block, BlockReplacementEntry> replacements = new Object2ObjectOpenHashMap<>();

	@Nullable
	public BlockReplacementEntry findEntry(final BlockState toReplace){
		final BlockReplacementEntry entry = this.replacements.get(toReplace.getBlock());
		if(entry != null && entry.matches(toReplace))
			return entry;
		return null;
	}

	@Nullable
	public BlockReplacementEntry findEntry(final Block toReplace){
		final BlockReplacementEntry entry = this.replacements.get(toReplace);
		if(entry != null && entry.matches(toReplace))
			return entry;
		return null;
	}

	public boolean matches(final Block block){
		return this.replacements.containsKey(block);
	}

	public void registerReplacement(final int weight, final BlockState toReplace, final ReplacementConstraints replacement){
		if(!this.replacements.containsKey(toReplace.getBlock())){
			final BlockReplacementEntry entry = new BlockReplacementEntry(toReplace.getBlock());
			entry.registerReplacement(weight, toReplace, replacement);
			this.replacements.put(toReplace.getBlock(), entry);
			return;
		}
		this.replacements.get(toReplace.getBlock()).registerReplacement(weight, toReplace, replacement);
	}

	@Override
	public String toString() {
		return this.replacements.toString();
	}

}
