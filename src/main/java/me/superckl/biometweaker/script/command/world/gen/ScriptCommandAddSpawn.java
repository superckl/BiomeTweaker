package me.superckl.biometweaker.script.command.world.gen;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addSpawn")
public class ScriptCommandAddSpawn extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String stage;
	private final SpawnerData data;

	public ScriptCommandAddSpawn(final BiomePackage pack, final String stage, final String rLoc, final int weight, final int minCount, final int maxCount) throws ClassNotFoundException {
		this.pack = pack;
		this.stage = stage;
		this.data = new SpawnerData(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(rLoc)), weight, minCount, maxCount);
	}

	@Override
	public void perform() throws Exception {
		final boolean isAllStages = "ALL".equals(this.stage);
		final MobCategory[] types = isAllStages ? MobCategory.values() : new MobCategory[] {MobCategory.valueOf(this.stage)};
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getSpawn().addSpawn(this.data, types));
	}

}
