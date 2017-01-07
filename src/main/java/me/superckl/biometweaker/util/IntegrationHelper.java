package me.superckl.biometweaker.util;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.world.WorldType;

public class IntegrationHelper {

	private static Field excludedWorldTypes = null;
	
	@SuppressWarnings("unchecked")
	public static List<WorldType> reflectBOPWorldTypesList(){
			try {
				if(excludedWorldTypes == null){
					LogHelper.info("Attempting to reflect excludedDecoratedWorldTypes list from BOP...");
					Class<?> clazz = Class.forName("biomesoplenty.api.biome.BOPBiomes");
					excludedWorldTypes = clazz.getDeclaredField("excludedDecoratedWorldTypes");
					excludedWorldTypes.setAccessible(true);
				}
				LogHelper.info("Success. Attemping to retrieve List...");
				return (List<WorldType>) excludedWorldTypes.get(null);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to reflect excludedDecoratedWorldTypes list from BOP!", e);
			}
	}
	
}
