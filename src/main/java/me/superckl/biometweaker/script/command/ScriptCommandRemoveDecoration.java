package me.superckl.biometweaker.script.command;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.script.IBiomePackage;

@RequiredArgsConstructor
public class ScriptCommandRemoveDecoration implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(!BiomeEventHandler.getDecorateTypes().containsKey(i))
				BiomeEventHandler.getDecorateTypes().put(i, new ArrayList<String>());
			BiomeEventHandler.getDecorateTypes().get(i).add(this.type);
		}
	}

}
