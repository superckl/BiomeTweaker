package me.superckl.biometweaker.script.command;

import java.util.Collection;
import java.util.Iterator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveSpawn implements IScriptCommand{

	public static enum Type{
		CREATURE, MONSTER, CAVE_CREATURE, WATER_CREATURE;
	}

	private final int biomeID;
	private final boolean remove;
	private final Type type;
	private final String entityClass;
	private final int weight, minCount, maxCount;

	public ScriptCommandAddRemoveSpawn(final int biomeID, final Type type, final String entityClass, final int weight, final int minCount, final int maxCount) {
		this(biomeID, false, type, entityClass, weight, minCount, maxCount);
	}

	public ScriptCommandAddRemoveSpawn(final int biomeID, final Type type, final String entityClass) {
		this(biomeID, true, type, entityClass, 0, 0, 0);
	}

	@Override
	public void perform() throws Exception {
		final Class<?> clazz = Class.forName(this.entityClass);
		if(clazz == null){
			LogHelper.info("Failed to load entity class: "+this.entityClass);
			return;
		}
		final SpawnListEntry entry = new SpawnListEntry(clazz, this.weight, this.minCount, this.maxCount);
		if(this.biomeID == -1){
			for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
				if(gen != null){
					this.handleTypeSwitch(gen, entry, clazz);
					Config.INSTANCE.onTweak(gen.biomeID);
				}
			return;
		}
		final BiomeGenBase gen = BiomeGenBase.getBiome(this.biomeID);
		if(gen == null){
			LogHelper.info("Error applying tweaks. Biome ID "+this.biomeID+" does not correspond to a biome! Check the output files for the correct ID!");
			return;
		}
		this.handleTypeSwitch(gen, entry, clazz);
		Config.INSTANCE.onTweak(this.biomeID);
	}

	private void handleTypeSwitch(final BiomeGenBase gen, final SpawnListEntry entry, final Class<?> clazz){
		switch(this.type){
		case CAVE_CREATURE:{
			if(this.remove)
				this.removeEntry(clazz, gen.spawnableCaveCreatureList);
			else
				gen.spawnableCaveCreatureList.add(entry);
			break;
		}
		case CREATURE:{
			if(this.remove)
				this.removeEntry(clazz, gen.spawnableCreatureList);
			else
				gen.spawnableCreatureList.add(entry);
			break;
		}
		case MONSTER:{
			if(this.remove)
				this.removeEntry(clazz, gen.spawnableMonsterList);
			else
				gen.spawnableMonsterList.add(entry);
			break;
		}
		case WATER_CREATURE:{
			if(this.remove)
				this.removeEntry(clazz, gen.spawnableWaterCreatureList);
			else
				gen.spawnableWaterCreatureList.add(entry);
			break;
		}
		default:
			break;
		}
	}

	private void removeEntry(final Class<?> clazz, final Collection<SpawnListEntry> coll){
		final Iterator<SpawnListEntry> it = coll.iterator();
		while(it.hasNext())
			if(it.next().entityClass.getName().equals(clazz.getName()))
				it.remove();
	}

}
