package me.superckl.biometweaker.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.SneakyThrows;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptCommandManager;
import me.superckl.biometweaker.script.ScriptCommandManager.ApplicationStage;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.IScriptCommand;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Getter
public class Config {

	public static final Config INSTANCE = new Config();

	private final ScriptCommandManager commandManager = new ScriptCommandManager();
	private final Set<Integer> tweakedBiomes = new HashSet<Integer>();
	private boolean outputSeperateFiles;

	private Config() {}

	@SneakyThrows
	public void init(final File whereAreWe, final JsonObject obj){
		final Logger log = ModBiomeTweakerCore.logger;
		if(obj.has("separate files"))
			this.outputSeperateFiles = obj.get("separate files").getAsBoolean();
		if(obj.has("include")){
			final JsonElement element = obj.get("include");
			if(element.isJsonArray()){
				final JsonArray array = (JsonArray) element;
				for(final JsonElement listElement:array){
					final String item = listElement.getAsString();
					final File subFile = new File(whereAreWe, item);
					if(!subFile.exists()){
						log.debug("Included subfile not found. A blank one will be generated.");
						subFile.createNewFile();
					}
					ScriptParser.parseScriptFile(subFile);
					this.commandManager.setCurrentStage(ApplicationStage.FINISHED_LOAD);
				}
			} else
				log.warn("Failed to parse include array! Check your formatting!");
		}
		ModBiomeTweakerCore.logger.info("Finished early script parsing. Ready to force tweaks (Once implemented ;D).");
	}

	public void addCommand(final IScriptCommand command){
		this.commandManager.addCommand(command);
	}

	public void onTweak(final int biomeID){
		this.tweakedBiomes.add(biomeID);
	}

}
