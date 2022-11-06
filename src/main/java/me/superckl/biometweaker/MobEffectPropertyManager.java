package me.superckl.biometweaker;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.superckl.api.biometweaker.APIInfo;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeModificationManager.MobEffectModification.Builder;

public class MobEffectPropertyManager {

	public static Property<Integer, Builder, ?> AMPLIFIER;
	public static Property<Integer, Builder, ?> DURATION;
	public static Property<Integer, Builder, ?> INTERVAL;
	public static Property<Float, Builder, ?> CHANCE;
	public static Property<Boolean, Builder, ?> VISIBLE_PARTICLES;
	public static Property<Boolean, Builder, ?> SHOW_ICON;

	private static final Map<String, Property<?, ?, ?>> propertyMap = new HashMap<>();

	public static void populatePropertyMap(){
		final Field[] fields = MobEffectPropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				MobEffectPropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), WarningHelper.uncheckedCast(field.get(null)));
			} catch (final Exception e) {
				APIInfo.log.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static Property<?, ?, ?> findProperty(final String name){
		return MobEffectPropertyManager.propertyMap.get(name);
	}

	public static Collection<Property<?, ?, ?>> getAllProperties(){
		return Collections.unmodifiableCollection(MobEffectPropertyManager.propertyMap.values());
	}

	public static Set<String> getAllNames(){
		return Collections.unmodifiableSet(MobEffectPropertyManager.propertyMap.keySet());
	}

}
