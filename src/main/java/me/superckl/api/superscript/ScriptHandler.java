package me.superckl.api.superscript;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import lombok.Getter;
import me.superckl.api.superscript.object.ScriptObject;

@Getter
public class ScriptHandler {

	private final static Map<String, ScriptObject> staticObjects = Maps.newHashMap();
	
	private final List<String> lines;

	private Iterator<String> it;

	private final Map<String, String> shortcuts = new HashMap<String, String>();
	private final Map<String, ScriptObject> objects = new HashMap<String, ScriptObject>();

	public ScriptHandler(final List<String> lines) {
		this.lines = lines;
		this.objects.putAll(staticObjects);
	}

	public void parse() throws Exception{
		this.it = this.lines.iterator();
		if(!this.it.hasNext())
			return;

		while(this.it.hasNext()){
			final String s = this.it.next().trim().split("#", 2)[0]; //Cuts out comments at the end of a line
			try {
				if(s.contains("=")){
					final Map<String, Object> map = ScriptParser.parseAssignment(s, this);
					if(map == null)
						continue;
					for(final Entry<String, Object> entry:map.entrySet())
						if(entry.getValue() instanceof String)
							this.shortcuts.put(entry.getKey(), (String) entry.getValue());
						else if(entry.getValue() instanceof ScriptObject)
							this.objects.put(entry.getKey(), (ScriptObject) entry.getValue());
				}else if(s.contains(".")){
					final String[] split = s.split("[.]", 2);
					if(split.length != 2){
						APIInfo.log.error("Found operator '.' in invalid context: "+s);
						continue;
					}
					if(!this.objects.containsKey(split[0])){
						APIInfo.log.error("Object not found: "+split[0]);
						continue;
					}
					final ScriptObject obj = this.objects.get(split[0]);
					obj.handleCall(split[1], this);
				}
			} catch (final Exception e) {
				APIInfo.log.error("Failed to handle a script line! "+s);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Registers a static ScriptObject, that can be referenced in scripts without being instantiated.
	 * @param name The name to be used in scripts. Example: The "Tweaker" object from BiomeTweaker.
	 * @param object The actual ScriptObject to reference.
	 */
	public static void registerStaticObject(String name, ScriptObject object){
		staticObjects.put(name, object);
	}
	
}
