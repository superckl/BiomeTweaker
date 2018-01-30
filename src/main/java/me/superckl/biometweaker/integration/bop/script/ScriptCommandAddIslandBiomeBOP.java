package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.common.init.ModBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;

@RequiredArgsConstructor
public class ScriptCommandAddIslandBiomeBOP extends ScriptCommand{

	private final BiomePackage pack;
	private final int weight;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(!ModBiomes.islandBiomesMap.containsKey(i)) {
				ModBiomes.islandBiomesMap.put(i, this.weight);
				ModBiomes.totalIslandBiomesWeight += this.weight;
			}
			BiomeTweaker.getInstance().onTweak(i);
		}
	}

}
