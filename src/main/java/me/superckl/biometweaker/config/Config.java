package me.superckl.biometweaker.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.SneakyThrows;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptParser;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Getter
public class Config {

	public static final Config INSTANCE = new Config();
	
	private final Map<Integer, ParsedBiomeEntry> parsedEntries = new LinkedHashMap<Integer, ParsedBiomeEntry>();
	private boolean outputSeperateFiles;
	
	private Config() {}
	
	@SneakyThrows
	public void init(File whereAreWe, JsonObject obj){
		Logger log = ModBiomeTweakerCore.logger;
		if(obj.has("separate files"))
			this.outputSeperateFiles = obj.get("separate files").getAsBoolean();
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
					}
					ScriptParser.parseScriptFile(subFile);
				}
			}else{
				log.warn("Failed to parse include array! Check your formatting!");
			}
		}
		ModBiomeTweakerCore.logger.info("Finished early script parsing. Ready to force tweaks (Once implemented ;D).");
	}
	
	public ParsedBiomeEntry getEntry(int biomeID){
		if(this.parsedEntries.containsKey(biomeID))
			return this.parsedEntries.get(biomeID);
		else{
			ParsedBiomeEntry entry = new ParsedBiomeEntry(biomeID);
			this.parsedEntries.put(biomeID, entry);
			return entry;
		}
	}
	
}
