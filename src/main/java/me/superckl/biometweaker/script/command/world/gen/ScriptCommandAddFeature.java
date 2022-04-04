package me.superckl.biometweaker.script.command.world.gen;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addFeature")
public class ScriptCommandAddFeature extends StagedScriptCommand{

	private final BiomePackage pack;
	private final String stage;
	private final String name;

	public ScriptCommandAddFeature(final BiomePackage pack, final String stage, final String name) throws ClassNotFoundException {
		this.pack = pack;
		this.stage = stage;
		this.name = name;
	}

	@Override
	public void perform() throws IllegalArgumentException, IllegalAccessException{
		final boolean isAllStages = "ALL".equals(this.stage);
		final Decoration[] stages = isAllStages ? Decoration.values() : new Decoration[] {Decoration.valueOf(this.stage)};
		final ResourceLocation key = new ResourceLocation(this.name);
		final Holder<PlacedFeature> feature = BuiltinRegistries.PLACED_FEATURE.getHolder(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
				key)).orElseThrow(() -> new IllegalArgumentException(String.format("Failed to find placed feature %s. Did you register it?", key)));
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getGeneration().addFeature(feature, stages));
	}

}
