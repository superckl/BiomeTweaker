package me.superckl.biometweaker.util;

import java.lang.reflect.Array;

import me.superckl.api.superscript.util.WarningHelper;

public class ArrayHelper {

	public static <T> T[] append(final Class<?> clazz, final T[] appendTo, final T[] toAppend){
		final T[] newArray = WarningHelper.uncheckedCast(Array.newInstance(clazz, appendTo.length+toAppend.length));
		System.arraycopy(appendTo, 0, newArray, 0, appendTo.length);
		System.arraycopy(toAppend, 0, newArray, appendTo.length, toAppend.length);
		return newArray;
	}

	public static <T> T[] append(final T[] appendTo, final T[] toAppend){
		return ArrayHelper.append(appendTo.getClass().getComponentType(), appendTo, toAppend);
	}

}
