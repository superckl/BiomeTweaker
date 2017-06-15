package me.superckl.biometweaker.script.command.generation.feature;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.block.Block;

@RequiredArgsConstructor
public class ScriptCommandSetDecorationBlock implements IScriptCommand{

	private final WorldGeneratorBuilder<?> builder;
	private final String block;
	private final int meta;

	public ScriptCommandSetDecorationBlock(final WorldGeneratorBuilder<?> builder, final String block) {
		this(builder, block, -1);
	}

	@Override
	public void perform() throws Exception {
		final Block toSet = Block.getBlockFromName(this.block);
		if(toSet == null)
			throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
		this.builder.setMainBlock(this.meta == -1 ? toSet.getDefaultState():toSet.getStateFromMeta(this.meta));
	}

}
