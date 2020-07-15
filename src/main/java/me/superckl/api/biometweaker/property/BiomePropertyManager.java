package me.superckl.api.biometweaker.property;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.superckl.api.biometweaker.APIInfo;
import net.minecraft.block.state.IBlockState;

public class BiomePropertyManager {

	public static Property<String> NAME;
	public static Property<Float> HEIGHT;
	public static Property<Float> HEIGHT_VARIATION;
	public static Property<IBlockState> TOP_BLOCK;
	public static Property<IBlockState> FILLER_BLOCK;
	public static Property<IBlockState> OCEAN_TOP_BLOCK;
	public static Property<IBlockState> OCEAN_FILLER_BLOCK;
	public static Property<Float> TEMPERATURE;
	public static Property<Float> HUMIDITY;
	public static Property<Boolean> ENABLE_RAIN;
	public static Property<Boolean> ENABLE_SNOW;
	public static Property<Integer> GRASS_COLOR;
	public static Property<Integer> FOLIAGE_COLOR;
	public static Property<Integer> WATER_COLOR;
	public static Property<Integer> SKY_COLOR;
	public static Property<Integer> FOG_COLOR;
	public static Property<Boolean> GEN_INITIAL_SNOW;
	public static Property<IBlockState[]> ACTUAL_FILLER_BLOCKS;
	public static Property<Boolean> CONTIGUOUS_REPLACEMENT;
	public static Property<Integer> GEN_WEIGHT;
	public static Property<Boolean> GEN_VILLAGES;
	public static Property<Boolean> GEN_STRONGHOLDS;
	public static Property<Boolean> GEN_SCATTERED_FEATURES;
	public static Property<Boolean> IS_SPAWN_BIOME;
	public static Property<Boolean> GEN_TALL_PLANTS;
	public static Property<Integer> WATERLILY_PER_CHUNK;
	public static Property<Integer> TREES_PER_CHUNK;
	public static Property<Integer> FLOWERS_PER_CHUNK;
	public static Property<Integer> GRASS_PER_CHUNK;
	public static Property<Integer> DEAD_BUSH_PER_CHUNK;
	public static Property<Integer> MUSHROOMS_PER_CHUNK;
	public static Property<Integer> REEDS_PER_CHUNK;
	public static Property<Integer> CACTI_PER_CHUNK;
	public static Property<Integer> SAND_PER_CHUNK;
	public static Property<Integer> CLAY_PER_CHUNK;
	public static Property<Integer> BIG_MUSHROOMS_PER_CHUNK;
	public static Property<Integer> DESERT_WELLS_PER_CHUNK;
	public static Property<Integer> FOSSILS_PER_CHUNK;
	public static Property<Integer> ICE_PER_CHUNK;
	public static Property<Integer> LAKES_PER_CHUNK;
	public static Property<Integer> LAVA_LAKES_PER_CHUNK;
	public static Property<Integer> PUMPKINS_PER_CHUNK;
	public static Property<Integer> ROCK_PER_CHUNK;
	public static Property<Integer> SAND2_PER_CHUNK;

	private static final Map<String, Property<?>> propertyMap = new HashMap<>();

	public static void populatePropertyMap(){
		final Field[] fields = BiomePropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				BiomePropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), (Property<?>) field.get(null));
			} catch (final Exception e) {
				APIInfo.log.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?> findProperty(final String name){
		return BiomePropertyManager.propertyMap.get(name);
	}

	public static Collection<Property<?>> getAllProperties(){
		return Collections.unmodifiableCollection(BiomePropertyManager.propertyMap.values());
	}

	public static Set<String> getAllNames(){
		return Collections.unmodifiableSet(BiomePropertyManager.propertyMap.keySet());
	}

}
