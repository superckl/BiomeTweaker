package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeAllDicTypes")
@RequiredArgsConstructor
public class ScriptCommandRemoveAllDictionaryTypes extends ScriptCommand{

	private final BiomePackage pack;

	@Override
	public void perform() throws Exception {
		final Iterator<Biome > it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveAllDictionaryTypes(this, gen)))
				continue;
			BiomeHelper.removeAllBiomeDicType(gen);
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

}
