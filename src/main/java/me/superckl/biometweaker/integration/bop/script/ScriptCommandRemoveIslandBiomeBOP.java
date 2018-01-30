package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.common.init.ModBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;

@RequiredArgsConstructor
public class ScriptCommandRemoveIslandBiomeBOP extends ScriptCommand{

	private final BiomePackage pack;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(ModBiomes.islandBiomesMap.containsKey(i)) {
				final int weight = ModBiomes.islandBiomesMap.remove(i);
				ModBiomes.totalIslandBiomesWeight -= weight;
			}
			BiomeTweaker.getInstance().onTweak(i);
		}
	}

}
