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
		if(Loader.isModLoaded("BiomeTweaker")){
			try {
				biomesScriptObjectClass = (Class<? extends ScriptObject>) Class.forName("me.superckl.biometweaker.script.object.BiomesScriptObject");
				tweakerScriptObjectClass = (Class<? extends ScriptObject>) Class.forName("me.superckl.biometweaker.script.object.TweakerScriptObject");
				found = true;
			} catch (Exception e) {
				found = false;
				APIInfo.log.debug("Failed to find BiomeTweaker. BiomeTweakerAPI will do nothing.");
				e.printStackTrace();
			}
		}
		foundBiomeTweaker = found;
	}
	
	public static boolean foundBiomeTweaker(){
		return foundBiomeTweaker;
	}
	
	public static Class<? extends ScriptObject> getBiomesScriptObjectClass(){
		return biomesScriptObjectClass;
	}
	
	public static Class<? extends ScriptObject> getTweakerScriptObjectClass(){
		return tweakerScriptObjectClass;
	}
	
	public static boolean registerBiomesScriptCommand(String name, ScriptCommandListing listing){
		if(foundBiomeTweaker){
			ScriptCommandRegistry.INSTANCE.registerListing(name, listing, biomesScriptObjectClass);
			return true;
		}
		return false;
	}
	
	public static boolean registerTweakerScriptCommand(String name, ScriptCommandListing listing){
		if(foundBiomeTweaker){
			ScriptCommandRegistry.INSTANCE.registerListing(name, listing, tweakerScriptObjectClass);
			return true;
		}
		return false;
	}
	
}
