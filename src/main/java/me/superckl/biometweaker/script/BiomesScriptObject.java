package me.superckl.biometweaker.script;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonPrimitive;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.config.ParsedBiomeEntry;
import me.superckl.biometweaker.util.LogHelper;

public class BiomesScriptObject implements IScriptObject{

	private final int[] biomes;
	
	public BiomesScriptObject(int ... biomes) {
		this.biomes = biomes;
	}
	
	public void handleCall(String call){
		if(call.startsWith("set(")){
			String[] args = ScriptParser.parseArguments(call);
			if(args.length < 2){
				LogHelper.error("Biome set command requires more than 1 argument, while "+args.length+" were found! Call: "+call);
				return;
			}
			String value = args[args.length - 1].trim();
			for(int i = 0; i < args.length - 1; i++){
				String arg = args[i].trim();
				if(!ScriptParser.isStringArg(arg)){
					LogHelper.error("Found non-String argument "+arg+" where a String is required: "+call);
					continue;
				}
				this.set(ScriptParser.extractStringArg(arg), value);
			}
		}else if(call.startsWith("remove(")){
			String[] args = ScriptParser.parseArguments(call);
			if(args.length < 2){
				LogHelper.error("Biome remove command requires more than 0 arguments, while "+args.length+" were found! Call: "+call);
				return;
			}
			for(int i = 0; i < args.length - 1; i++){
				String arg = args[i].trim();
				if(!ScriptParser.isStringArg(arg)){
					LogHelper.error("Found non-String argument "+arg+" where a String is required: "+call);
					continue;
				}
				this.remove(ScriptParser.extractStringArg(arg));
			}
		}
	}
	
	private void set(String key, String value){
		for(int biome:biomes)
			Config.INSTANCE.getEntry(biome).addMapping(key, new JsonPrimitive(value));
	}
	
	private void remove(String key){
		for(int biome:biomes)
			Config.INSTANCE.getEntry(biome).removeMapping(key);
	}
	
}
