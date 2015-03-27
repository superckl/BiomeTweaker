package me.superckl.biometweaker.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.object.ScriptObject;

@RequiredArgsConstructor
@Getter
public class ScriptHandler {

	private final List<String> lines;

	private Iterator<String> it;

	private final Map<String, String> shortcuts = new HashMap<String, String>();
	private final Map<String, ScriptObject> objects = new HashMap<String, ScriptObject>();

	public void parse(){
		this.it = this.lines.iterator();
		if(!this.it.hasNext())
			return;

		while(this.it.hasNext()){
			final String s = this.it.next().trim();
			if(s.contains("=")){
				final Map<String, Object> map = ScriptParser.parseAssignment(s);
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
					ModBiomeTweakerCore.logger.error("Found operator '.' in invalid context: "+s);
					continue;
				}
				if(!this.objects.containsKey(split[0])){
					ModBiomeTweakerCore.logger.error("Object not found: "+split[0]);
					continue;
				}
				final ScriptObject obj = this.objects.get(split[0]);
				obj.handleCall(split[1], this);
			}
		}
	}

}
