package me.superckl.biometweaker.script.command.world.gen;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.BiomeScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setSpawnCost")
public class ScriptCommandSetSpawnCost extends BiomeScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation rLoc;
	private final int costPer;
	private final int maxCost;

	public ScriptCommandSetSpawnCost(final BiomePackage pack, final String rLoc, final int perCost, final int maxCost) throws ClassNotFoundException {
		this.pack = pack;
		this.rLoc = new ResourceLocation(rLoc);
		this.costPer = perCost;
		this.maxCost = maxCost;
	}

	@Override
	public void perform() {
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getSpawn().setCost(this.rLoc, this.costPer, this.maxCost));
	}

}
