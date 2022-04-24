package me.superckl.biometweaker.script.command.world.gen;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

@AutoRegister(classes = TweakerScriptObject.class, name = "createDicType")
@RequiredArgsConstructor
public class ScriptCommandCreateDicType extends ScriptCommand{

	private final String name;
	private final String[] subTypes;

	@Override
	public void perform() throws Exception {
		if(BiomeDictionary.Type.hasType(this.name))
			throw new IllegalArgumentException(String.format("Type %s already exists!", this.name));
		final Type[] subTypes = ScriptCommandAddDictionaryTypes.toTypes(this.subTypes);
		BiomeDictionary.Type.getType(this.name, subTypes);
	}

}
