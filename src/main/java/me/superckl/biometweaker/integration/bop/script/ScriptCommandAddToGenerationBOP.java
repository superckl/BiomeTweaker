package me.superckl.biometweaker.integration.bop.script;

import java.util.Iterator;

import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.enums.BOPClimates;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.integration.bop.BOPIntegrationModule;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandAddToGenerationBOP extends ScriptCommand{

	private final BiomePackage pack;
	private final String type;
	private final int weight;

	@Override
	public void perform() throws Exception {
		final BOPClimates climate = BOPClimates.valueOf(this.type);
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final IExtendedBiome eBiome = BOPIntegrationModule.getExtendedBiome(biome);
			climate.addBiome(this.weight, eBiome.getBaseBiome());
		}
	}

}
