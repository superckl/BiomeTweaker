package me.superckl.biometweaker.script.command;

import java.lang.reflect.Field;
import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.util.ArrayHelper;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandAddActualFillerBlock implements IScriptCommand{

	private static Field actualFillerBlocks;

	private final IBiomePackage pack;
	private final String block;

	@Override
	public void perform() throws Exception {
		if(ScriptCommandAddActualFillerBlock.actualFillerBlocks == null)
			ScriptCommandAddActualFillerBlock.actualFillerBlocks = BiomeGenBase.class.getDeclaredField("actualFillerBlocks");
		final Block toAdd = Block.getBlockFromName(this.block);
		if(toAdd == null)
			throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase biome = it.next();
			Block[] blocks = (Block[]) ScriptCommandAddActualFillerBlock.actualFillerBlocks.get(biome);
			blocks = ArrayHelper.append(blocks, toAdd);
			ScriptCommandAddActualFillerBlock.actualFillerBlocks.set(biome, blocks);
		}
	}

}
