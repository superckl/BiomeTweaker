package me.superckl.biometweaker.script.command.world.gen;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.BiomeModificationManager.GenerationModification;
import me.superckl.biometweaker.script.command.BiomeScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeCarver")
public class ScriptCommandRemoveCarver extends BiomeScriptCommand{

	private final BiomePackage pack;
	private final String stage;
	private final Set<ResourceLocation> keys;
	private final boolean isAll;

	public ScriptCommandRemoveCarver(final BiomePackage pack, final String stage, final String[] types) throws ClassNotFoundException {
		this.pack = pack;
		this.stage = stage;
		this.isAll = Arrays.stream(types).anyMatch("ALL"::equals);
		this.keys = this.isAll ? Collections.emptySet() : Arrays.stream(types).map(ResourceLocation::new).collect(Collectors.toSet());
	}

	@Override
	public void perform() throws IllegalArgumentException, IllegalAccessException{
		final boolean isAllStages = "ALL".equals(this.stage);
		final Carving[] stages = isAllStages ? Carving.values() : new Carving[] {Carving.valueOf(this.stage)};
		this.pack.locIterator().forEachRemaining(loc -> {
			final GenerationModification mod = BiomeModificationManager.forBiome(loc).getGeneration();
			Arrays.stream(stages).forEach(stage -> {
				if(this.isAll)
					mod.removeAllCarvers(stage);
				else
					this.keys.forEach(key -> mod.removeCarver(key, stage));
			});

		});
	}

}
