package me.superckl.biometweaker.integration.bop;

import java.lang.reflect.Field;

import biomesoplenty.api.enums.BOPClimates;

public class BOPReflectionHelper {

	public static Field totalLandBiomesWeight = null;
	public static Field landBiomes = null;

	public static void reflectFields() throws Exception{
		if(BOPReflectionHelper.totalLandBiomesWeight == null){
			BOPReflectionHelper.totalLandBiomesWeight = BOPClimates.class.getDeclaredField("totalLandBiomesWeight");
			BOPReflectionHelper.totalLandBiomesWeight.setAccessible(true);
		}
		if(BOPReflectionHelper.landBiomes == null){
			BOPReflectionHelper.landBiomes = BOPClimates.class.getDeclaredField("landBiomes");
			BOPReflectionHelper.landBiomes.setAccessible(true);
		}
	}

}
