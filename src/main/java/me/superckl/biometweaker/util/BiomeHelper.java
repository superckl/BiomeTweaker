package me.superckl.biometweaker.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.config.Config;
import net.minecraft.block.Block;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;

public class BiomeHelper {

	//private static Field actualFillerBlock;
	//private static Field liquidFillerBlock;
	private static Field grassColor;
	private static Field foliageColor;
	private static Field waterColor;
	private static Field fillerBlockMeta;

	private static Field biomeList;
	private static Field typeInfoList;
	private static Field typeList;
	private static Field biomes;
	private static Field isModded;

	public static JsonObject fillJsonObject(final BiomeGenBase gen, final int ... coords){
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
			//obj.addProperty("Actual Filler Block", ((Block) BiomeHelper.actualFillerBlock.get(gen)).delegate.name());
			//obj.addProperty("Liquid Filler Block", ((Block) BiomeHelper.liquidFillerBlock.get(gen)).delegate.name());
			final boolean hasCoords = (coords != null) && (coords.length == 3);
			int x = 0, y = 0, z = 0;
			if(hasCoords){
				x = coords[0];
				y = coords[1];
				z = coords[2];
			}
			obj.addProperty("Grass Color", ""+(hasCoords ? gen.getBiomeGrassColor(x, y, z):(i = BiomeHelper.grassColor.getInt(gen)) == -1 ? "Not set. Check in-game.":i));
			obj.addProperty("Foliage Color", ""+(hasCoords ? gen.getBiomeFoliageColor(x, y, z):(i = BiomeHelper.foliageColor.getInt(gen)) == -1 ? "Not set. Check in-game.":i));
			obj.addProperty("Water Color", ""+gen.getWaterColorMultiplier());
		} catch (final Exception e) {
			LogHelper.error("Failed to retrieve inserted fields!");
			e.printStackTrace();
		}
		obj.addProperty("Temperature", gen.temperature);
		obj.addProperty("Humidity", gen.rainfall);
		obj.addProperty("Water Tint", gen.waterColorMultiplier);
		obj.addProperty("Enable Rain", gen.enableRain);
		obj.addProperty("Enable Snow", gen.enableSnow);
		JsonArray array = new JsonArray();
		for(final Type type: BiomeDictionary.getTypesForBiome(gen))
			array.add(new JsonPrimitive(type.toString()));
		obj.add("Dictionary Types", array);

		final JsonObject managerWeights = new JsonObject();
		for(final BiomeManager.BiomeType type:BiomeManager.BiomeType.values()){
			final JsonArray subArray = new JsonArray();
			final List<BiomeEntry> entries = BiomeManager.getBiomes(type);
			for(final BiomeEntry entry:entries)
				if(entry.biome == gen)
					subArray.add(new JsonPrimitive(entry.itemWeight));
			if(subArray.size() > 0)
				managerWeights.add(type.name()+" Weights", subArray);
		}
		obj.add("BiomeManager Entries", managerWeights);

		array = new JsonArray();
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

	private static Set<BiomeManager.BiomeType> logged = EnumSet.noneOf(BiomeManager.BiomeType.class);
	private static boolean loggedSpawn;

