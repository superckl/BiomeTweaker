package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.common.world.gen.feature.DecorationManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addDecoration")
@RequiredArgsConstructor
public class ScriptCommandAddDecoration extends ScriptCommand{

	private final BiomePackage pack;
	private final WorldGeneratorBuilder<?> builder;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			if(TweakWorldManager.getCurrentWorld() == null)
				DecorationManager.registerGlobalDecoration(Biome.getIdForBiome(biome), this.builder.build());
			else
				DecorationManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).registerDecoration(Biome.getIdForBiome(biome), this.builder.build());
		}
	}

}
