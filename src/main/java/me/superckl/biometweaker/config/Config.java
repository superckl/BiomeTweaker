package me.superckl.biometweaker.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.SneakyThrows;
import me.superckl.api.superscript.ScriptCommandManager;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.core.BiomeTweakerCore;

@Getter
public class Config {

	public static final Config INSTANCE = new Config();

	public static class Fields{

		public static final String SEPARATE_FILES = "separate files";
		public static final String VERSION_CHECK = "version check";
		public static final String ENABLE_LIGHT_ASM = "enable light asm";
		public static final String ASM_BLACKLIST = "asm blacklist";
		public static final String REMOVE_LATE_BLOCK_ASSIGN = "remove late block assignments";
		public static final String INCLUDE = "include";

	}

	private final ScriptCommandManager commandManager = ScriptCommandManager.newInstance(ModData.MOD_ID);
	private final Set<Integer> tweakedBiomes = new HashSet<Integer>();
	private boolean outputSeperateFiles = true;
	private boolean versionCheck = false;
	private boolean lightASM = false;
	private String[] asmBlacklist = new String[0];
	private boolean removeLateAssignments = false;
	private JsonArray includes = new JsonArray();
	private boolean init;
	private File whereAreWe;
	private Config() {}

	@SneakyThrows
	public void init(final File whereAreWe, final JsonObject obj){
		this.whereAreWe = whereAreWe;
		if(this.init)
			BiomeTweakerCore.logger.warn("Config is already initialized! Tweaks will be applied immediately. Values changed previously will not be restored.");
		this.commandManager.reset();
		if(obj.has(Fields.SEPARATE_FILES))
			this.outputSeperateFiles = obj.get(Fields.SEPARATE_FILES).getAsBoolean();
		if(obj.has(Fields.VERSION_CHECK))
			this.versionCheck = obj.get(Fields.VERSION_CHECK).getAsBoolean();
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
				this.includes = array;
			} else
				BiomeTweakerCore.logger.warn("Failed to parse include array! Check your formatting!");
		}
		this.init = true;
		BiomeTweakerCore.logger.info("Finished config parsing.");
	}

	public void addCommand(final IScriptCommand command){
		this.commandManager.addCommand(command);
	}

	public void onTweak(final int biomeID){
		this.tweakedBiomes.add(biomeID);
	}

}
