package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;

public class PropertyOceanic extends Property<Boolean>{

	public PropertyOceanic() {
		super(Boolean.class);
	}

	@Override
	public void set(Object obj, Boolean val) {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if (val && !this.get(obj)) {
			BiomeManager.oceanBiomes.add((Biome) obj);
		} else if (!val && this.get(obj)) {
			BiomeManager.oceanBiomes.remove(obj);
		}
			
	}

	@Override
	public Boolean get(Object obj) {
		return BiomeManager.oceanBiomes.contains(obj);
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
	public Class<?> getTargetClass() {
		return Biome.class;
	}

}
