package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.WorldEventHandler;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandRelight implements IScriptCommand{

	private final IBiomePackage pack;
	
	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			WorldEventHandler.getResetLight().add((byte) Biome.getIdForBiome(biome));
			//TODO fire event
		}
	}

}
