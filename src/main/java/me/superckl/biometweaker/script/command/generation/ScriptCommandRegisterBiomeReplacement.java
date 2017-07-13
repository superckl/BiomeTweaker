package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.gen.layer.GenLayerReplacement;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "registerGenBiomeRep")
@RequiredArgsConstructor
public class ScriptCommandRegisterBiomeReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final IBiomePackage replaceWith;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final int id = Biome.getIdForBiome(it.next());
			final Iterator<Biome> it2 = this.replaceWith.getIterator();
			if(!it2.hasNext())
				throw new IllegalArgumentException("No biome found for replacement!");
			GenLayerReplacement.registerReplacement(id, Biome.getIdForBiome(it2.next()));
			BiomeTweaker.getInstance().onTweak(id);
		}
	}

}
