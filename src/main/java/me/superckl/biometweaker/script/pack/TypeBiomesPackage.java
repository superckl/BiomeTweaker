package me.superckl.biometweaker.script.pack;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class TypeBiomesPackage implements IBiomePackage{

	private final String[] sTypes;
	private BiomeDictionary.Type[] types;

	public TypeBiomesPackage(final String ... types) {
		this.sTypes = types;
	}

	@Override
	public Iterator<BiomeGenBase> getIterator() {
		this.checkTypes();
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
		this.checkTypes();
		final List<Integer> ints = Lists.newArrayList();
		for(final BiomeDictionary.Type type:this.types){
			final BiomeGenBase[] gens = BiomeDictionary.getBiomesForType(type);
			for(final BiomeGenBase gen:gens)
				ints.add(gen.biomeID);
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

	@Override
	public List<Integer> getMergeIDExclusions() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Iterator<BiomeGenBase> getExclusionsIterator() {
		return Iterators.emptyIterator();
	}

}
