package me.superckl.biometweaker.integration.bop;

import java.util.ArrayList;

import biomesoplenty.api.enums.BOPClimates;
import me.superckl.api.biometweaker.property.PropertyField;

public class BOPBiomeProperties {

	public static final PropertyField<Integer> TOTAL_BIOMES_WEIGHT = new PropertyField<>(BOPClimates.class, "totalBiomesWeight", Integer.class);
	public static final PropertyField<ArrayList> LAND_BIOMES = new PropertyField<>(BOPClimates.class, "landBiomes", ArrayList.class);

}
