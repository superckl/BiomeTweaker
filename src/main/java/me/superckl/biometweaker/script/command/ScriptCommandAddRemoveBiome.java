package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.IBiomePackage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

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
			final Iterator<BiomeGenBase> it = this.pack.getIterator();
			while(it.hasNext()){
				final BiomeGenBase gen = it.next();
				for(final BiomeType type:BiomeType.values())
					for(final BiomeEntry entry:BiomeManager.getBiomes(type))
						if(entry.biome.biomeID == gen.biomeID)
							BiomeManager.removeBiome(type, entry);
				Config.INSTANCE.onTweak(gen.biomeID);
			}
		} else
			for(final int i:this.pack.getRawIds()){
				final BiomeTweakerBiome biome = new BiomeTweakerBiome(i);
				BiomeManager.addBiome(BiomeType.getType(this.type), new BiomeEntry(biome, this.weight));
				Config.INSTANCE.onTweak(i);
			}
	}

}
