package me.superckl.biometweaker.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Getter
public class Config {

	public static final Config INSTANCE = new Config();
	
	private final Map<Integer, ParsedBiomeEntry> subFiles = new HashMap<Integer, ParsedBiomeEntry>();
	private boolean outputSeperateFiles;
	
	private Config() {}
	
	@SneakyThrows
	public void init(File whereAreWe, JsonObject obj){
		Logger log = ModBiomeTweakerCore.logger;
		if(obj.has("include")){
			JsonElement element = obj.get("include");
			if(element.isJsonArray()){
				JsonArray array = (JsonArray) element;
				for(JsonElement listElement:array){
					String item = listElement.getAsString();
					File subFile = new File(whereAreWe, item);
					if(!subFile.exists()){
						log.debug("Included subfile not found. A blank one will be generated.");
						subFile.createNewFile();
						JsonArray array1 = new JsonArray();
						@Cleanup
						BufferedWriter writer = new BufferedWriter(new FileWriter(subFile));
						writer.write(array1.toString());
					}
					@Cleanup
					BufferedReader reader = new BufferedReader(new FileReader(subFile));
					JsonArray object = (JsonArray) new JsonParser().parse(reader);
					for(JsonElement element2:object){
						if(!element2.isJsonArray())
							continue;
						ParsedBiomeEntry biomeEntry = this.parseEntry((JsonArray) element2);
						if(subFiles.containsKey(biomeEntry.getBiomeID()))
							subFiles.get(biomeEntry.getBiomeID()).overwriteWith(biomeEntry);
						else
							subFiles.put(biomeEntry.getBiomeID(), biomeEntry);
					}
				}
			}else{
				log.warn("Failed to parse include array! Check your formatting!");
			}
		}
		if(obj.has("separate files"))
			this.outputSeperateFiles = obj.get("separate files").getAsBoolean();
	}
	
	private ParsedBiomeEntry parseEntry(JsonArray obj){
		return null;
	}
	
}
