package me.superckl.biometweaker.script;

import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import com.google.common.collect.Lists;

public class TypeBiomesPackage implements IBiomePackage{

	private final BiomeDictionary.Type[] types;

	public TypeBiomesPackage(final String ... types) {
		final BiomeDictionary.Type[] bTypes = new BiomeDictionary.Type[types.length];
		for(int i = 0; i < types.length; i++)
			bTypes[i] = BiomeDictionary.Type.getType(types[i]);
		this.types = bTypes;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		final List<BiomeGenBase> list = Lists.newArrayList();
		for(final BiomeDictionary.Type type:this.types){
			final BiomeGenBase[] gens = BiomeDictionary.getBiomesForType(type);
			for(final BiomeGenBase gen:gens)
				if(!list.contains(gen))
					list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> ints = Lists.newArrayList();
		for(final BiomeDictionary.Type type:this.types){
			final BiomeGenBase[] gens = BiomeDictionary.getBiomesForType(type);
			for(final BiomeGenBase gen:gens)
				ints.add(gen.biomeID);
		}
		return ints;
	}

}
