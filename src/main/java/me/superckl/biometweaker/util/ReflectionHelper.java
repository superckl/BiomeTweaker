package me.superckl.biometweaker.util;

public class ReflectionHelper {

	public static Class<?> tryLoadClass(final String className){
		try{
			return Class.forName(className);
		}catch(final Exception e){

		}
		return null;
	}

}
