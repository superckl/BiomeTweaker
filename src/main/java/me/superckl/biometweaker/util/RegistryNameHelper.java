package me.superckl.biometweaker.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryNameHelper {

	public static ResourceLocation getRegistryName(final Biome biome) {
		return ForgeRegistries.BIOMES.getKey(biome);
	}

	public static ResourceLocation getRegistryName(final EntityType<?> type) {
		return ForgeRegistries.ENTITY_TYPES.getKey(type);
	}

	public static ResourceLocation getRegistryName(final SoundEvent sound) {
		return ForgeRegistries.SOUND_EVENTS.getKey(sound);
	}

}
