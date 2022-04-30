package me.superckl.api.biometweaker.property;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.superckl.api.biometweaker.APIInfo;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeModificationManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;

public class BiomePropertyManager {

	public static EarlyBiomeProperty<Float> TEMPERATURE;
	public static EarlyBiomeProperty<Biome.TemperatureModifier> TEMPERATURE_MODIFIER;
	public static EarlyBiomeProperty<Biome.Precipitation> PRECIPITATION;
	public static EarlyBiomeProperty<Float> DOWNFALL;
	public static EarlyBiomeProperty<Integer> GRASS_COLOR;
	public static EarlyBiomeProperty<Integer> FOLIAGE_COLOR;
	public static EarlyBiomeProperty<Integer> WATER_COLOR;
	public static EarlyBiomeProperty<Integer> WATER_FOG_COLOR;
	public static EarlyBiomeProperty<Integer> SKY_COLOR;
	public static EarlyBiomeProperty<Integer> FOG_COLOR;
	public static EarlyBiomeProperty<Float> SPAWN_PROBABILITY;
	public static EarlyBiomeProperty<BiomeCategory> CATEGORY;
	public static EarlyBiomeProperty<String> AMBIENT_LOOP_SOUND;
	public static EarlyBiomeProperty<Boolean> DISABLE_SLEEP;
	public static EarlyBiomeProperty<Boolean> DISABLE_BONEMEAL;
	public static EarlyBiomeProperty<Boolean> DISABLE_CROP_GROWTH;
	public static EarlyBiomeProperty<Boolean> DISABLE_SAPLING_GROWTH;
	public static EarlyBiomeProperty<Float> FOG_START_MODIFIER;
	public static EarlyBiomeProperty<Float> FOG_END_MODIFIER;
	public static EarlyBiomeProperty<BiomeModificationManager.FogShape> FOG_SHAPE;

	private static final Map<String, Property<?, ?>> propertyMap = new HashMap<>();

	public static void populatePropertyMap(){
		final Field[] fields = BiomePropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				BiomePropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), WarningHelper.uncheckedCast(field.get(null)));
			} catch (final Exception e) {
				APIInfo.log.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?, ?> findProperty(final String name){
		return BiomePropertyManager.propertyMap.get(name);
	}

	public static Collection<Property<?, ?>> getAllProperties(){
		return Collections.unmodifiableCollection(BiomePropertyManager.propertyMap.values());
	}

	public static Set<String> getAllNames(){
		return Collections.unmodifiableSet(BiomePropertyManager.propertyMap.keySet());
	}

}
