package me.superckl.biometweaker.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.ParsedBiomeEntry;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;

@RequiredArgsConstructor
public class ScriptHandler {

	private final List<String> lines;
	
	private Iterator<String> it;
	
	private final Map<String, String> shortcuts = new HashMap<String, String>();
	private final Map<String, IScriptObject> objects = new HashMap<String, IScriptObject>();
	
	public Map<Integer, ParsedBiomeEntry> parse(){
		this.it = lines.iterator();
		Map<Integer, ParsedBiomeEntry> map = new HashMap<Integer, ParsedBiomeEntry>();
		if(!it.hasNext())
			return map;
		
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
				//TODO
			}
		}
		
		return map;
	}
	
}
