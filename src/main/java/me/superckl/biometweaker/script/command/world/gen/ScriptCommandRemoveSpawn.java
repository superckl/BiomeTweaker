package me.superckl.biometweaker.script.command.world.gen;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import me.superckl.api.biometweaker.BiomeLookup;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.BiomeModificationManager.MobSpawnModification;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeSpawn")
public class ScriptCommandRemoveSpawn extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String stage;
	private final Set<ResourceLocation> keys;
	private final boolean isAll;

	public ScriptCommandRemoveSpawn(final BiomePackage pack, final String stage, final String[] types) throws ClassNotFoundException {
		this.pack = pack;
		this.stage = stage;
		this.isAll = Arrays.stream(types).anyMatch("ALL"::equals);
		this.keys = this.isAll ? Collections.emptySet() : Arrays.stream(types).map(ResourceLocation::new).collect(Collectors.toSet());
	}

	@Override
	public void perform() throws IllegalArgumentException, IllegalAccessException{
		final boolean isAllStages = "ALL".equals(this.stage);
		final MobCategory[] types = isAllStages ? MobCategory.values() : new MobCategory[] {MobCategory.valueOf(this.stage)};
		this.pack.locIterator(BiomeLookup.fromForge()).forEachRemaining(loc -> {
			final MobSpawnModification mod = BiomeModificationManager.forBiome(loc).getSpawn();
			Arrays.stream(types).forEach(type -> {
				if(this.isAll)
					mod.removeAllSpawns(type);
				else
					this.keys.forEach(key -> mod.removeSpawn(key, type));
			});

		});
	}

}
