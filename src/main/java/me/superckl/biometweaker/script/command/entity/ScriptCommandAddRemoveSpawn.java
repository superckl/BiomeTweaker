package me.superckl.biometweaker.script.command.entity;

import java.util.Collection;
import java.util.Iterator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveSpawn implements IScriptCommand{

	private final IBiomePackage pack;
	private final boolean remove;
	private final SpawnListType type;
	private final String entityClass;
	private final int weight, minCount, maxCount;

	@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addSpawn")
	@ParameterOverride(exceptionKey = "nonNegInt", parameterIndex = 3)
	@ParameterOverride(exceptionKey = "nonNegInt", parameterIndex = 4)
	@ParameterOverride(exceptionKey = "nonNegInt", parameterIndex = 5)
	public ScriptCommandAddRemoveSpawn(final IBiomePackage pack, final String entityClass, final SpawnListType type, final int weight, final int minCount, final int maxCount) {
		this(pack, false, type, entityClass, weight, minCount, maxCount);
	}

	@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeSpawn")
	public ScriptCommandAddRemoveSpawn(final IBiomePackage pack, final String entityClass, final SpawnListType type) {
		this(pack, true, type, entityClass, 0, 0, 0);
	}

	@Override
	public void perform() throws Exception {
		final Class<? extends Entity> clazz2 = EntityHelper.getEntityClass(this.entityClass);
		if(clazz2 == null)
			throw new IllegalArgumentException("Failed to find entity class: "+this.entityClass);
		if(!EntityLiving.class.isAssignableFrom(clazz2))
			throw new IllegalArgumentException("entity class "+this.entityClass+" is not assignable to EntityLiving. It cannot be spawned!");
		final Class<? extends EntityLiving> clazz = WarningHelper.uncheckedCast(clazz2);
		final SpawnListEntry entry = new SpawnListEntry(clazz, this.weight, this.minCount, this.maxCount);
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(this.remove && MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveSpawn(this, gen, this.type, clazz)))
				continue;
			else if(!this.remove && MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddSpawn(this, gen, entry)))
				continue;
			this.handleTypeSwitch(gen, entry, clazz);
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

	private void handleTypeSwitch(final Biome gen, final SpawnListEntry entry, final Class<?> clazz){
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
