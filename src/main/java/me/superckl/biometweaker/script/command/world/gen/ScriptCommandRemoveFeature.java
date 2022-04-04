package me.superckl.biometweaker.script.command.world.gen;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.BiomeModificationManager.GenerationModification;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeFeature")
public class ScriptCommandRemoveFeature extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String stage;
	private final Set<ResourceLocation> keys;
	private final boolean isAll;

	public ScriptCommandRemoveFeature(final BiomePackage pack, final String stage, final String[] types) throws ClassNotFoundException {
		this.pack = pack;
		this.stage = stage;
		this.isAll = Arrays.stream(types).anyMatch("ALL"::equals);
		this.keys = this.isAll ? Collections.emptySet() : Arrays.stream(types).map(ResourceLocation::new).collect(Collectors.toSet());
	}

	@Override
	public void perform() throws IllegalArgumentException, IllegalAccessException{
		final boolean isAllStages = "ALL".equals(this.stage);
		final Decoration[] stages = isAllStages ? Decoration.values() : new Decoration[] {Decoration.valueOf(this.stage)};
		this.pack.locIterator().forEachRemaining(loc -> {
			final GenerationModification mod = BiomeModificationManager.forBiome(loc).getGeneration();
			Arrays.stream(stages).forEach(stage -> {
				if(this.isAll)
					mod.removeAllFeatures(stage);
				else
					this.keys.forEach(key -> mod.removeFeature(key, stage));
			});

		});
	}

}
