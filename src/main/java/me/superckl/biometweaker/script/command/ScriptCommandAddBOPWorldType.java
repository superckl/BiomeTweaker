package me.superckl.biometweaker.script.command;

import java.util.List;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.util.IntegrationHelper;
import net.minecraft.world.WorldType;

@RequiredArgsConstructor
public class ScriptCommandAddBOPWorldType implements IScriptCommand{
	
	private final String name;
	
	@Override
	public void perform() throws Exception {
		WorldType type = WorldType.parseWorldType(this.name);
		if(type == null){
			throw new IllegalArgumentException("Unable to find world type with name: "+this.name);
		}
		List<WorldType> list = IntegrationHelper.reflectBOPWorldTypesList();
		if(!list.contains(type))
			list.add(type);
	}
	
}
