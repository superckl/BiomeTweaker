package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveBiome implements IScriptCommand{

	private final IBiomePackage pack;
	private final boolean remove;
	private final String type;
	private final int weight;

	public ScriptCommandAddRemoveBiome(final IBiomePackage pack) {
		this(pack, true, null, 0);
	}

	public ScriptCommandAddRemoveBiome(final IBiomePackage pack, final String type, final int weight) {
		this(pack, false, type, weight);
	}

	@Override
	public void perform() throws Exception {
		if(this.remove){
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
				Config.INSTANCE.onTweak(Biome.getIdForBiome(gen));
			}
		} else
			for(final int i:this.pack.getRawIds()){
				final BiomeTweakerBiome biome = new BiomeTweakerBiome(new BiomeProperties("BiomeTweaker Biome").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F));
				if(!MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Create(this, biome))){
					BiomeManager.addBiome(BiomeType.getType(this.type), new BiomeEntry(biome, this.weight));
					Biome.registerBiome(i, "bt_custom_biome_"+i, biome);
				}
				Config.INSTANCE.onTweak(i);
			}
	}

}
