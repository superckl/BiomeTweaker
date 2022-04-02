package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class BiomePackage implements Iterable<Biome>{

	public abstract Iterator<ResourceLocation> locIterator();

	@Override
	public Iterator<Biome> iterator() {
		return Iterators.transform(this.locIterator(), ForgeRegistries.BIOMES::getValue);
	}

	public abstract boolean requiresRegistry();

}
