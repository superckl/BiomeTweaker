package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class TypeBiomesPackage extends BiomePackage{

	private final String[] sTypes;
	private BiomeDictionary.Type[] types;

	public TypeBiomesPackage(final String ... types) {
		this.sTypes = types;
	}

	@Override
	public Iterator<Biome> getIterator() {
		this.checkTypes();
		final List<Biome> list = new ArrayList<>();
		for(final BiomeDictionary.Type type:this.types){
			final Set<Biome> gens = BiomeDictionary.getBiomes(type);
			for(final Biome gen:gens)
				if(!list.contains(gen))
					list.add(gen);
		}
		return list.iterator();
	}

	@Override
	public List<Integer> getRawIds() {
		this.checkTypes();
		final List<Integer> ints = new ArrayList<>();
		for(final BiomeDictionary.Type type:this.types){
			final Set<Biome> gens = BiomeDictionary.getBiomes(type);
			for(final Biome gen:gens)
				ints.add(Biome.getIdForBiome(gen));
		}
		return ints;
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
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
