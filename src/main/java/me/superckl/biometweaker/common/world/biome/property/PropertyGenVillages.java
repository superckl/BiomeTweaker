package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.BiomeManager;

public class PropertyGenVillages extends Property<Boolean>{

	public PropertyGenVillages() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(val)
			BiomeManager.addVillageBiome((Biome) obj, true);
		else
			BiomeManager.removeVillageBiome((Biome) obj);
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		return MapGenVillage.VILLAGE_SPAWN_BIOMES.contains(obj);
	}

}
