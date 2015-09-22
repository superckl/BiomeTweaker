package me.superckl.api.biometweaker;

import cpw.mods.fml.common.Loader;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.object.ScriptObject;

public class BiomeTweakerAPI {

	private static Class<? extends ScriptObject> biomesScriptObjectClass;
	private static Class<? extends ScriptObject> tweakerScriptObjectClass;
	private static boolean foundBiomeTweaker;

	static{
		boolean found = false;
		if(Loader.isModLoaded("BiomeTweaker"))
			try {
				BiomeTweakerAPI.biomesScriptObjectClass = (Class<? extends ScriptObject>) Class.forName("me.superckl.biometweaker.script.object.BiomesScriptObject");
				BiomeTweakerAPI.tweakerScriptObjectClass = (Class<? extends ScriptObject>) Class.forName("me.superckl.biometweaker.script.object.TweakerScriptObject");
				found = true;
			} catch (final Exception e) {
				found = false;
				APIInfo.log.debug("Failed to find BiomeTweaker. BiomeTweakerAPI will do nothing.");
				e.printStackTrace();
			}
		BiomeTweakerAPI.foundBiomeTweaker = found;
	}

	public static boolean foundBiomeTweaker(){
		return BiomeTweakerAPI.foundBiomeTweaker;
	}

	public static Class<? extends ScriptObject> getBiomesScriptObjectClass(){
		return BiomeTweakerAPI.biomesScriptObjectClass;
	}

	public static Class<? extends ScriptObject> getTweakerScriptObjectClass(){
		return BiomeTweakerAPI.tweakerScriptObjectClass;
	}

	public static boolean registerBiomesScriptCommand(final String name, final ScriptCommandListing listing){
		if(BiomeTweakerAPI.foundBiomeTweaker){
			ScriptCommandRegistry.INSTANCE.registerListing(name, listing, BiomeTweakerAPI.biomesScriptObjectClass);
			return true;
		}
		return false;
	}

	public static boolean registerTweakerScriptCommand(final String name, final ScriptCommandListing listing){
		if(BiomeTweakerAPI.foundBiomeTweaker){
			ScriptCommandRegistry.INSTANCE.registerListing(name, listing, BiomeTweakerAPI.tweakerScriptObjectClass);
			return true;
		}
		return false;
	}

}
