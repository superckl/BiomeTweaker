package me.superckl.biometweaker.script.command.block;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.TweakWorldManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.level.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "registerGenBlockRep")
@RequiredArgsConstructor
public class ScriptCommandRegisterBlockReplacement extends ScriptCommand{

	private final BiomePackage pack;
	private final int weight;
	private final BlockStateBuilder<?> toReplace;
	private final ReplacementConstraints replaceWith;

	public ScriptCommandRegisterBlockReplacement(final BiomePackage pack, final BlockStateBuilder<?> block1, final ReplacementConstraints block2) {
		this(pack, 1, block1, block2);
	}

	@Override
	public void perform() throws Exception {
		if(this.replaceWith == null || !this.replaceWith.hasBlock())
			throw new IllegalStateException("Cannot register block replacement with no block specified!");
		final Iterator<Biome> it = this.pack.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(TweakWorldManager.getCurrentWorld() == null)
				BlockReplacementManager.registerGlobalBlockReplacement(gen.getRegistryName(), this.weight, this.toReplace.build(), this.replaceWith);
			else
				BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).registerBlockReplacement(gen.getRegistryName(),
						this.weight, this.toReplace.build(), this.replaceWith);
		}
	}

}
