package me.superckl.biometweaker.common.world.biome.property;

import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import net.minecraft.world.biome.Biome;

public class PropertyContigReplace extends Property<Boolean>{

	public PropertyContigReplace() {
		super(Boolean.class);
	}

	@Override
	public void set(final Object obj, final Boolean val) throws IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(TweakWorldManager.getCurrentWorld() == null)
			BlockReplacementManager.setGlobalContiguousReplacement(Biome.getIdForBiome((Biome) obj), val);
		else
			BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).setContiguousReplacement(Biome.getIdForBiome((Biome) obj), val);
	}

	@Override
	public Boolean get(final Object obj) throws IllegalStateException, IllegalArgumentException, UnsupportedOperationException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(TweakWorldManager.getCurrentWorld() == null)
			return BlockReplacementManager.isGlobalContiguousReplacement(Biome.getIdForBiome((Biome) obj));
		else
			return BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).isContiguousReplacement(Biome.getIdForBiome((Biome) obj));
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
