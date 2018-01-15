package me.superckl.api.superscript.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.superckl.api.superscript.APIInfo;
import me.superckl.api.superscript.script.object.ScriptObject;

public class ScriptHandler {

	private final static Map<String, Class<? extends ScriptObject>> staticObjects = new HashMap<>();

	private final List<ScriptLine> lines;

	private final Map<String, String> shortcuts = new HashMap<>();
	private final Map<String, ScriptObject> objects = new HashMap<>();

	public ScriptHandler(final List<ScriptLine> scriptLines) {
		this.lines = scriptLines;
		for(final Entry<String, Class<? extends ScriptObject>> entry:ScriptHandler.staticObjects.entrySet())
			try {
				this.objects.put(entry.getKey(), entry.getValue().newInstance());
			} catch (final Exception e) {
				APIInfo.log.error("Failed to instantiate static object "+entry.getKey()+"! Reason: "+e.getMessage());
				APIInfo.log.debug("Full stacktrace of error provided below for bug reports.", e);
			}
	}

	public void parse() throws Exception{
		final Iterator<ScriptLine> it = this.lines.iterator();
		if(!it.hasNext())
			return;
		while(it.hasNext()){
			final ScriptLine line = it.next();
			final String s = line.getLine().split("#", 2)[0].trim(); //Cuts out comments at the end of a line
			try {
				if(s.contains("=")){
					final Map<String, Object> map = ScriptParser.parseAssignment(s, line.getContext(), this);
					if(map == null)
						continue;
					for(final Entry<String, Object> entry:map.entrySet())
						if(entry.getValue() instanceof String)
							this.shortcuts.put(entry.getKey(), (String) entry.getValue());
						else if(entry.getValue() instanceof ScriptObject) {
							((ScriptObject) entry.getValue()).setContext(line.getContext());
							this.objects.put(entry.getKey(), (ScriptObject) entry.getValue());
						}
				}else if(s.contains(".")){
					final String[] split = s.split("[.]", 2);
					if(split.length != 2) {
						APIInfo.log.error("Failed to handle a script line! "+s+" @ "+line.getContext()+". Reason: Found operator '.' in invalid context.");
						continue;
					}
					if(!this.objects.containsKey(split[0])) {
						APIInfo.log.error("Failed to handle a script line! "+s+" @ "+line.getContext()+". Reason: Object not found: "+split[0]);
						continue;
					}
					final ScriptObject obj = this.objects.get(split[0]);
					obj.handleCall(split[1], line.getContext(), this);
				}
			} catch (final Exception e) {
				APIInfo.log.error("Failed to handle a script line! "+s+" @ "+line.getContext()+". Reason: "+e.getMessage());
				APIInfo.log.debug("Full stacktrace of error provided below for bug reports.", e);
			}
		}
	}

	/**
	 * Registers a static ScriptObject, that can be referenced in scripts without being instantiated.
	 * @param name The name to be used in scripts. Example: The "Tweaker" object from BiomeTweaker.
	 * @param clazz The Class object for the ScriptObject. Static ScriptObjects must have a no argument constructor.
	 */
	public static void registerStaticObject(final String name, final Class<? extends ScriptObject> clazz){
		ScriptHandler.staticObjects.put(name, clazz);
	}

	public List<ScriptLine> getLines() {
		return this.lines;
	}

	public Map<String, String> getShortcuts() {
		return this.shortcuts;
	}

	public Map<String, ScriptObject> getObjects() {
		return this.objects;
	}

}
