package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.common.init.ModBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;

@RequiredArgsConstructor
public class ScriptCommandRemoveSubBiomeBOP extends ScriptCommand{

	private final BiomePackage pack;
	private final BiomePackage toAdd;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(ModBiomes.subBiomesMap.containsKey(i))
				ModBiomes.subBiomesMap.get(i).removeAll(this.toAdd.getRawIds());
			BiomeTweaker.getInstance().onTweak(i);
		}
	}

}
