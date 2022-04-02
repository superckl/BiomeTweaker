package me.superckl.biometweaker.util;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public enum Classes{

		BIOME_GENERATION_SETTINGS("net.minecraft.world.level.biome.BiomeGenerationSettings"),
		BIOME("net.minecraft.world.level.biome.Biome"),
		CLIMATE_SETTINGS("net.minecraft.world.level.biome.Biome.ClimateSettings");

		@Getter
		private final String name;

		public String getInternalName(){
			return this.name.replace('.', '/');
		}

		public Class<?> get() throws ClassNotFoundException{
			return Class.forName(this.name);
		}
	}

	@RequiredArgsConstructor
	public enum Fields {

		FEATURES(Classes.BIOME_GENERATION_SETTINGS, "f_4778"+"1_"),
		TEMPERATURE(Classes.CLIMATE_SETTINGS, "f_4768"+"1_"),
		DOWNFALL(Classes.CLIMATE_SETTINGS, "f_4768"+"3_"),
		PRECIPITATION(Classes.CLIMATE_SETTINGS, "f_4768"+"0_"),
		TEMPERATURE_MODIFIER(Classes.CLIMATE_SETTINGS, "f_4768"+"2_"),
		CLIMATE_SETTINGS(Classes.BIOME, "f_4743"+"7_");

		@Getter
		private final Classes clazz;
		@Getter
		private final String name;

		public Field get() throws ClassNotFoundException {
			return ObfuscationReflectionHelper.findField(this.clazz.get(), this.name);
		}

	}

}
