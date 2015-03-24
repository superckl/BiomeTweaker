package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import net.minecraft.world.biome.BiomeGenBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.config.ParsedBiomeEntry;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.proxy.IProxy;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION)
public class BiomeTweaker {
	
	@Instance(ModData.MOD_ID)
	@Getter
	private static BiomeTweaker instance;
	
	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static IProxy proxy;
	
	@EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent e) throws IOException{
		LogHelper.info("Found tweaks for "+Config.INSTANCE.getParsedEntries().size()+" biomes. Applying tweaks...");
		
		for(ParsedBiomeEntry entry:Config.INSTANCE.getParsedEntries().values()){
			if(entry.getBiomeID() == -1){
				for(BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
					if(gen == null)
						continue;
					for(Entry<String, JsonElement> setting:entry.getMappings().entrySet())
						try {
							BiomeHelper.setBiomeProperty(setting.getKey(), setting.getValue(), gen);
						} catch (Exception e1) {
							LogHelper.error("Failed to set biome property "+setting.getKey()+" to value "+setting.getValue().toString());
							e1.printStackTrace();
						}
				}
				continue;
			}
			BiomeGenBase gen = BiomeGenBase.getBiome(entry.getBiomeID());
			if(gen == null){
				LogHelper.info("Error applying tweaks. Biome ID "+entry.getBiomeID()+" does not correspond to a biome! Check the output files for the correct ID!");
				continue;
			}
			for(Entry<String, JsonElement> setting:entry.getMappings().entrySet())
				try {
					BiomeHelper.setBiomeProperty(setting.getKey(), setting.getValue(), gen);
				} catch (Exception e1) {
					LogHelper.error("Failed to set biome property "+setting.getKey()+" to value "+setting.getValue().toString());
					e1.printStackTrace();
				}
		}
		LogHelper.info("Successfully applied tweaks. Generating biome status report...");
		JsonArray array = new JsonArray();
		for(BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
			if(gen == null)
				continue;
			array.add(BiomeHelper.fillJsonObject(gen));
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File dir = new File(BiomeTweakerCore.mcLocation, "/config/BiomeTweaker/output/");
		dir.mkdirs();
		for(File file:dir.listFiles())
			file.delete();
		if(Config.INSTANCE.isOutputSeperateFiles()){
			for(JsonElement element:array){
				JsonObject obj = (JsonObject) element;
				File output = new File(dir, ""+obj.get("Name").getAsString()+".json");
				if(output.exists())
					output.delete();
				output.createNewFile();
				@Cleanup
				BufferedWriter writer = new BufferedWriter(new FileWriter(output));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		}else{
			File output = new File(dir, "BiomeTweaker - Biome Status Report.json");
			if(output.exists())
				output.delete();
			output.createNewFile();
			@Cleanup
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(array));
		}
	}
	
}
