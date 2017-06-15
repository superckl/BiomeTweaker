package me.superckl.biometweaker.script.command.generation.feature.tree;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import net.minecraft.block.Block;

@RequiredArgsConstructor
public class ScriptCommandAddTreesSoilBlock implements IScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final String block;
	private final int meta;

	public ScriptCommandAddTreesSoilBlock(final WorldGenTreesBuilder builder, final String block) {
		this(builder, block, -1);
	}

	@Override
	public void perform() throws Exception {
		final Block toSet = Block.getBlockFromName(this.block);
		if(toSet == null)
			throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
		this.builder.addSoilPredicate(new BlockEquivalencePredicate(this.meta == -1 ? toSet.getDefaultState():toSet.getStateFromMeta(this.meta)));
	}

}
