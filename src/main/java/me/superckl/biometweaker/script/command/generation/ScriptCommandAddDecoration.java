package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.common.world.gen.feature.DecorationManager;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandAddDecoration implements IScriptCommand{

	private final IBiomePackage pack;
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
