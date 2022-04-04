package me.superckl.biometweaker.script.command.world.gen;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addDicTypes")
@RequiredArgsConstructor
public class ScriptCommandAddDictionaryTypes extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String[] types;

	@Override
	public void perform() throws Exception {
		final Type[] types = ScriptCommandAddDictionaryTypes.toTypes(this.types);
		this.pack.locIterator().forEachRemaining(loc -> BiomeDictionary.addTypes(ResourceKey.create(Registry.BIOME_REGISTRY, loc), types));
	}

	public static Type[] toTypes(final String[] types) {
		for(final String type : types)
			if(!BiomeDictionary.Type.hasType(type))
				throw new IllegalArgumentException(String.format("Nonexisting type %s must be created before it can be used!", type));
		return Arrays.stream(types).map(BiomeDictionary.Type::getType).collect(Collectors.toList()).toArray(new Type[types.length]);
	}

}
