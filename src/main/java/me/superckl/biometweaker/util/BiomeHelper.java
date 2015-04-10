package me.superckl.biometweaker.util;

import java.lang.reflect.Field;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.ScriptParser;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BiomeHelper {

	private static Field actualFillerBlock;
	private static Field liquidFillerBlock;
	private static Field grassColor;
	private static Field foliageColor;
	private static Field waterColor;

	public static JsonObject fillJsonObject(final BiomeGenBase gen){
		BiomeHelper.checkFields();
		final JsonObject obj = new JsonObject();
		obj.addProperty("ID", gen.biomeID);
		obj.addProperty("Name", gen.biomeName);
		obj.addProperty("Class", gen.getClass().getName());
		obj.addProperty("Color", gen.color);
		obj.addProperty("Root Height", gen.rootHeight);
		obj.addProperty("Height Variation", gen.heightVariation);
		obj.addProperty("Top Block", gen.topBlock.delegate.name());
		obj.addProperty("Filler Block", gen.fillerBlock.delegate.name());
		try {
			int i = -1;
			obj.addProperty("Actual Filler Block", ((Block) BiomeHelper.actualFillerBlock.get(gen)).delegate.name());
			obj.addProperty("Liquid Filler Block", ((Block) BiomeHelper.liquidFillerBlock.get(gen)).delegate.name());
			obj.addProperty("Grass Color", ""+((i = BiomeHelper.grassColor.getInt(gen)) == -1 ? "Not Set":i));
			obj.addProperty("Foliage Color", ""+((i = BiomeHelper.foliageColor.getInt(gen)) == -1 ? "Not Set":i));
			obj.addProperty("Water Color", ""+((i = BiomeHelper.waterColor.getInt(gen)) == -1 ? "Not Set":i));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		obj.addProperty("Tweaked", Config.INSTANCE.getTweakedBiomes().contains(-1) || Config.INSTANCE.getTweakedBiomes().contains(gen.biomeID));

		return obj;
	}



	public static void setBiomeProperty(final String prop, final JsonElement value, final BiomeGenBase biome) throws Exception{
		BiomeHelper.checkFields();
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
		}else if(prop.equals("actualFillerBlock")){
			final String blockName = ScriptParser.extractStringArg(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				BiomeHelper.actualFillerBlock.set(biome, block);
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("liquidFillerBlock")){
			final String blockName = ScriptParser.extractStringArg(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				BiomeHelper.liquidFillerBlock.set(biome, block);
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("grassColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.grassColor.set(biome, toSet);
		}else if(prop.equals("foliageColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.foliageColor.set(biome, toSet);
		}else if(prop.equals("waterColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.waterColor.set(biome, toSet);
		}
	}

	private static void checkFields(){
		try{
			if(BiomeHelper.actualFillerBlock == null)
				BiomeHelper.actualFillerBlock = BiomeGenBase.class.getDeclaredField("actualFillerBlock");
			if(BiomeHelper.liquidFillerBlock == null)
				BiomeHelper.liquidFillerBlock = BiomeGenBase.class.getDeclaredField("liquidFillerBlock");
			if(BiomeHelper.grassColor == null)
				BiomeHelper.grassColor = BiomeGenBase.class.getDeclaredField("grassColor");
			if(BiomeHelper.foliageColor == null)
				BiomeHelper.foliageColor = BiomeGenBase.class.getDeclaredField("foliageColor");
			if(BiomeHelper.waterColor == null)
				BiomeHelper.waterColor = BiomeGenBase.class.getDeclaredField("waterColor");
		}catch(final Exception e){
			LogHelper.info("Failed to find inserted fields!");
			e.printStackTrace();
		}
	}

	public static int callGrassColorEvent(final int color, final BiomeGenBase gen){
		final GetGrassColor e = new GetGrassColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.newColor;
	}

	public static int callFoliageColorEvent(final int color, final BiomeGenBase gen){
		final GetFoliageColor e = new GetFoliageColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.newColor;
	}

	public static int callWaterColorEvent(final int color, final BiomeGenBase gen){
		final GetWaterColor e = new GetWaterColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.newColor;
	}

}
