package me.superckl.biometweaker.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;

@RequiredArgsConstructor
@Getter
public class ScriptHandler {

	private final List<String> lines;
	
	private Iterator<String> it;
	
	private final Map<String, String> shortcuts = new HashMap<String, String>();
	private final Map<String, IScriptObject> objects = new HashMap<String, IScriptObject>();
	
	public void parse(){
		this.it = lines.iterator();
		if(!it.hasNext())
			return;
		
		while(it.hasNext()){
			String s = it.next().trim();
			if(s.contains("=")){
				Entry<String, Object> entry = ScriptParser.parseAssignment(s);
				if(entry == null)
					continue;
				else if(entry.getValue() instanceof String)
					this.shortcuts.put(entry.getKey(), (String) entry.getValue());
				else if(entry.getValue() instanceof IScriptObject)
					this.objects.put(entry.getKey(), (IScriptObject) entry.getValue());
			}else if(s.contains(".")){
				String[] split = s.split("[.]", 2);
				if(split.length != 2){
					ModBiomeTweakerCore.logger.error("Found operator '.' in invalid context: "+s);
					continue;
				}
				if(!this.objects.containsKey(split[0])){
					ModBiomeTweakerCore.logger.error("Object not found: "+split[0]);
					continue;
				}
				IScriptObject obj = this.objects.get(split[0]);
				obj.handleCall(split[1], this);
			}
		}
	}
	
}
