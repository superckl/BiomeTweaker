package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addDicType")
@RequiredArgsConstructor
public class ScriptCommandAddDictionaryType implements IScriptCommand{

	private final IBiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			for (final String type:this.types) {
				final BiomeDictionary.Type bType = Type.getType(type);
				if (MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddDictionaryType(this, gen, bType)))
					continue;
				BiomeHelper.modifyBiomeDicType(gen, bType, false);
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
			}
		}
	}

}
