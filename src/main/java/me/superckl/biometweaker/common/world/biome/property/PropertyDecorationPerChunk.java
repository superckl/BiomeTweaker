package me.superckl.biometweaker.common.world.biome.property;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class PropertyDecorationPerChunk extends Property<Integer> {

	private final EventType type;

	public PropertyDecorationPerChunk(final EventType type) {
		super(Integer.class);
		this.type = type;
	}

	@Override
	public void set(final Object obj, final Integer val) throws IllegalStateException, IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(!BiomeEventHandler.getDecorationsPerChunk().containsKey(this.type))
			BiomeEventHandler.getDecorationsPerChunk().put(this.type, new TIntIntHashMap());
		BiomeEventHandler.getDecorationsPerChunk().get(this.type).put(Biome.getIdForBiome((Biome) obj), val);
	}

	@Override
	public Integer get(final Object obj) throws IllegalStateException, IllegalArgumentException {
		if(!(obj instanceof Biome))
			throw new IllegalArgumentException("Passed object is not an instance of Biome!");
		if(!BiomeEventHandler.getDecorationsPerChunk().containsKey(this.type))
			throw new IllegalStateException("Decoration "+this.type.name()+" has not been set for biome" + ((Biome) obj).getBiomeName() + "!");
		final int id = Biome.getIdForBiome((Biome) obj);
		final TIntIntMap map = BiomeEventHandler.getDecorationsPerChunk().get(this.type);
		if(!map.containsKey(id))
			throw new IllegalStateException("Decoration "+this.type.name()+" has not been set for biome" + ((Biome) obj).getBiomeName() + "!");
		return map.get(id);
	}

}
