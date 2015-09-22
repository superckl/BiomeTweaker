package me.superckl.biometweaker.script.command;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.WorldType;

@RequiredArgsConstructor
public class ScriptCommandAverageBiomeSize implements IScriptCommand{

	private final String type;
	private final byte size;

	public ScriptCommandAverageBiomeSize(final byte size) {
		this(null, size);
	}

	@Override
	public void perform() throws Exception {
		if(this.type == null){
			BiomeEventHandler.globalSize = this.size;
			return;
		}
		WorldType type = null;
		for(final WorldType worldType:WorldType.worldTypes)
			if(worldType.getWorldTypeName().equals(this.type)){
				type = worldType;
				break;
			}
		if(type == null){
			LogHelper.warn("Failed to retrieve WorldType for '"+this.type+"'!");
			return;
		}
		BiomeEventHandler.sizes.put(type, this.size);
	}

}
