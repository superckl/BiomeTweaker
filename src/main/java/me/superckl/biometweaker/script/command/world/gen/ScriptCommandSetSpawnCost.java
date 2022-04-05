package me.superckl.biometweaker.script.command.world.gen;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setSpawnCost")
public class ScriptCommandSetSpawnCost extends StagedScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation rLoc;
	private final double costPer;
	private final double maxCost;

	public ScriptCommandSetSpawnCost(final BiomePackage pack, final ResourceLocation rLoc, final float perCost, final float maxCost) throws ClassNotFoundException {
		this.pack = pack;
		this.rLoc = rLoc;
		this.costPer = perCost;
		this.maxCost = maxCost;
	}

	@Override
	public void perform() {
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getSpawn().setCost(this.rLoc, this.costPer, this.maxCost));
	}

}
