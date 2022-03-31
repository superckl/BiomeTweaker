package me.superckl.api.biometweaker.script.pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class PropertyRangeBiomePackage extends BiomePackage{

	private final Property<? extends Number> property;
	private final float lowBound;
	private final float highBound;

	public PropertyRangeBiomePackage(final String property, final float lowBound, final float highBound) {
		final Property<?> prop = BiomePropertyManager.findProperty(property);
		if(prop == null)
			throw new IllegalArgumentException("No property found for name "+property);
		if(!Number.class.isAssignableFrom(prop.getTypeClass()))
			throw new IllegalArgumentException("Property "+property+" is not a numeric value!");
		this.property = WarningHelper.uncheckedCast(prop);
		this.lowBound = lowBound;
		this.highBound = highBound;
	}

	@Override
	public Iterator<Biome> iterator() {
		final List<Biome> biomes = new ArrayList<>();
		final Iterator<Biome> it = ForgeRegistries.BIOMES.iterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final float value = this.property.get(biome).floatValue();
			if(this.lowBound <= value && value <= this.highBound)
				biomes.add(biome);
		}
		return biomes.iterator();
	}
}
