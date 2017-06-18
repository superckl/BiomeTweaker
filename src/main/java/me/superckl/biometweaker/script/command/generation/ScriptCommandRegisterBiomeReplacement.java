package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandRegisterBiomeReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int replaceWith;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final int id = Biome.getIdForBiome(it.next());
			BiomeEventHandler.getBiomeReplacements().put(id, this.replaceWith);
			BiomeTweaker.getInstance().onTweak(id);
		}
	}

}
