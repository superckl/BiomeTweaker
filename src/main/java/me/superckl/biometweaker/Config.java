package me.superckl.biometweaker;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Getter;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Getter
public class Config {

	@Getter
	private static Config instance;

	private final BooleanValue separateFiles;
	private final BooleanValue outputBiomes;
	private final BooleanValue reducedBiomeOutput;
	private final BooleanValue outputEntities;
	private final BooleanValue outputDims;
	private final BooleanValue outputCarvers;
	private final BooleanValue outputFeatures;


	private Config(final ForgeConfigSpec.Builder builder) {


		builder.comment("If true, BiomeTweaker will output separate files for each object (e.g., biomes)");
		this.separateFiles = builder.define("Output.Separate Files", true);


		builder.comment("Whether or not BiomeTweaker should output biome info. You can turn this off if you don't need it to shave output time.",
				"You must run the biometweaker output command in game.");
		this.outputBiomes = builder.define("Output.Biomes", true);
		builder.comment("Set to true to have BiomeTweaker only output biome data that is sent from server to client. This should be unnecessary, but can alleviate issues with mod compability.");
		this.reducedBiomeOutput = builder.define("Output.Reduced Biome", false);
		builder.comment("Whether or not BiomeTweaker should output entity info. You can turn this off if you don't need it to shave output time.",
				"You must run the biometweaker output command in game.");
		this.outputEntities = builder.define("Output.Entities", true);
		builder.comment("Whether or not BiomeTweaker should output dimension info. You can turn this off if you don't need it to shave output time.",
				"You must run the biometweaker output command in game.");
		this.outputDims = builder.define("Output.Dimensions", true);
		builder.comment("Whether or not BiomeTweaker should output feature info. You can turn this off if you don't need it to shave output time.",
				"You must run the biometweaker output command in game.");
		this.outputFeatures = builder.define("Output.Features", true);
		builder.comment("Whether or not BiomeTweaker should output carver info. You can turn this off if you don't need it to shave output time.",
				"You must run the biometweaker output command in game.");
		this.outputCarvers = builder.define("Output.Carvers", true);
	}

	public static ForgeConfigSpec setup() {
		if (Config.instance != null)
			throw new IllegalStateException("Config has already been setup!");
		final Pair<Config, ForgeConfigSpec> specPair =  new ForgeConfigSpec.Builder().configure(Config::new);
		Config.instance = specPair.getKey();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(Config.instance::onConfigLoad);
		return specPair.getValue();
	}

	private void onConfigLoad(final ModConfigEvent e) {

	}

}
