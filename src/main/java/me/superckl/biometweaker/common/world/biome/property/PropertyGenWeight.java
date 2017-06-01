package me.superckl.biometweaker.common.world.biome.property;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import me.superckl.api.biometweaker.APIInfo;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class PropertyGenWeight extends Property<Integer>{

	private static Set<BiomeManager.BiomeType> logged = EnumSet.noneOf(BiomeManager.BiomeType.class);

	public PropertyGenWeight() {
		super(Integer.class);
	}

	@Override
	public void set(final Object obj, final Integer val) throws IllegalStateException, IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		final int weight  = val;
		final int id = Biome.getIdForBiome((Biome) obj);
		for(final BiomeType type:BiomeType.values()){
			final List<BiomeEntry> entries = BiomeManager.getBiomes(type);
			for(final BiomeEntry entry:entries)
				if(Biome.getIdForBiome(entry.biome) == id)
					entry.itemWeight = weight;
			if((type != BiomeManager.BiomeType.DESERT) && !PropertyGenWeight.logged.contains(type) && (WeightedRandom.getTotalWeight(entries) <= 0)){
				APIInfo.log.warn("Sum of biome generation weights for type "+type+" is zero! This will cause Vanilla generation to crash! You have been warned!");
				PropertyGenWeight.logged.add(type);
			}
		}
		try {
			BiomeHelper.modTypeLists();
		} catch (final Exception e) {
			throw new IllegalStateException("Failed to set type lists as modded!", e);
		}
	}

	@Override
	public Integer get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Can't get gen weight!");
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

}
