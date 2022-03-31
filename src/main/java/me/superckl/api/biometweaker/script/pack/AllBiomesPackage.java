package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class AllBiomesPackage extends BiomePackage{

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> list = new ArrayList<>();
		final Iterator<Biome> it = ForgeRegistries.BIOMES.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen != null)
				list.add(gen);
		}
		return list.iterator();
	}

}
