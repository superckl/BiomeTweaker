package me.superckl.biometweaker.script.command.generation;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.WorldType;

@AutoRegister(classes = TweakerScriptObject.class, name = "setAverageBiomeSize")
@RequiredArgsConstructor
public class ScriptCommandAverageBiomeSize extends ScriptCommand{

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
		for(final WorldType worldType:WorldType.WORLD_TYPES)
			if(worldType.getName().equals(this.type)){
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
