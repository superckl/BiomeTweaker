package me.superckl.biometweaker.script.command.world.gen;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addDicTypes")
@RequiredArgsConstructor
public class ScriptCommandAddDictionaryTypes extends ScriptCommand{

	private final BiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		final Type[] types = ScriptCommandAddDictionaryTypes.toTypes(this.types);
		final Iterator<Biome> it = this.pack.iterator();
		while(it.hasNext())
			BiomeDictionary.addTypes(ForgeRegistries.BIOMES.getResourceKey(it.next()).orElseThrow(), types);
	}

	public static Type[] toTypes(final String[] types) {
		for(final String type : types)
			if(!BiomeDictionary.Type.hasType(type))
				throw new IllegalArgumentException(String.format("Nonexisting type %s must be created before it can be used!", type));
		return Arrays.stream(types).map(BiomeDictionary.Type::getType).collect(Collectors.toList()).toArray(new Type[types.length]);
	}

}
