package me.superckl.biometweaker.script.command.block;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.common.world.gen.TweakWorldManager;
import me.superckl.biometweaker.script.command.BiomeScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "registerGenBlockRep")
@RequiredArgsConstructor
public class ScriptCommandRegisterBlockReplacement extends BiomeScriptCommand{

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
		this.pack.locIterator().forEachRemaining(loc -> {
			if(TweakWorldManager.getCurrentWorld() == null)
				BlockReplacementManager.registerGlobalBlockReplacement(loc, this.weight, this.toReplace.build(), this.replaceWith);
			else
				BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).registerBlockReplacement(loc,
						this.weight, this.toReplace.build(), this.replaceWith);
		});
	}

}
