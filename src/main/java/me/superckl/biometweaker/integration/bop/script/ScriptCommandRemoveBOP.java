package me.superckl.biometweaker.integration.bop.script;

import java.util.Iterator;
import java.util.List;

import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.enums.BOPClimates;
import biomesoplenty.api.enums.BOPClimates.WeightedBiomeEntry;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.integration.bop.BOPBiomeProperties;
import me.superckl.biometweaker.integration.bop.BOPIntegrationModule;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandRemoveBOP implements IScriptCommand{

	private final IBiomePackage pack;
	private final String[] types;

	public ScriptCommandRemoveBOP(final IBiomePackage pack) {
		this(pack, null);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			Biome biome = it.next();
			final IExtendedBiome eBiome = BOPIntegrationModule.getExtendedBiome(biome);
			biome = eBiome.getBaseBiome();
			if(this.types == null)
				for(final BOPClimates climate:BOPClimates.values()){
					final Iterator<WeightedBiomeEntry> bit = ((List<WeightedBiomeEntry>) BOPBiomeProperties.LAND_BIOMES.get(climate)).iterator();
					while(bit.hasNext()){
						final WeightedBiomeEntry entry = bit.next();
						if(Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(biome)){
							bit.remove();
							BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.set(climate, BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.get(climate)-entry.weight);
						}
					}
				}
			else
				for (final String type:this.types) {
					final BOPClimates climate = BOPClimates.valueOf(type);
					if (climate == null)
						throw new IllegalArgumentException("No climate type found for: " + type);
					final Iterator<WeightedBiomeEntry> bit = ((List<WeightedBiomeEntry>) BOPBiomeProperties.LAND_BIOMES.get(climate)).iterator();
					while (bit.hasNext()) {
						final WeightedBiomeEntry entry = bit.next();
						if (Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(biome)) {
							bit.remove();
							BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.set(climate, BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.get(climate)-entry.weight);
						}
					}
				}
			Config.INSTANCE.onTweak(Biome.getIdForBiome(biome));
		}
	}

}
