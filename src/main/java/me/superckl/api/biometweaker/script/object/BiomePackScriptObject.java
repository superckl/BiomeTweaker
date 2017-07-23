package me.superckl.api.biometweaker.script.object;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.object.ScriptObject;

public abstract class BiomePackScriptObject extends ScriptObject{

	protected BiomePackage pack;

	public BiomePackage getPack() {
		return this.pack;
	}

}
