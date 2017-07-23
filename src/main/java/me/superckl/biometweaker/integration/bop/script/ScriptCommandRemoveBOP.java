package me.superckl.biometweaker.integration.bop.script;

import java.util.Iterator;
import java.util.List;

import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.enums.BOPClimates;
import biomesoplenty.api.enums.BOPClimates.WeightedBiomeEntry;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.integration.bop.BOPBiomeProperties;
import me.superckl.biometweaker.integration.bop.BOPIntegrationModule;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandRemoveBOP extends ScriptCommand{

	private final BiomePackage pack;
	private final String[] types;

	public ScriptCommandRemoveBOP(final BiomePackage pack) {
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
					final Iterator<WeightedBiomeEntry> bit = WarningHelper.<List<WeightedBiomeEntry>>uncheckedCast(BOPBiomeProperties.LAND_BIOMES.get(climate)).iterator();
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
					final Iterator<WeightedBiomeEntry> bit = WarningHelper.<List<WeightedBiomeEntry>>uncheckedCast(BOPBiomeProperties.LAND_BIOMES.get(climate)).iterator();
					while (bit.hasNext()) {
						final WeightedBiomeEntry entry = bit.next();
						if (Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(biome)) {
							bit.remove();
							BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.set(climate, BOPBiomeProperties.TOTAL_BIOMES_WEIGHT.get(climate)-entry.weight);
						}
					}
				}
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(biome));
		}
	}

}
