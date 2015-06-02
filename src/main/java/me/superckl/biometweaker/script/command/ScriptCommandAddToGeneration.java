package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.script.pack.IBiomePackage;

@RequiredArgsConstructor
public class ScriptCommandAddToGeneration implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;
	private final int weight;
	
	@Override
	public void perform() throws Exception {
		BiomeManager.BiomeType type = BiomeManager.BiomeType.valueOf(this.type);
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			BiomeManager.addBiome(type, new BiomeEntry(it.next(), weight));
		}
	}

}
