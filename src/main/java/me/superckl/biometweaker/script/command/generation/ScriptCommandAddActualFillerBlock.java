package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.ArrayHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addActualFillerBlock")
@RequiredArgsConstructor
public class ScriptCommandAddActualFillerBlock extends ScriptCommand{

	private final BiomePackage pack;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			IBlockState[] blocks = BiomePropertyManager.ACTUAL_FILLER_BLOCKS.get(biome);
			blocks = ArrayHelper.append(blocks, new IBlockState[] {this.block.build()});
			BiomePropertyManager.ACTUAL_FILLER_BLOCKS.set(biome, blocks);
		}
	}

}
