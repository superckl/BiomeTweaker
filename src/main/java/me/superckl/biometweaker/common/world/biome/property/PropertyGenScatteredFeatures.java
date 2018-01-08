package me.superckl.biometweaker.common.world.biome.property;

import java.util.List;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.property.Property;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;

public class PropertyGenScatteredFeatures extends Property<Boolean>{

	public PropertyGenScatteredFeatures() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		final List<Biome> biomes = Lists.newArrayList();
		biomes.addAll(MapGenScatteredFeature.BIOMELIST);
		if(val && !biomes.contains(obj))
			biomes.add((Biome) obj);
		else if(!val)
			biomes.remove(obj);
		MapGenScatteredFeature.BIOMELIST = biomes;
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		return MapGenScatteredFeature.BIOMELIST.contains(obj);
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

	@Override
	public boolean isCopyable() {
		//Return false here because biomes are hardcoded in the generator. Will always be a jungle temple unless it is one of the default biomes.
		//Requires the user to manually set it to what they want so they're aware of the weird functionality
		return false;
	}

}
