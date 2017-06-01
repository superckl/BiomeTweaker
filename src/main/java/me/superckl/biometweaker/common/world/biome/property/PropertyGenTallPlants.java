package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenDoublePlantBlank;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;

public class PropertyGenTallPlants extends Property<Boolean>{

	public PropertyGenTallPlants() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		Biome.DOUBLE_PLANT_GENERATOR = val ? new WorldGenDoublePlant():new WorldGenDoublePlantBlank();
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		return !(Biome.DOUBLE_PLANT_GENERATOR instanceof WorldGenDoublePlantBlank);
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
