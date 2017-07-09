package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "registerGenBlockRep")
@RequiredArgsConstructor
public class ScriptCommandRegisterBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int weight;
	private final BlockStateBuilder<?> toReplace;
	private final BlockStateBuilder<?> replaceWith;

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final BlockStateBuilder<?> block1, final BlockStateBuilder<?> block2) {
		this(pack, 1, block1, block2);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RegisterGenBlockReplacement(this, this.weight, gen, this.toReplace, this.replaceWith)))
				continue;
			if(TweakWorldManager.getCurrentWorld() == null)
				BlockReplacementManager.registerGlobalBlockReplacement(Biome.getIdForBiome(gen), this.weight, this.toReplace.build(), this.replaceWith.build());
			else
				BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).registerBlockReplacement(Biome.getIdForBiome(gen),
						this.weight, this.toReplace.build(), this.replaceWith.build());
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

}
