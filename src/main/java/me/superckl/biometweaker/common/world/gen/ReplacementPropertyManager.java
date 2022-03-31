package me.superckl.biometweaker.common.world.gen;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.LogHelper;
import net.minecraft.world.level.block.state.BlockState;

public class ReplacementPropertyManager {

	public static Property<? extends BlockState> BLOCK;
	public static Property<Integer> MIN_Y;
	public static Property<Integer> MAX_Y;
	public static Property<Integer> MIN_X;
	public static Property<Integer> MAX_X;
	public static Property<Integer> MIN_Z;
	public static Property<Integer> MAX_Z;
	public static Property<Integer> MIN_CHUNK_X;
	public static Property<Integer> MAX_CHUNK_X;
	public static Property<Integer> MIN_CHUNK_Z;
	public static Property<Integer> MAX_CHUNK_Z;
	public static Property<Boolean> IGNORE_META;
	public static Property<Boolean> CONTIGUOUS;

	private static final Map<String, Property<?>> propertyMap = new HashMap<>();

	public static void populatePropertyMap(){
		final Field[] fields = ReplacementPropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				ReplacementPropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), (Property<?>) field.get(null));
			} catch (final Exception e) {
				LogHelper.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?> findProperty(final String name){
		return ReplacementPropertyManager.propertyMap.get(name);
	}
}
