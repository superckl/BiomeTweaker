package me.superckl.api.biometweaker;

import java.util.function.Consumer;

import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.command.ScriptCommandListing;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.api.superscript.script.object.ScriptObject;

public class BiomeTweakerAPI {

	public static final String MOD_ID = "biometweaker";

	private static Class<? extends ScriptObject> biomesScriptObjectClass;
	private static Class<? extends ScriptObject> tweakerScriptObjectClass;
	private static Consumer<ScriptCommand> commandAdder;

	public static void setBiomesScriptObjectClass(final Class<? extends ScriptObject> biomesScriptObjectClass) {
		BiomeTweakerAPI.biomesScriptObjectClass = biomesScriptObjectClass;
	}

	public static void setTweakerScriptObjectClass(final Class<? extends ScriptObject> tweakerScriptObjectClass) {
		BiomeTweakerAPI.tweakerScriptObjectClass = tweakerScriptObjectClass;
	}

	public static Class<? extends ScriptObject> getBiomesScriptObjectClass(){
		return BiomeTweakerAPI.biomesScriptObjectClass;
	}

	public static Class<? extends ScriptObject> getTweakerScriptObjectClass(){
		return BiomeTweakerAPI.tweakerScriptObjectClass;
	}

	public static boolean registerScriptCommand(final Class<? extends ScriptObject> clazz, final String name, final ScriptCommandListing listing){
		ScriptCommandRegistry.INSTANCE.registerListing(name, listing, clazz);
		return true;
	}

	public static Consumer<ScriptCommand> getCommandAdder() {
		return BiomeTweakerAPI.commandAdder;
	}

	public static void setCommandAdder(final Consumer<ScriptCommand> commandAdder) {
		BiomeTweakerAPI.commandAdder = commandAdder;
	}

}
