package me.superckl.biometweaker.script.command.entity;

import java.util.Iterator;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setMaxSpawnPackSize")
@RequiredArgsConstructor
public class ScriptCommandMaxSpawnPackSize extends ScriptCommand{

	private final BiomePackage pack;
	private final String entityClass;
	private final int size;

	public ScriptCommandMaxSpawnPackSize(final BiomePackage pack, final int size) {
		this(pack, null, size);
	}

	@Override
	public void perform() throws Exception {
		if(this.entityClass == null){
			EntityEventHandler.setGlobalPackSize(this.size);
			return;
		}
		Class<?> clazz;
		try{
			clazz = Class.forName(this.entityClass);
		}catch(final Exception e){
			throw new IllegalArgumentException("Failed to load entity class: "+this.entityClass, e);
		}
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final TIntObjectMap<TObjectIntMap<String>>  map = EntityEventHandler.getPackSizes();
			if(!map.containsKey(Biome.getIdForBiome(biome)))
				map.put(Biome.getIdForBiome(biome), new TObjectIntHashMap<String>());
			map.get(Biome.getIdForBiome(biome)).put(clazz.getName(), this.size);
		}
	}

}
