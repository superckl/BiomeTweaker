package me.superckl.api.biometweaker.script.pack;

import java.util.Iterator;
import java.util.Optional;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import me.superckl.api.biometweaker.BiomeLookup;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class PropertyRangeBiomePackage extends BiomePackage{

	private final Property<? extends Number, Biome, ? super BiomeLookup> property;
	private final float lowBound;
	private final float highBound;

	public PropertyRangeBiomePackage(final String property, final float lowBound, final float highBound) {
		final Property<?, ?, ?> prop = BiomePropertyManager.findProperty(property);
		if(prop == null)
			throw new IllegalArgumentException("No property found for name "+property);
		if(!Number.class.isAssignableFrom(prop.getTypeClass()))
			throw new IllegalArgumentException("Property "+property+" is not a numeric value!");
		this.property = WarningHelper.uncheckedCast(prop);
		this.lowBound = lowBound;
		this.highBound = highBound;
	}

	@Override
	public Iterator<Biome> iterator(final BiomeLookup lookup) {
		final Predicate<Biome> inRange = biome -> {
			final float value = this.property.get(biome, Optional.of(lookup)).floatValue();
			if(this.lowBound <= value && value <= this.highBound)
				return true;
			return false;
		};
		return Iterators.filter(lookup.allValues(), inRange);
	}

	@Override
	public Iterator<ResourceLocation> locIterator(final BiomeLookup lookup) {
		return Iterators.transform(this.iterator(lookup), lookup::key);
	}

	@Override
	public boolean requiresRegistry() {
		return true;
	}
}
