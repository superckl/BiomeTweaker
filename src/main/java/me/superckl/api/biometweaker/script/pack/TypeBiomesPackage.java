package me.superckl.api.biometweaker.script.pack;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class TypeBiomesPackage extends BiomePackage{

	private final String[] sTypes;
	private BiomeDictionary.Type[] types;

	public TypeBiomesPackage(final String ... types) {
		this.sTypes = types;
	}

	@Override
	public Iterator<Biome> iterator() {
		this.checkTypes();
		final Set<ResourceLocation> biomes = new HashSet<>();
		for(final BiomeDictionary.Type type:this.types){
			final Set<ResourceKey<Biome>> gens = BiomeDictionary.getBiomes(type);
			for(final ResourceKey<Biome> gen:gens)
				if(!biomes.contains(gen.getRegistryName()))
					biomes.add(gen.getRegistryName());
		}
		return biomes.stream().map(loc -> ForgeRegistries.BIOMES.getValue(loc)).iterator();
	}

	private void checkTypes(){
		if(this.types == null){
			final BiomeDictionary.Type[] bTypes = new BiomeDictionary.Type[this.sTypes.length];
			for(int i = 0; i < this.sTypes.length; i++)
				bTypes[i] = BiomeDictionary.Type.getType(this.sTypes[i]);
			this.types = bTypes;
		}
	}

}
