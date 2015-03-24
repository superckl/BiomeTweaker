package me.superckl.biometweaker.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.Cleanup;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.util.LogHelper;

public class ScriptParser {

	public static void parseScriptFile(File file){
		try{
			List<String> scriptLines = parseScriptLines(file);
			new ScriptHandler(scriptLines).parse();
		}catch(Exception e){
			ModBiomeTweakerCore.logger.error("Failed to parse a script file: "+file.getPath());
			e.printStackTrace();
		}
	}
	
	public static List<String> parseScriptLines(File file) throws IOException{
		List<String> array = new ArrayList<String>();
		@Cleanup
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while((line = r.readLine()) != null)
			if(!line.isEmpty() && !line.startsWith("#"))
				array.add(line);
		return array;
	}
	
	public static boolean isStringArg(String arg){
		return arg.trim().startsWith("\"") && arg.endsWith("\"");
	}
	
	public static String extractStringArg(String arg){
		return arg.trim().substring(1, arg.length()-1);
	}
	
	public static boolean isPositiveInteger(String arg){
		return arg.matches("[0-9]+");
	}
	
	public static String[] parseArguments(String script){
		if(!script.endsWith(")")){
			LogHelper.error("Tried to parse an argument array that didn't end with ')'!");
			return new String[0];
		}
		String[] split = script.substring(script.indexOf("(")+1, script.length()-1).split(",");
		return split;
	}
	
	public static Entry<String, Object> parseAssignment(String script){
		String[] split = script.split("=");
		if(split.length != 2){
			LogHelper.error("Failed to parse object assignment: "+script);
			return null;
		}
		String var = split[0].trim();
		String assign = split[1].trim();
		if(assign.startsWith("\"")){
			String shortcut = assign.substring(1, assign.length()-1);
			return new AbstractMap.SimpleEntry(var, shortcut);
		}else if(assign.startsWith("forBiomes(")){
			String[] args = parseArguments(assign);
			if(args.length < 1){
				LogHelper.error("Can't assign biomes object with empty argument list: "+assign);
				return null;
			}
			List<Integer> ints = new ArrayList<Integer>();
			for(String arg:args){
				if(!isPositiveInteger(arg)){
					LogHelper.error("Found non-integer argument where integer required: "+assign);
					continue;
				}
				ints.add(Integer.parseInt(arg));
			}
			int[] array = new int[ints.size()];
			for(int i = 0; i < array.length; i++)
				array[i] = ints.get(i);
			return new AbstractMap.SimpleEntry(var, new BiomesScriptObject(array));
		}else if(assign.equals("forAllBiomes()")){
			return new AbstractMap.SimpleEntry(var, new BiomesScriptObject(-1));
		}
		return null;
	}
	
}
