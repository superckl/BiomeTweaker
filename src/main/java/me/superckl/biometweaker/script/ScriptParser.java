package me.superckl.biometweaker.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.ScriptObject;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.biometweaker.script.util.ParameterType;
import me.superckl.biometweaker.script.util.ParameterWrapper;
import me.superckl.biometweaker.script.util.ScriptListing;
import me.superckl.biometweaker.util.CollectionHelper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.realmsclient.util.Pair;

public class ScriptParser {

	@Getter
	private static final Map<String, ScriptListing<ScriptObject>> validObjects = Maps.newHashMap();

	static{
		try{
			ScriptListing<ScriptObject> listing = new ScriptListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(ParameterType.BASIC_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor(IBiomePackage.class));
			ScriptParser.validObjects.put("forBiomes", listing);

			listing = new ScriptListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(ParameterType.TYPE_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor(IBiomePackage.class));
			ScriptParser.validObjects.put("forBiomesOfTypes", listing);

			listing = new ScriptListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(ParameterType.ALL_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor(IBiomePackage.class));
			ScriptParser.validObjects.put("forAllBiomes", listing);

			listing = new ScriptListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(ParameterType.ALL_BUT_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor(IBiomePackage.class));
			ScriptParser.validObjects.put("forAllBiomesExcept", listing);

		}catch(final Exception e){
			ModBiomeTweakerCore.logger.error("Something went wrong when filling the object mappings! Some objects may not work!");
			e.printStackTrace();
		}
	}

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

	public static boolean isPositiveInteger(final String arg){
		return arg.matches("[0-9]+");
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

	public static Map<String, Object> parseAssignment(final String script, final ScriptHandler handler) throws Exception{
		final String[] split = script.split("=");
		if(split.length != 2){
			ModBiomeTweakerCore.logger.error("Failed to parse object assignment: "+script);
			return null;
		}
		final String var = split[0].trim();
		final String assign = split[1].trim();
		if(assign.startsWith("\"") && assign.endsWith("\"")){
			final String shortcut = (String) ParameterType.STRING.tryParse(assign, handler);
			return CollectionHelper.linkedMapWithEntry(var, (Object) shortcut);
		}else{
			final String called = ScriptParser.getCommandCalled(assign);
			if(ScriptParser.validObjects.containsKey(called)){
				final ScriptListing<ScriptObject> listing = ScriptParser.validObjects.get(called);
				String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
				for(final Entry<List<ParameterWrapper>, Constructor<? extends ScriptObject>> entry:listing.getConstructors().entrySet()){
					final List<Object> objs = Lists.newArrayList();
					final List<ParameterWrapper> params = Lists.newArrayList(entry.getKey());
					final Iterator<ParameterWrapper> it = params.iterator();
					while(it.hasNext()){
						final ParameterWrapper wrap = it.next();
						final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
						Collections.addAll(objs, parsed.first());
						arguments = parsed.second();
						it.remove();
					}
					if(!params.isEmpty() || (arguments.length != 0))
						continue;
					final IBiomePackage[] args = new IBiomePackage[objs.size()];
					System.arraycopy(objs.toArray(), 0, args, 0, objs.size());
					return CollectionHelper.linkedMapWithEntry(var, (Object) new BiomesScriptObject(new MergedBiomesPackage(args)));
				}
			}
			ModBiomeTweakerCore.logger.error("Failed to find meaning in object assignment "+script+". It will be ignored.");
		}
		return null;
	}

}