	public static void setBiomeProperty(final String prop, final JsonElement value, final BiomeGenBase biome) throws Exception{
		BiomeHelper.checkFields();
		if(prop.equals("name")){
			final String toSet = (String) ParameterTypes.STRING.tryParse(value.getAsString());
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
			final String blockName = (String) ParameterTypes.STRING.tryParse(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				biome.topBlock = block;
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("fillerBlock")){
			final String blockName = (String) ParameterTypes.STRING.tryParse(value.getAsString());
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
		}/*else if(prop.equals("actualFillerBlock")){
			final String blockName = (String) ParameterTypes.STRING.tryParse(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				BiomeHelper.actualFillerBlock.set(biome, block);
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}
		}else if(prop.equals("liquidFillerBlock")){
			final String blockName = (String) ParameterTypes.STRING.tryParse(value.getAsString());
			try {
				final Block block = Block.getBlockFromName(blockName);
				BiomeHelper.liquidFillerBlock.set(biome, block);
			} catch (final Exception e) {
				LogHelper.info("Failed to parse block: "+blockName);
			}

		}*/else if(prop.equals("grassColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.grassColor.set(biome, toSet);
		}else if(prop.equals("foliageColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.foliageColor.set(biome, toSet);
		}else if(prop.equals("waterColor")){
			final int toSet = value.getAsInt();
			BiomeHelper.waterColor.set(biome, toSet);
		}else if(prop.equals("fillerBlockMeta")){
			final byte toSet = value.getAsByte();
			BiomeHelper.fillerBlockMeta.set(biome, toSet);
		}else if(prop.equals("topBlockMeta"))
			biome.field_150604_aj = value.getAsInt();
		else if(prop.equals("waterliliesPerChunk"))
			BiomeEventHandler.getWaterlilyPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("treesPerChunk"))
			BiomeEventHandler.getTreesPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("flowersPerChunk"))
			BiomeEventHandler.getFlowersPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("grassPerChunk"))
			BiomeEventHandler.getGrassPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("deadbushesPerChunk"))
			BiomeEventHandler.getDeadBushPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("mushroomsPerChunk"))
			BiomeEventHandler.getMushroomPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("reedsPerChunk"))
			BiomeEventHandler.getReedsPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("cactiPerChunk"))
			BiomeEventHandler.getCactiPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("sandPerChunk"))
			BiomeEventHandler.getSandPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("clayPerChunk"))
			BiomeEventHandler.getClayPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("bigMushroomsPerChunk"))
			BiomeEventHandler.getBigMushroomsPerChunk().put(biome.biomeID, value.getAsInt());
		else if(prop.equals("contiguousReplacement"))
			BiomeEventHandler.getContigReplaces()[biome.biomeID] = value.getAsBoolean();
		else if(prop.equals("genWeight")){
			final int weight  = value.getAsInt();
			for(final BiomeType type:BiomeType.values()){
				final List<BiomeEntry> entries = BiomeManager.getBiomes(type);
				for(final BiomeEntry entry:entries)
					if(entry.biome.biomeID == biome.biomeID)
						entry.itemWeight = weight;
				if((type != BiomeManager.BiomeType.DESERT) && !BiomeHelper.logged.contains(type) && (WeightedRandom.getTotalWeight(entries) <= 0)){
					LogHelper.warn("Sum of biome generation weights for type "+type+" is zero! This will cause Vanilla generation to crash! You have been warned!");
					BiomeHelper.logged.add(type);
				}
			}
			BiomeHelper.modTypeLists();
		}else if(prop.equals("genVillages"))
			if(value.getAsBoolean())
				BiomeManager.addVillageBiome(biome, true);
			else
				BiomeManager.removeVillageBiome(biome);
		else if(prop.equals("genStrongholds"))
			if(value.getAsBoolean())
				BiomeManager.removeStrongholdBiome(biome);
			else
				BiomeManager.addStrongholdBiome(biome);
		else if(prop.equals("isSpawnBiome"))
			if(value.getAsBoolean())
				BiomeManager.addSpawnBiome(biome);
			else{
				BiomeManager.removeSpawnBiome(biome);
				if(!BiomeHelper.loggedSpawn && (WorldChunkManager.allowedBiomes.size() == 0)){
					LogHelper.warn("Upon removal of biome "+biome.biomeID+" the allowed spawn list appears to be empty. If you aren't adding one later, this will cause a crash.");
					BiomeHelper.loggedSpawn = true;
				}
			}
		else
			LogHelper.warn("Attempted to set property "+prop+" but corresponding property was not found for biomes. Value: "+value.getAsString());
	}

	private static void checkFields(){
		try{
			/*if(BiomeHelper.actualFillerBlock == null)
				BiomeHelper.actualFillerBlock = BiomeGenBase.class.getDeclaredField("actualFillerBlock");*/
			/*if(BiomeHelper.liquidFillerBlock == null)
				BiomeHelper.liquidFillerBlock = BiomeGenBase.class.getDeclaredField("liquidFillerBlock");*/
			if(BiomeHelper.grassColor == null)
				BiomeHelper.grassColor = BiomeGenBase.class.getDeclaredField("grassColor");
			if(BiomeHelper.foliageColor == null)
				BiomeHelper.foliageColor = BiomeGenBase.class.getDeclaredField("foliageColor");
			if(BiomeHelper.waterColor == null)
				BiomeHelper.waterColor = BiomeGenBase.class.getDeclaredField("waterColor");
			if(BiomeHelper.fillerBlockMeta == null)
				BiomeHelper.fillerBlockMeta = BiomeGenBase.class.getDeclaredField("fillerBlockMeta");
			if(BiomeHelper.biomeList == null){
				BiomeHelper.biomeList = BiomeDictionary.class.getDeclaredField("biomeList");
				BiomeHelper.biomeList.setAccessible(true);
			}
			if(BiomeHelper.typeInfoList == null){
				BiomeHelper.typeInfoList = BiomeDictionary.class.getDeclaredField("typeInfoList");
				BiomeHelper.typeInfoList.setAccessible(true);
			}
			if(BiomeHelper.biomes == null){
				BiomeHelper.biomes = BiomeManager.class.getDeclaredField("biomes");
				BiomeHelper.biomes.setAccessible(true);
			}
		}catch(final Exception e){
			LogHelper.error("Failed to find inserted fields!");
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

	public static void modifyBiomeDicType(final BiomeGenBase gen, final BiomeDictionary.Type type, final boolean remove) throws Exception{
		BiomeHelper.checkFields();
		if(gen == null)
			return;
		final List<BiomeGenBase>[] listArray = (List<BiomeGenBase>[]) BiomeHelper.typeInfoList.get(null);
		if(listArray.length > type.ordinal()){
			final List<BiomeGenBase> list = listArray[type.ordinal()];
			if(remove)
				list.remove(gen);
			else if(!list.contains(gen))
				list.add(gen);
		}
		//Okay, here we go. REFLECTION OVERLOAD!!!1! (It's really not that bad.)
		final Object array = BiomeHelper.biomeList.get(null);
		final Object biomeInfo = Array.get(array, gen.biomeID);
		if(BiomeHelper.typeList == null){
			BiomeHelper.typeList = biomeInfo.getClass().getDeclaredField("typeList");
			BiomeHelper.typeList.setAccessible(true);
		}
		final EnumSet<BiomeDictionary.Type> set = (EnumSet<Type>) BiomeHelper.typeList.get(biomeInfo);
		if(remove)
			set.remove(type);
		else if(!set.contains(type)){
			final EnumSet<BiomeDictionary.Type> set2 = EnumSet.noneOf(BiomeDictionary.Type.class);
			set2.addAll(set);
			set2.add(type);
			BiomeHelper.typeList.set(biomeInfo, set2);
		}
	}

	public static void removeAllBiomeDicType(final BiomeGenBase gen) throws Exception{
		BiomeHelper.checkFields();
		if(gen == null)
			return;
		final Object array = BiomeHelper.biomeList.get(null);
		final Object biomeInfo = Array.get(array, gen.biomeID);
		if(BiomeHelper.typeList == null){
			BiomeHelper.typeList = biomeInfo.getClass().getDeclaredField("typeList");
			BiomeHelper.typeList.setAccessible(true);
		}
		final EnumSet<BiomeDictionary.Type> set = (EnumSet<Type>) BiomeHelper.typeList.get(biomeInfo);
		final List<BiomeGenBase>[] listArray = (List<BiomeGenBase>[]) BiomeHelper.typeInfoList.get(null);
		for(final BiomeDictionary.Type type : set)
			listArray[type.ordinal()].remove(gen);
		set.clear();
	}

	private static boolean hasModded;

	public static void modTypeLists() throws Exception{
		BiomeHelper.checkFields();
		if(BiomeHelper.hasModded)
			return;
		//LogHelper.info("Setting TrackedLists to modded...");
		final Object array = BiomeHelper.biomes.get(null);
		final int length = Array.getLength(array);
		for(int i = 0; i < length; i++){
			final Object list = Array.get(array, i);
			if(BiomeHelper.isModded == null){
				BiomeHelper.isModded = list.getClass().getDeclaredField("isModded");
				BiomeHelper.isModded.setAccessible(true);
			}
			BiomeHelper.isModded.setBoolean(list, true);
		}
		BiomeHelper.hasModded = true;
	}

}
