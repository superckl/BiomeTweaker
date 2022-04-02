package me.superckl.biometweaker.common.world.gen;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.LogHelper;

public class ReplacementPropertyManager {

	public static Property<BlockStateBuilder<?>, ReplacementConstraints> BLOCK;
	public static Property<Integer, ReplacementConstraints> MIN_Y;
	public static Property<Integer, ReplacementConstraints> MAX_Y;
	public static Property<Integer, ReplacementConstraints> MIN_X;
	public static Property<Integer, ReplacementConstraints> MAX_X;
	public static Property<Integer, ReplacementConstraints> MIN_Z;
	public static Property<Integer, ReplacementConstraints> MAX_Z;
	public static Property<Integer, ReplacementConstraints> MIN_CHUNK_X;
	public static Property<Integer, ReplacementConstraints> MAX_CHUNK_X;
	public static Property<Integer, ReplacementConstraints> MIN_CHUNK_Z;
	public static Property<Integer, ReplacementConstraints> MAX_CHUNK_Z;
	public static Property<Boolean, ReplacementConstraints> IGNORE_META;
	public static Property<Boolean, ReplacementConstraints> CONTIGUOUS;

	private static final Map<String, Property<?, ReplacementConstraints>> propertyMap = new HashMap<>();

	public static void populatePropertyMap(){
		final Field[] fields = ReplacementPropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				ReplacementPropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), WarningHelper.uncheckedCast(field.get(null)));
			} catch (final Exception e) {
				LogHelper.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?, ReplacementConstraints> findProperty(final String name){
		return ReplacementPropertyManager.propertyMap.get(name);
	}
}
