package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.api.biome.BOPBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.script.command.ScriptCommand;
import net.minecraft.world.WorldType;

@RequiredArgsConstructor
public class ScriptCommandRemoveBOPWorldType extends ScriptCommand{

	private final String name;

	@Override
	public void perform() throws Exception {
		final WorldType type = WorldType.parseWorldType(this.name);
		if(type == null)
			throw new IllegalArgumentException("Unable to find world type with name: "+this.name);
		BOPBiomes.excludedDecoratedWorldTypes.remove(type);
	}



}
