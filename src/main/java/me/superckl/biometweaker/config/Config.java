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

	public static class Fields{

		public static final String SEPERATE_FILES = "seperate files";
		public static final String ENABLE_LIGHT_ASM = "enable light asm";
		public static final String ASM_BLACKLIST = "asm blacklist";
		public static final String REMOVE_LATE_BLOCK_ASSIGN = "remove late block assignments";
		public static final String INCLUDE = "include";

	}

	private final ScriptCommandManager commandManager = new ScriptCommandManager();
	private final Set<Integer> tweakedBiomes = new HashSet<Integer>();
	private boolean outputSeperateFiles = true;
	private boolean lightASM = false;
	private String[] asmBlacklist = new String[0];
	private boolean removeLateAssignments = false;
	private boolean init;
	private Config() {}

	@SneakyThrows
	public void init(final File whereAreWe, final JsonObject obj){
		final Logger log = ModBiomeTweakerCore.logger;
		if(this.init)
			log.warn("Config is already initialized! Tweaks will be applied immediately. Values changed previously will not be restored.");
		this.commandManager.reset();
		if(obj.has(Fields.SEPERATE_FILES))
			this.outputSeperateFiles = obj.get(Fields.SEPERATE_FILES).getAsBoolean();
		if(obj.has(Fields.ENABLE_LIGHT_ASM))
			this.lightASM = obj.get(Fields.ENABLE_LIGHT_ASM).getAsBoolean();
		if(obj.has(Fields.ASM_BLACKLIST)){
			final JsonArray array = obj.get(Fields.ASM_BLACKLIST).getAsJsonArray();
			this.asmBlacklist = new String[array.size()];
			for(int i = 0; i < this.asmBlacklist.length; i++)
				this.asmBlacklist[i] = array.get(i).getAsString();
		}
		if(obj.has(Fields.REMOVE_LATE_BLOCK_ASSIGN))
			this.removeLateAssignments = obj.get(Fields.REMOVE_LATE_BLOCK_ASSIGN).getAsBoolean();
		if(obj.has(Fields.INCLUDE)){
			final JsonElement element = obj.get(Fields.INCLUDE);
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
		this.init = true;
		ModBiomeTweakerCore.logger.info("Finished script parsing. Ready to tweak.");
	}

	public void addCommand(final IScriptCommand command){
		this.commandManager.addCommand(command);
	}

	public void onTweak(final int biomeID){
		this.tweakedBiomes.add(biomeID);
	}

}
