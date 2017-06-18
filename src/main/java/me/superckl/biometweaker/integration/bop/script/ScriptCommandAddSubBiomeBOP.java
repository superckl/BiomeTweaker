package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.common.init.ModBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;

@RequiredArgsConstructor
public class ScriptCommandAddSubBiomeBOP implements IScriptCommand{

	private final IBiomePackage pack;
	private final IBiomePackage toAdd;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(!ModBiomes.subBiomesMap.containsKey(i))
				ModBiomes.subBiomesMap.put(i, this.toAdd.getRawIds());
			else
				ModBiomes.subBiomesMap.get(i).addAll(this.toAdd.getRawIds());
			BiomeTweaker.getInstance().onTweak(i);
		}
	}

}
