package me.superckl.biometweaker.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionHelper {

	public static <K,V> Map<K,V> linkedMapWithEntry(final K key, final V value){
		final Map<K,V> map = new LinkedHashMap<K, V>();
		map.put(key, value);
		return map;
	}

}
