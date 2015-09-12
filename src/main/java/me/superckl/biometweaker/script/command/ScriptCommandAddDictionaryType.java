package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor
public class ScriptCommandAddDictionaryType implements IScriptCommand{

	private final IBiomePackage pack;
	private final String type;

	@Override
	public void perform() throws Exception {
		final BiomeDictionary.Type bType = Type.getType(this.type);
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddDictionaryType(this, gen, bType)))
				continue;
			BiomeHelper.modifyBiomeDicType(gen, bType, false);
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
