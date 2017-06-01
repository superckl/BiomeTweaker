package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;

public class PropertyGenStrongholds extends Property<Boolean>{

	public PropertyGenStrongholds() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(val)
			BiomeManager.addStrongholdBiome((Biome) obj);
		else
			BiomeManager.removeStrongholdBiome((Biome) obj);
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		return BiomeManager.strongHoldBiomes.contains(obj) && !BiomeManager.strongHoldBiomesBlackList.contains(obj);
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

}
