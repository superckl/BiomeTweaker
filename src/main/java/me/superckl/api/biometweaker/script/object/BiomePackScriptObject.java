package me.superckl.api.biometweaker.script.object;

import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.object.ScriptObject;

public abstract class BiomePackScriptObject extends ScriptObject{

	protected IBiomePackage pack;

	public IBiomePackage getPack() {
		return pack;
	}
	
}
