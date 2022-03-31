package me.superckl.biometweaker.script.command.world.gen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.LogHelper;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.ObfNameHelper;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "removeFeature")
public class ScriptCommandRemoveFeature extends ScriptCommand{

	private final BiomePackage pack;
	private final Set<ResourceLocation> keys;
	private final Field featureSettings;

	public ScriptCommandRemoveFeature(final BiomePackage pack, final String[] types) throws ClassNotFoundException {
		this.pack = pack;
		this.keys = Arrays.stream(types).map(ResourceLocation::new).collect(Collectors.toSet());
		this.featureSettings = ObfNameHelper.Fields.FEATURES.get();
	}

	@Override
	public void perform() throws IllegalArgumentException, IllegalAccessException{
		for(final Biome biome : this.pack) {
			this.makeFeaturesModifiable(biome);
			final List<List<Supplier<PlacedFeature>>> features = biome.getGenerationSettings().features();
			for(final List<Supplier<PlacedFeature>> list : features) {
				final Iterator<Supplier<PlacedFeature>> it = list.iterator();
				while(it.hasNext()) {
					final PlacedFeature feature = it.next().get();
					final ResourceLocation key = BuiltinRegistries.PLACED_FEATURE.getKey(feature);
					if (key != null && this.keys.contains(key)) {
						it.remove();
						LogHelper.debug(String.format("Removed feature %s from biome %s", key, biome));
					}
				}
			}
		}
	}

	private void makeFeaturesModifiable(final Biome biome) throws IllegalArgumentException, IllegalAccessException {
		final List<List<Supplier<PlacedFeature>>> features = WarningHelper.uncheckedCast(this.featureSettings.get(biome.getGenerationSettings()));
		final List<List<Supplier<PlacedFeature>>> newFeatures = new ArrayList<>();
		for (final List<Supplier<PlacedFeature>> list : features)
			newFeatures.add(new ArrayList<>(list));
		this.featureSettings.set(biome.getGenerationSettings(), newFeatures);
	}

}
