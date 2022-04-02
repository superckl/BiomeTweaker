package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class AllBiomesPackage extends BiomePackage{

	@Override
	public Iterator<ResourceLocation> locIterator() {
		return ForgeRegistries.BIOMES.getKeys().iterator();
	}

	@Override
	public Iterator<Biome> iterator() {
		return ForgeRegistries.BIOMES.iterator();
	}

	@Override
	public boolean requiresRegistry() {
		return true;
	}



}
