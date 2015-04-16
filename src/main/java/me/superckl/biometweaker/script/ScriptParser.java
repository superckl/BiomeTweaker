package me.superckl.biometweaker.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Cleanup;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.ScriptObject;
import me.superckl.biometweaker.util.CollectionHelper;

import com.google.common.collect.Lists;

public class ScriptParser {

	public static void parseScriptFile(final File file){
		try{
			final List<String> scriptLines = ScriptParser.parseScriptLines(file);
			new ScriptHandler(scriptLines).parse();
		}catch(final Exception e){
			ModBiomeTweakerCore.logger.error("Failed to parse a script file: "+file.getPath());
			e.printStackTrace();
		}
	}

	public static List<String> parseScriptLines(final File file) throws IOException{
		final List<String> array = new ArrayList<String>();
		@Cleanup
		final
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		while((line = r.readLine()) != null)
			if(!line.isEmpty() && !line.startsWith("#"))
				array.add(line);
		return array;
	}

	public static boolean isStringArg(final String arg){
		return arg.trim().startsWith("\"") && arg.endsWith("\"");
	}

	public static String extractStringArg(final String arg){
		return arg.trim().substring(1, arg.length()-1);
	}

	public static boolean isPositiveInteger(final String arg){
		return arg.matches("[0-9]+");
	}

	public static boolean isCommandCall(final String command){
		if(!command.endsWith(")") || !command.contains("("))
			return false;
		final String call = command.substring(0, command.indexOf("("));
		return call.equals(command);
	}

	public static String getCommandCalled(final String command){
		if(!command.endsWith(")") || !command.contains("("))
			return "";
		return command.substring(0, command.indexOf("("));
	}

	public static String[] parseArguments(final String script){
		if(!script.endsWith(")") || !script.contains("(")){
			ModBiomeTweakerCore.logger.error("Tried to parse an invalid argument array!");
			return new String[0];
		}
		final String args = script.substring(script.indexOf("(")+1, script.length()-1).trim();
		final String[] split = args.isEmpty() ? new String[0]:args.split(",");
		return split;
	}

	public static Map<String, Object> parseAssignment(final String script, final ScriptHandler handler){
		final String[] split = script.split("=");
		if(split.length != 2){
			ModBiomeTweakerCore.logger.error("Failed to parse object assignment: "+script);
			return null;
		}
		final String var = split[0].trim();
		final String assign = split[1].trim();
		if(assign.startsWith("\"") && assign.endsWith("\"")){
			final String shortcut = (String) ParameterType.STRING.tryParse(assign);
			return CollectionHelper.linkedMapWithEntry(var, (Object) shortcut);
		}else if(assign.startsWith("forBiomes(")){
			final String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
			if(args.length < 1){
				ModBiomeTweakerCore.logger.error("Can't assign biomes object with empty argument list: "+assign);
				return null;
			}
			final int[] array = ScriptParser.parseBiomeIds(args, handler);
			return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new BasicBiomesPackage(array)));
		}else if(assign.startsWith("forBiomesOfTypes(")){
			final String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
			final List<String> types = Lists.newArrayList();
			for(final String arg:args){
				if(!arg.startsWith("\"") || !arg.startsWith("\"")){
					ModBiomeTweakerCore.logger.error("Found non-String argument "+arg+" where a String is required: "+assign);
					continue;
				}
				types.add((String) ParameterType.STRING.tryParse(arg));
			}
			return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new TypeBiomesPackage(types.toArray(new String[types.size()]))));
		}else if(assign.startsWith("forallBiomesExcept(")){
			final String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
			if(args.length < 1){
				ModBiomeTweakerCore.logger.error("Can't assign biomes object with empty argument list: "+assign);
				return null;
			}
			final int[] array = ScriptParser.parseBiomeIds(args, handler);
			return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new AllButBiomesPackage(array)));
		}else if(assign.equals("forAllBiomes()"))
			return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new AllBiomesPackage()));
		else if(assign.startsWith("newBiomes(")){
			final String[] args = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
			if(args.length < 3){
				ModBiomeTweakerCore.logger.error("Can't assign biomes object with empty argument list: "+assign);
				return null;
			}
			final int[] array = ScriptParser.parseBiomeIds(Arrays.copyOfRange(args, 0, args.length-2), handler);
			if(!ScriptParser.isStringArg(args[args.length-2])){
				ModBiomeTweakerCore.logger.error("Found non-String argument "+args[args.length-2]+" where a String is required: "+assign);
				return null;
			}
			if(!ScriptParser.isPositiveInteger(args[args.length-1])){
				ModBiomeTweakerCore.logger.error("Found non-integer argument where integer required: "+assign);
				return null;
			}
			final String type = ScriptParser.extractStringArg(args[args.length-2]);
			final int weight = Integer.parseInt(args[args.length-1]);
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveBiome(new BasicBiomesPackage(array), type, weight));
			return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new BasicBiomesPackage(array)));
		}
		return null;
	}

	public static int[] parseBiomeIds(final String[] args, final ScriptHandler handler){
		final List<Integer> ints = new ArrayList<Integer>();
		for(final String arg:args){
			boolean parsed = false;
			if(ScriptParser.isPositiveInteger(arg)){
				ints.add(Integer.parseInt(arg));
				parsed = true;
			}else if(handler.getObjects().containsKey(arg)){
				final ScriptObject obj = handler.getObjects().get(arg);
				if(obj instanceof BiomesScriptObject){
					final BiomesScriptObject biomes = (BiomesScriptObject) obj;
					if(!biomes.getPack().supportsEarlyRawIds()){
						ModBiomeTweakerCore.logger.error("Tried to merge biome objects but biome object "+arg+" does not support early raw IDs! (It is dependent on biome registration.)");
						continue;
					}
					ints.addAll(biomes.getPack().getRawIds());
				}
			}else if(arg.contains("-")){
				final String[] split = arg.split("-");
				if((split.length == 2) && ScriptParser.isPositiveInteger(split[0]) && ScriptParser.isPositiveInteger(split[1])){
					final int start = Integer.parseInt(split[0]);
					final int end = Integer.parseInt(split[1]);
					final int[] values = CollectionHelper.range(start, end);
					CollectionHelper.addAll(ints, values);
					parsed = true;
				}
			}
			if(!parsed)
				ModBiomeTweakerCore.logger.error("Found invalid argument when parsing biome IDs. It will be ignored: "+arg);
		}
		final int[] array = new int[ints.size()];
		for(int i = 0; i < array.length; i++)
			array[i] = ints.get(i);
		return array;
	}

}
