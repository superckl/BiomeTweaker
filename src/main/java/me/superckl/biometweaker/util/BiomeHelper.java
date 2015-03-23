package me.superckl.biometweaker.util;

import me.superckl.biometweaker.config.Config;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BiomeHelper {

	public static JsonObject fillJsonObject(BiomeGenBase gen){
		JsonObject obj = new JsonObject();
		obj.addProperty("ID", gen.biomeID);
		obj.addProperty("Name", gen.biomeName);
		obj.addProperty("Foliage Color", gen.color);
		obj.addProperty("Root Height", gen.rootHeight);
		obj.addProperty("Height Variation", gen.heightVariation);
		obj.addProperty("Top Block", gen.topBlock.delegate.name());
		obj.addProperty("Filler Block", gen.fillerBlock.delegate.name());
		obj.addProperty("Temperature", gen.temperature);
		obj.addProperty("Humidity", gen.rainfall);
		obj.addProperty("Water Tint", gen.waterColorMultiplier);
		obj.addProperty("Enable Rain", gen.enableRain);
		obj.addProperty("Enable Snow", gen.enableSnow);
		JsonArray array = new JsonArray();
		for(Object entity:gen.spawnableCreatureList){
			SpawnListEntry entry = (SpawnListEntry) entity;
			JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Creatures", array);
		
		array = new JsonArray();
		for(Object entity:gen.spawnableMonsterList){
			SpawnListEntry entry = (SpawnListEntry) entity;
			JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Monsters", array);
		
		array = new JsonArray();
		for(Object entity:gen.spawnableWaterCreatureList){
			SpawnListEntry entry = (SpawnListEntry) entity;
			JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Water Creatures", array);
		
		array = new JsonArray();
		for(Object entity:gen.spawnableCaveCreatureList){
			SpawnListEntry entry = (SpawnListEntry) entity;
			JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Cave Creatures", array);
		
		obj.addProperty("Tweaked", Config.INSTANCE.getSubFiles().containsKey(gen.biomeID));
		
		return obj;
	}
	
}
