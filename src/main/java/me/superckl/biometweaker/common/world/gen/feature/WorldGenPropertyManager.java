package me.superckl.biometweaker.common.world.gen.feature;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.state.IBlockState;

public class WorldGenPropertyManager {

	public static Property<Integer> COUNT;
	public static Property<IBlockState> MAIN_BLOCK;

	public static class Mineable {

		public static Property<Integer> SIZE;
		public static Property<Integer> MIN_Y;
		public static Property<Integer> MAX_Y;

	}

	public static class Cluster {

		public static Property<Integer> RADIUS;
		public static Property<Integer> HEIGHT;

	}

	public static class Tree {

		public static Property<Integer> MIN_HEIGHT;
		public static Property<Integer> HEIGHT_VARIATION;
		public static Property<Integer> LEAF_HEIGHT;
		public static Property<IBlockState> LEAF_BLOCK;
		public static Property<IBlockState> VINE_BLOCK;
		public static Property<Boolean> GROW_VINES;
		public static Property<Boolean> CHECK_CAN_GROW;

	}

	public static class Splotch {

		public static Property<Integer> SIZE;
		public static Property<Boolean> REQUIRES_BASE;

	}

	private static final Map<Class<?>, Map<String, Property<?>>> propertyMap = new IdentityHashMap<>();

	public static void populatePropertyMap(){
		final Class<?>[] classes = WorldGenPropertyManager.class.getClasses();
		final List<Field> fields = new ArrayList<>();
		Collections.addAll(fields, WorldGenPropertyManager.class.getDeclaredFields());
		for(final Class<?> clazz:classes)
			Collections.addAll(fields, clazz.getDeclaredFields());
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				final Property<?> prop = WarningHelper.uncheckedCast(field.get(null));
				if(!WorldGenPropertyManager.propertyMap.containsKey(prop.getTargetClass()))
					WorldGenPropertyManager.propertyMap.put(prop.getTargetClass(), new HashMap<>());
				WorldGenPropertyManager.propertyMap.get(prop.getTargetClass()).put(field.getName().toLowerCase().replace("_", ""), prop);
			} catch (final Exception e) {
				LogHelper.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?> findProperty(final Class<?> target, final String name){
		for(final Class<?> clazz:WorldGenPropertyManager.propertyMap.keySet())
			if(clazz.isAssignableFrom(target) && WorldGenPropertyManager.propertyMap.get(clazz).containsKey(name))
				return WorldGenPropertyManager.propertyMap.get(clazz).get(name);
		return null;
	}

}
