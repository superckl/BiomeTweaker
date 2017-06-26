package me.superckl.api.superscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.ConstructorListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;

public class ScriptParser {

	private static final Map<String, ConstructorListing<ScriptObject>> validObjects = Maps.newHashMap();

	/**
	 * Registers a new way to instantiate a ScriptObject.<br>
	 * NOTE: The constructor listing in this method does not care what the actual constructors are.
	 *  It will always call the constructor of the same class that takes no arguments, and then call {@link ScriptObject#readArgs(Object...)}.
	 * @param name The name of the way to instantiate the object. Example: "forBiomes" from BiomeTweaker.
	 * @param listing The ConstructorListing to use.
	 */
	public static void registerValidObjectInst(final String name, final ConstructorListing<ScriptObject> listing){
		ScriptParser.validObjects.put(name, listing);
	}

	public static void parseScriptFile(final File file){
		try{
			final List<String> scriptLines = ScriptParser.parseScriptLines(file);
			new ScriptHandler(scriptLines).parse();
		}catch(final Exception e){
			APIInfo.log.error("Failed to parse a script file: "+file.getPath());
			e.printStackTrace();
		}
	}

	public static List<String> parseScriptLines(final File file) throws IOException{
		final List<String> array = new ArrayList<String>();
		try(final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
			String line;
			while((line = r.readLine()) != null)
				if(!line.isEmpty() && !line.startsWith("#"))
					array.add(line);
		}
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
			APIInfo.log.error("Tried to parse an invalid argument array!");
			return new String[0];
		}
		final String args = script.substring(script.indexOf("(")+1, script.length()-1).trim();
		final String[] split = args.isEmpty() ? new String[0]:args.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		return split;
	}

	public static Map<String, Object> parseAssignment(final String script, final ScriptHandler handler) throws Exception{
		final String[] split = script.split("=");
		if(split.length != 2){
			APIInfo.log.error("Failed to parse object assignment: "+script);
			return null;
		}
		final String var = split[0].trim();
		final String assign = split[1].trim();
		if(assign.startsWith("\"") && assign.endsWith("\"")){
			final String shortcut = (String) ParameterTypes.STRING.tryParse(assign, handler);
			return CollectionHelper.linkedMapWithEntry(var, (Object) shortcut);
		}else{
			final String called = ScriptParser.getCommandCalled(assign);
			if(ScriptParser.validObjects.containsKey(called)){
				final ConstructorListing<ScriptObject> listing = ScriptParser.validObjects.get(called);
				String[] arguments = CollectionHelper.trimAll(ScriptParser.parseArguments(assign));
				for(final Entry<List<ParameterWrapper>, Constructor<? extends ScriptObject>> entry:listing.getConstructors().entrySet()){
					final List<Object> objs = Lists.newArrayList();
					final List<ParameterWrapper> params = Lists.newArrayList(entry.getKey());
					final Iterator<ParameterWrapper> it = params.iterator();
					while(it.hasNext()){
						final ParameterWrapper wrap = it.next();
						final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
						Collections.addAll(objs, parsed.getKey());
						arguments = parsed.getValue();
						it.remove();
					}
					if(!params.isEmpty() || (arguments.length != 0))
						continue;
					final Object[] args = new Object[objs.size()];
					System.arraycopy(objs.toArray(), 0, args, 0, objs.size());
					final ScriptObject obj = entry.getValue().getDeclaringClass().newInstance();
					obj.readArgs(args);
					return CollectionHelper.linkedMapWithEntry(var, (Object) obj);
				}
			}
			APIInfo.log.error("Failed to find meaning in object assignment "+script+". It will be ignored.");
		}
		return null;
	}

	@Nullable
	public static Pair<Constructor<? extends IScriptCommand>, Object[]> findConstructor(final ScriptCommandListing listing, final String[] args, final ScriptHandler handler) throws Exception{
		outer:
			for(final Entry<List<ParameterWrapper>, Constructor<? extends IScriptCommand>> entry:listing.getConstructors().entrySet()){
				String[] arguments = Arrays.copyOf(args, args.length);
				final List<Object> objs = Lists.newArrayList();
				final List<ParameterWrapper> params = Lists.newArrayList(entry.getKey());
				final Iterator<ParameterWrapper> it = params.iterator();
				while(it.hasNext()){
					final ParameterWrapper wrap = it.next();
					final Pair<Object[], String[]> parsed = wrap.parseArgs(handler, arguments);
					if((parsed.getKey().length == 0) && !wrap.canReturnNothing())
						continue outer;
					Collections.addAll(objs, parsed.getKey());
					arguments = parsed.getValue();
					it.remove();
				}
				if(!params.isEmpty() || (arguments.length != 0))
					continue;
				return Pair.of(entry.getValue(), objs.toArray());
			}
	return null;
	}

	public static Map<String, ConstructorListing<ScriptObject>> getValidobjects() {
		return ScriptParser.validObjects;
	}

}
