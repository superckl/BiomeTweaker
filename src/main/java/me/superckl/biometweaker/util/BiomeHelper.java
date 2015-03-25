package me.superckl.biometweaker.util;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.ScriptParser;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BiomeHelper {

	public static JsonObject fillJsonObject(final BiomeGenBase gen){
		final JsonObject obj = new JsonObject();
		obj.addProperty("ID", gen.biomeID);
		obj.addProperty("Name", gen.biomeName);
		obj.addProperty("Color", gen.color);
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
		for(final Object entity:gen.spawnableCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Creatures", array);

		array = new JsonArray();
		for(final Object entity:gen.spawnableMonsterList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Monsters", array);

		array = new JsonArray();
		for(final Object entity:gen.spawnableWaterCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Water Creatures", array);

		array = new JsonArray();
		for(final Object entity:gen.spawnableCaveCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Cave Creatures", array);

		//TODO
		obj.addProperty("Tweaked", Config.INSTANCE.getTweakedBiomes().contains(-1) || Config.INSTANCE.getTweakedBiomes().contains(gen.biomeID));

		return obj;
	}

	public static void setBiomeProperty(final String prop, final JsonElement value, final BiomeGenBase biome) throws Exception{
		if(prop.equals("name")){
			final String toSet = ScriptParser.extractStringArg(value.getAsString());
			biome.biomeName = toSet;
		}else if(prop.equals("color")){
			final int toSet = value.getAsInt();
			biome.color = toSet;
		}else if(prop.equals("height")){
			final float toSet = value.getAsFloat();
			biome.rootHeight = toSet;
		}else if(prop.equals("heightVariation")){
			final float toSet = value.getAsFloat();
			biome.heightVariation = toSet;
		}else if(prop.equals("topBlock")){
			final String blockName = ScriptParser.extractStringArg(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				biome.topBlock = block;
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("fillerBlock")){
			final String blockName = ScriptParser.extractStringArg(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				biome.fillerBlock = block;
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("temperature")){
			final float toSet = value.getAsFloat();
			biome.temperature = toSet;
		}else if(prop.equals("humidity")){
			final float toSet = value.getAsFloat();
			biome.rainfall = toSet;
		}else if(prop.equals("waterTint")){
			final int toSet = value.getAsInt();
			biome.waterColorMultiplier = toSet;
		}else if(prop.equals("enableRain")){
			final boolean toSet = value.getAsBoolean();
			biome.enableRain = toSet;
		}else if(prop.equals("enableSnow")){
			final boolean toSet = value.getAsBoolean();
			biome.enableSnow = toSet;
		}
	}

}
