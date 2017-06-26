package me.superckl.biometweaker.integration.bop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.enums.BOPClimates;
import biomesoplenty.api.enums.BOPClimates.WeightedBiomeEntry;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerationManager;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.integration.IIntegrationModule;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandAddBOPWorldType;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandAddSubBiomeBOP;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandAddToGenerationBOP;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandRemoveBOP;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandRemoveBOPWorldType;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandRemoveGeneratorBOP;
import me.superckl.biometweaker.integration.bop.script.ScriptCommandRemoveSubBiomeBOP;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;

public class BOPIntegrationModule implements IIntegrationModule{

	@Override
	public void preInit() {

		try {
			LogHelper.info("Registering BOP script commands...");

			//TweakerObject
			ScriptCommandListing listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandRemoveBOPWorldType.class.getDeclaredConstructor(String.class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeExcludedBOPWorldType", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), ScriptCommandAddBOPWorldType.class.getDeclaredConstructor(String.class));
			ScriptCommandRegistry.INSTANCE.registerListing("addExcludedBOPWorldType", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
					ScriptCommandAddToGenerationBOP.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
			ScriptCommandRegistry.INSTANCE.registerListing("addToGenerationBOP", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandRemoveBOP.class.getDeclaredConstructor(IBiomePackage.class));
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveBOP.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeBOP", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveGeneratorBOP.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeGeneratorBOP", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()),
					ScriptCommandAddSubBiomeBOP.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
			ScriptCommandRegistry.INSTANCE.registerListing("addSubBiomeBOP", listing, TweakerScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()),
					ScriptCommandRemoveSubBiomeBOP.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeSubBiomeBOP", listing, TweakerScriptObject.class);


			//BiomesObject
			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
					ScriptCommandAddToGenerationBOP.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
			ScriptCommandRegistry.INSTANCE.registerListing("addToGenerationBOP", listing, BiomesScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(new ArrayList<ParameterWrapper>(), ScriptCommandRemoveBOP.class.getDeclaredConstructor(IBiomePackage.class));
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveBOP.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeBOP", listing, BiomesScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveGeneratorBOP.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeGeneratorBOP", listing, BiomesScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandAddSubBiomeBOP.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
			ScriptCommandRegistry.INSTANCE.registerListing("addSubBiomeBOP", listing, BiomesScriptObject.class);

			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper()), ScriptCommandRemoveSubBiomeBOP.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
			ScriptCommandRegistry.INSTANCE.registerListing("removeSubBiomeBOP", listing, BiomesScriptObject.class);

		} catch (final Exception e) {
			LogHelper.error("Failed to register BOP script commands! Some commands may not work properly!");
			e.printStackTrace();
		}

	}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public String getName() {
		return "Biomes O' Plenty Integration";
	}

	public static IExtendedBiome getExtendedBiome(final Biome biome){
		IExtendedBiome eBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome);
		if(eBiome == null){
			eBiome = new ExtendedBiomeWrapper(biome);
			BOPBiomes.REG_INSTANCE.registerBiome(eBiome, eBiome.getBaseBiome().biomeName.toLowerCase());
		}
		return eBiome;
	}

	@Override
	public void addBiomeInfo(Biome biome, final JsonObject obj) {
		final IExtendedBiome eBiome = BOPIntegrationModule.getExtendedBiome(biome);
		biome = eBiome.getBaseBiome();
		final IGenerationManager gManager = eBiome.getGenerationManager();
		final JsonObject genNames = new JsonObject();
		for(final GeneratorStage stage:GeneratorStage.values()){
			final Collection<IGenerator> gens = gManager.getGeneratorsForStage(stage);
			if(gens.isEmpty())
				continue;
			final JsonArray subArray = new JsonArray();
			final Iterator<IGenerator> it = gens.iterator();
			while(it.hasNext()){
				final JsonObject subSubObj = new JsonObject();
				final IGenerator gen = it.next();
				subSubObj.addProperty("ID", gen.getIdentifier());
				subSubObj.addProperty("Name", gen.getName());
				subArray.add(subSubObj);
			}
			genNames.add(stage.name() + " Generators", subArray);
		}
		obj.add("BOP Generators", genNames);

		obj.addProperty("BOP Owner", eBiome.getBiomeOwner().name());
		final Gson gson = new Gson();
		obj.add("BOP Weight Map", gson.toJsonTree(eBiome.getWeightMap()));
		try {
			final JsonObject weights = new JsonObject();
			for(final BOPClimates climate:BOPClimates.values()){
				final List<WeightedBiomeEntry> entries = WarningHelper.uncheckedCast(BOPBiomeProperties.LAND_BIOMES.get(climate));
				final JsonArray subArray = new JsonArray();
				for(final WeightedBiomeEntry entry:entries)
					if(Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(biome))
						subArray.add(new JsonPrimitive(entry.weight));
				if(subArray.size() > 0)
					weights.add(climate.name(), subArray);
			}
			obj.add("BOP Climate Weights", weights);
		} catch (final Exception e) {
			LogHelper.error("Failed to retrieve all BOP biome info!");
			e.printStackTrace();
		}
	}

}
