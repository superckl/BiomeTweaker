package me.superckl.biometweaker.common.world.biome.property;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyBlockState;
import me.superckl.api.biometweaker.property.PropertyBlockStateMeta;
import me.superckl.api.biometweaker.property.PropertyField;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class BiomePropertyManager {

	public static final PropertyField<String> NAME = new PropertyField<>(Biome.class, "biomeName", String.class);
	public static final PropertyField<Float> HEIGHT = new PropertyField<>(Biome.class, "baseHeight", Float.class);
	public static final PropertyField<Float> HEIGHT_VARIATION = new PropertyField<>(Biome.class, "heightVariation", Float.class);
	public static final PropertyBlockState TOP_BLOCK = new PropertyBlockState(Biome.class, "topBlock");
	public static final PropertyBlockStateMeta TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "topBlock");
	public static final PropertyBlockState FILLER_BLOCK = new PropertyBlockState(Biome.class, "fillerBlock");
	public static final PropertyBlockStateMeta FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "fillerBlock");
	public static final PropertyBlockState OCEAN_TOP_BLOCK = new PropertyBlockState(Biome.class, "oceanTopBlock");
	public static final PropertyBlockStateMeta OCEAN_TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "oceanTopBlock");
	public static final PropertyBlockState OCEAN_FILLER_BLOCK = new PropertyBlockState(Biome.class, "oceanFillerBlock");
	public static final PropertyBlockStateMeta OCEAN_FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "oceanFillerBlock");
	public static final PropertyField<Float> TEMPERATURE = new PropertyField<>(Biome.class, "temperature", Float.class);
	public static final PropertyField<Float> HUMIDITY = new PropertyField<>(Biome.class, "rainfall", Float.class);
	public static final PropertyField<Integer> WATER_TINT = new PropertyField<>(Biome.class, "waterColor", Integer.class);
	public static final PropertyField<Boolean> ENABLE_RAIN = new PropertyField<>(Biome.class, "enableRain", Boolean.class);
	public static final PropertyField<Boolean> ENABLE_SNOW = new PropertyField<>(Biome.class, "enableSnow", Boolean.class);
	public static final PropertyField<Integer> GRASS_COLOR = new PropertyField<>(Biome.class, "grassColor", Integer.class);
	public static final PropertyField<Integer> FOLIAGE_COLOR = new PropertyField<>(Biome.class, "foliageColor", Integer.class);
	public static final PropertyField<Integer> WATER_COLOR = new PropertyField<>(Biome.class, "waterColor", Integer.class);
	public static final PropertyField<Integer> SKY_COLOR = new PropertyField<>(Biome.class, "skyColor", Integer.class);
	public static final PropertyGenWeight GEN_WEIGHT = new PropertyGenWeight();
	public static final PropertyGenVillages GEN_VILLAGES = new PropertyGenVillages();
	public static final PropertyGenStrongholds GEN_STRONGHOLDS = new PropertyGenStrongholds();
	public static final PropertySpawnBiome IS_SPAWN_BIOME = new PropertySpawnBiome();
	public static final PropertyGenTallPlants GEN_TALL_PLANTS = new PropertyGenTallPlants();
	public static final PropertyDecorationPerChunk WATERLILY_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LILYPAD);
	public static final PropertyDecorationPerChunk TREES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.TREE);
	public static final PropertyDecorationPerChunk FLOWERS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.FLOWERS);
	public static final PropertyDecorationPerChunk GRASS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.GRASS);
	public static final PropertyDecorationPerChunk DEAD_BUSH_PER_CHUNK = new PropertyDecorationPerChunk(EventType.DEAD_BUSH);
	public static final PropertyDecorationPerChunk MUSHROOMS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SHROOM);
	public static final PropertyDecorationPerChunk REEDS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.REED);
	public static final PropertyDecorationPerChunk CACTI_PER_CHUNK = new PropertyDecorationPerChunk(EventType.CACTUS);
	public static final PropertyDecorationPerChunk SAND_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SAND);
	public static final PropertyDecorationPerChunk CLAY_PER_CHUNK = new PropertyDecorationPerChunk(EventType.CLAY);
	public static final PropertyDecorationPerChunk BIG_MUSHROOMS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.BIG_SHROOM);

	public static final Map<String, Property<?>> propertyMap = Maps.newHashMap();

	static {
		final Field[] fields = BiomePropertyManager.class.getDeclaredFields();
		for(final Field field:fields){
			if(!Property.class.isAssignableFrom(field.getType()))
				continue;
			try {
				BiomePropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), (Property<?>) field.get(null));
			} catch (final Exception e) {
				LogHelper.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
		}
	}

	public static boolean setProperty(final Biome biome, final String property, final JsonElement value){
		final Property<?> prop = BiomePropertyManager.propertyMap.get(property);
		if(prop == null)
			return false;
		final Class<?> type = prop.getTypeClass();
		try {
			if(type.getCanonicalName().equals(Integer.class.getCanonicalName()))
				((Property<Integer>) prop).set(biome, value.getAsInt());
			else if(type.getCanonicalName().equals(Float.class.getCanonicalName()))
				((Property<Float>)prop).set(biome, value.getAsFloat());
			else if(type.getCanonicalName().equals(Boolean.class.getCanonicalName()))
				((Property<Boolean>)prop).set(biome, value.getAsBoolean());
			else if(type.getCanonicalName().equals(String.class.getCanonicalName()))
				((Property<String>)prop).set(biome, (String) ParameterTypes.STRING.tryParse(value.getAsString()));
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

}
