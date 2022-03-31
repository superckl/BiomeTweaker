package me.superckl.biometweaker.util;

import java.lang.reflect.Field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public enum Classes{

		BIOME_GENERATION_SETTINGS("net.minecraft.world.level.biome.BiomeGenerationSettings");

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

		FEATURES(Classes.BIOME_GENERATION_SETTINGS, "f_4778"+"1_");

		private final Classes clazz;
		private final String name;

		public Field get() throws ClassNotFoundException {
			return ObfuscationReflectionHelper.findField(this.clazz.get(), this.name);
		}

	}

}
