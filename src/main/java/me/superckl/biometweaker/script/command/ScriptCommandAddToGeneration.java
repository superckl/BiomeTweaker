package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandAddToGeneration implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;
	private final int weight;

	@Override
	public void perform() throws Exception {
		final BiomeManager.BiomeType type = BiomeManager.BiomeType.valueOf(this.type);
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final BiomeTweakEvent.AddToGeneration event = new BiomeTweakEvent.AddToGeneration(this, biome, new BiomeEntry(biome, this.weight));
			if(MinecraftForge.EVENT_BUS.post(event))
				continue;
			BiomeManager.addBiome(type, event.getEntry());
		}
	}

}
