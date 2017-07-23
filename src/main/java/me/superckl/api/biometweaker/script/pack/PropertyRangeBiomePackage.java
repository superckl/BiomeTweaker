package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.world.biome.Biome;

public class PropertyRangeBiomePackage extends BiomePackage{

	private final Property<? extends Number> property;
	private final float lowBound;
	private final float highBound;

	public PropertyRangeBiomePackage(final String property, final float lowBound, final float highBound) {
		final Property<?> prop = BiomePropertyManager.propertyMap.get(property);
		if(prop == null)
			throw new IllegalArgumentException("No property found for name "+property);
		if(!Number.class.isAssignableFrom(prop.getTypeClass()))
			throw new IllegalArgumentException("Property "+property+" is not a numeric value!");
		this.property = WarningHelper.uncheckedCast(prop);
		this.lowBound = lowBound;
		this.highBound = highBound;
	}

	@Override
	public Iterator<Biome> getIterator() {
		final List<Biome> biomes = Lists.newArrayList();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final float value = this.property.get(biome).floatValue();
			if(this.lowBound <= value && value <= this.highBound)
				biomes.add(biome);
		}
		return biomes.iterator();
	}

	@Override
	public boolean supportsEarlyRawIds() {
		return false;
	}

	@Override
	public List<Integer> getRawIds() {
		final List<Integer> biomes = Lists.newArrayList();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final float value = this.property.get(biome).floatValue();
			if(this.lowBound <= value && value <= this.highBound)
				biomes.add(Biome.getIdForBiome(biome));
		}
		return biomes;
	}
}
