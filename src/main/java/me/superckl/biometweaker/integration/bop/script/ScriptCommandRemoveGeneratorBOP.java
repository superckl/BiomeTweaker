package me.superckl.biometweaker.integration.bop.script;

import java.util.Iterator;

import biomesoplenty.api.biome.IExtendedBiome;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.integration.bop.BOPIntegrationModule;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandRemoveGeneratorBOP implements IScriptCommand{

	private final IBiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final IExtendedBiome eBiome = BOPIntegrationModule.getExtendedBiome(biome);
			for (String type:this.types) {
				eBiome.getGenerationManager().removeGenerator(type);
			}
		}
	}

}
