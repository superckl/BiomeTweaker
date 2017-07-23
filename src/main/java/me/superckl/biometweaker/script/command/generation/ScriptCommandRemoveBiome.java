package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "remove")
@RequiredArgsConstructor
public class ScriptCommandRemoveBiome extends ScriptCommand{

	private final BiomePackage pack;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			for(final BiomeType type:BiomeType.values())
				for(final BiomeEntry entry:BiomeManager.getBiomes(type))
					if(Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(gen))
						if(!MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Remove(this, entry.biome, entry))){
							BiomeManager.removeBiome(type, entry);
							if(BiomeManager.getBiomes(type).isEmpty())
								LogHelper.warn("Viable generation biomes for type "+type+" is empty! This will cause Vanilla generation to crash! You've been warned!");
						}
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

}
