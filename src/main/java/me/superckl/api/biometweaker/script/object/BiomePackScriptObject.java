package me.superckl.api.biometweaker.script.object;

import lombok.Getter;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.object.ScriptObject;

public abstract class BiomePackScriptObject extends ScriptObject{

	@Getter
	protected IBiomePackage pack;
	
}
