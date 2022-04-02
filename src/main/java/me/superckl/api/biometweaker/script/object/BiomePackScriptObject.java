package me.superckl.api.biometweaker.script.object;

import java.util.Set;

import com.google.common.collect.Sets;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.object.ScriptObject;

public abstract class BiomePackScriptObject extends ScriptObject{

	public static final Set<ApplicationStage> REGISTRY_AVAILABLE = Sets.immutableEnumSet(ApplicationStage.CONSTRUCTION);

	protected BiomePackage pack;

	public BiomePackage getPack() {
		return this.pack;
	}

}
