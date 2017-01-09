package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.api.biome.BOPBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.world.WorldType;

@RequiredArgsConstructor
public class ScriptCommandRemoveBOPWorldType implements IScriptCommand{

	private final String name;

	@Override
	public void perform() throws Exception {
		final WorldType type = WorldType.parseWorldType(this.name);
		if(type == null)
			throw new IllegalArgumentException("Unable to find world type with name: "+this.name);
		BOPBiomes.excludedDecoratedWorldTypes.remove(type);
	}



}
