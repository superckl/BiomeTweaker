package me.superckl.api.superscript.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CollectionHelper {

	public static <K,V> Map<K,V> linkedMapWithEntry(final K key, final V value){
		final Map<K,V> map = new LinkedHashMap<K, V>();
		map.put(key, value);
		return map;
	}

	public static <T> int find(final T toFind, final T[] in){
		for(int i = 0; i < in.length; i++)
			if((in[i] == toFind) || in[i].equals(toFind))
				return i;
		return -1;
	}

	public static int find(final int toFind, final int[] in){
		for(int i = 0; i < in.length; i++)
			if(in[i] == toFind)
				return i;
		return -1;
	}


	public static int[] range(final int start, final int end){
		final int[] values = new int[(end-start)+1];
		for(int i = start; i <= end; i++)
			values[i-start] = i;
		return values;
	}

	public static void addAll(final Collection<Integer> coll, final int ... values){
		for(final int i:values)
			coll.add(i);
	}

	public static <T> boolean allContains(final T element, final Iterable<? extends Collection<T>> colls){
		for(final Collection<T> coll:colls)
			if(!coll.contains(element))
				return false;
		return true;
	}

	public static String[] trimAll(final String ... strings){
		for(int i = 0; i < strings.length; i++)
			strings[i] = strings[i].trim();
		return strings;
	}

	public static <T, V extends Collection<T>> V addAllFromArray(final V addTo, final Object array){
		for(int i = 0; i < Array.getLength(array); i++)
			addTo.add(WarningHelper.uncheckedCast(Array.get(array, i)));
		return addTo;
	}

	public static <T, V> T reverseLookup(final Map<T,V> map, final V value){
		for(final Entry<T,V> entry:map.entrySet())
			if(entry.getValue().equals(value))
				return entry.getKey();
		return null;
	}

}
