package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.IBiomePackage;
import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandRemoveDictionaryType implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;

	@Override
	public void perform() throws Exception {
		final BiomeDictionary.Type bType = Type.getType(this.type);
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveDictionaryType(this, gen, bType)))
				continue;
			BiomeHelper.modifyBiomeDicType(gen, bType, true);
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
