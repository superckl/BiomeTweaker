package me.superckl.biometweaker.script.command.effects;

import lombok.AllArgsConstructor;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addFarFogModifier")
@AllArgsConstructor
public class ScriptCommandAddFarFogModifier extends StagedScriptCommand{

	private final BiomePackage pack;
	private final float modifier;
	private final int minY, maxY;

	public ScriptCommandAddFarFogModifier(final BiomePackage pack, final float modifier) {
		this.pack = pack;
		this.modifier = modifier;
		this.minY = Integer.MIN_VALUE;
		this.maxY = Integer.MAX_VALUE;
	}

	@Override
	public void perform() throws Exception {
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getFog().addFarModifier(this.minY, this.maxY, this.modifier));
	}

}
