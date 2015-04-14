package me.superckl.biometweaker.script.command;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;

@RequiredArgsConstructor
public class ScriptCommandRemoveDecoration implements IScriptCommand{

	private final int biomeID;
	private final String type;

	@Override
	public void perform() throws Exception {
		if(!BiomeEventHandler.getDecorateTypes().containsKey(this.biomeID))
			BiomeEventHandler.getDecorateTypes().put(this.biomeID, new ArrayList<String>());
		BiomeEventHandler.getDecorateTypes().get(this.biomeID).add(this.type);
	}

}
