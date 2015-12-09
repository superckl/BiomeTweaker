package me.superckl.biometweaker.script.command;

import java.util.Collection;
import java.util.Iterator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveSpawn implements IScriptCommand{

	private final IBiomePackage pack;
	private final boolean remove;
	private final SpawnListType type;
	private final String entityClass;
	private final int weight, minCount, maxCount;

	public ScriptCommandAddRemoveSpawn(final IBiomePackage pack, final String entityClass, final SpawnListType type, final int weight, final int minCount, final int maxCount) {
		this(pack, false, type, entityClass, weight, minCount, maxCount);
	}

	public ScriptCommandAddRemoveSpawn(final IBiomePackage pack, final String entityClass, final SpawnListType type) {
		this(pack, true, type, entityClass, 0, 0, 0);
	}

	@Override
	public void perform() throws Exception {
		Class<? extends EntityLiving> clazz;
		try{
			clazz = (Class<? extends EntityLiving>) Class.forName(this.entityClass);
		}catch(final Exception e){
			throw new IllegalArgumentException("Failed to load entity class: "+this.entityClass, e);
		}
		final SpawnListEntry entry = new SpawnListEntry(clazz, this.weight, this.minCount, this.maxCount);
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(this.remove && MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveSpawn(this, gen, this.type, clazz)))
				continue;
			else if(!this.remove && MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddSpawn(this, gen, entry)))
				continue;
			this.handleTypeSwitch(gen, entry, clazz);
			Config.INSTANCE.onTweak(gen.biomeID);
		}
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
