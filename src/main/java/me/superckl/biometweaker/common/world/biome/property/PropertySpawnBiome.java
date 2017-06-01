package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.BiomeManager;

public class PropertySpawnBiome extends Property<Boolean>{

	private static boolean loggedSpawn;

	public PropertySpawnBiome() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalStateException, IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(val)
			BiomeManager.addSpawnBiome((Biome) obj);
		else{
			BiomeManager.removeSpawnBiome((Biome) obj);
			if(!PropertySpawnBiome.loggedSpawn && (BiomeProvider.allowedBiomes.size() == 0)){
				LogHelper.warn("Upon removal of biome "+Biome.getIdForBiome((Biome) obj)+" the allowed spawn list appears to be empty. If you aren't adding one later, this will cause a crash.");
				PropertySpawnBiome.loggedSpawn = true;
			}
		}
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		return BiomeProvider.allowedBiomes.contains(obj);
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isSettable() {
		return true;
	}

}
