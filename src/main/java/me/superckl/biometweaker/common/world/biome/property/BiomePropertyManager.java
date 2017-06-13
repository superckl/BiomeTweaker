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
import me.superckl.biometweaker.core.ObfNameHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class BiomePropertyManager {

	public static final PropertyField<String> NAME = new PropertyField<>(Biome.class, ObfNameHelper.Fields.BIOMENAME.getName(), String.class);
	public static final PropertyField<Float> HEIGHT = new PropertyField<>(Biome.class, ObfNameHelper.Fields.BASEHEIGHT.getName(), Float.class);
	public static final PropertyField<Float> HEIGHT_VARIATION = new PropertyField<>(Biome.class, ObfNameHelper.Fields.HEIGHTVARIATION.getName(), Float.class);
	public static final PropertyBlockState TOP_BLOCK = new PropertyBlockState(Biome.class, ObfNameHelper.Fields.TOPBLOCK.getName());
	public static final PropertyBlockStateMeta TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, ObfNameHelper.Fields.TOPBLOCK.getName());
	public static final PropertyBlockState FILLER_BLOCK = new PropertyBlockState(Biome.class, ObfNameHelper.Fields.FILLERBLOCK.getName());
	public static final PropertyBlockStateMeta FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class, ObfNameHelper.Fields.FILLERBLOCK.getName());
	public static final PropertyBlockState OCEAN_TOP_BLOCK = new PropertyBlockState(Biome.class, "oceanTopBlock");
	public static final PropertyBlockStateMeta OCEAN_TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "oceanTopBlock");
	public static final PropertyBlockState OCEAN_FILLER_BLOCK = new PropertyBlockState(Biome.class, "oceanFillerBlock");
	public static final PropertyBlockStateMeta OCEAN_FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "oceanFillerBlock");
	public static final PropertyField<Float> TEMPERATURE = new PropertyField<>(Biome.class, ObfNameHelper.Fields.TEMPERATURE.getName(), Float.class);
	public static final PropertyField<Float> HUMIDITY = new PropertyField<>(Biome.class, ObfNameHelper.Fields.RAINFALL.getName(), Float.class);
	public static final PropertyField<Integer> WATER_TINT = new PropertyField<>(Biome.class, ObfNameHelper.Fields.WATERCOLOR.getName(), Integer.class);
	public static final PropertyField<Boolean> ENABLE_RAIN = new PropertyField<>(Biome.class, ObfNameHelper.Fields.ENABLERAIN.getName(), Boolean.class);
	public static final PropertyField<Boolean> ENABLE_SNOW = new PropertyField<>(Biome.class, ObfNameHelper.Fields.ENABLESNOW.getName(), Boolean.class);
	public static final PropertyField<Integer> GRASS_COLOR = new PropertyField<>(Biome.class, "grassColor", Integer.class);
	public static final PropertyField<Integer> FOLIAGE_COLOR = new PropertyField<>(Biome.class, "foliageColor", Integer.class);
	public static final PropertyField<Integer> WATER_COLOR = new PropertyField<>(Biome.class, "waterColor", Integer.class);
	public static final PropertyField<Integer> SKY_COLOR = new PropertyField<>(Biome.class, "skyColor", Integer.class);
	public static final PropertyField<IBlockState[]> ACTUAL_FILLER_BLOCKS = new PropertyField<>(Biome.class, "actualFillerBlocks", IBlockState[].class);
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
	public static final PropertyDecorationPerChunk DESERT_WELLS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.DESERT_WELL);
	public static final PropertyDecorationPerChunk FOSSILS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.FOSSIL);
	public static final PropertyDecorationPerChunk ICE_PER_CHUNK = new PropertyDecorationPerChunk(EventType.ICE);
	public static final PropertyDecorationPerChunk LAKES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LAKE_WATER);
	public static final PropertyDecorationPerChunk LAVA_LAKES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LAKE_LAVA);
	public static final PropertyDecorationPerChunk PUMPKINS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.PUMPKIN);
	public static final PropertyDecorationPerChunk ROCK_PER_CHUNK = new PropertyDecorationPerChunk(EventType.ROCK);
	public static final PropertyDecorationPerChunk SAND2_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SAND_PASS2);

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

	public static boolean setProperty(final Biome biome, final String property, final JsonElement value) throws Exception{
		final Property<?> prop = BiomePropertyManager.propertyMap.get(property);
		if(prop == null)
			throw new IllegalArgumentException("No property found for "+property);
		if(!prop.isSettable())
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
			throw e;
		}
		return true;
	}

}
