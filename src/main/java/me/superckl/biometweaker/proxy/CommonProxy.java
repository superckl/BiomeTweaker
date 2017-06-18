package me.superckl.biometweaker.proxy;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.PropertyBlockState;
import me.superckl.api.biometweaker.property.PropertyBlockStateMeta;
import me.superckl.api.biometweaker.property.PropertyField;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ConstructorListing;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import me.superckl.biometweaker.common.world.biome.property.PropertyDecorationPerChunk;
import me.superckl.biometweaker.common.world.biome.property.PropertyGenStrongholds;
import me.superckl.biometweaker.common.world.biome.property.PropertyGenTallPlants;
import me.superckl.biometweaker.common.world.biome.property.PropertyGenVillages;
import me.superckl.biometweaker.common.world.biome.property.PropertyGenWeight;
import me.superckl.biometweaker.common.world.biome.property.PropertySpawnBiome;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.script.object.decoration.ClusterDecorationScriptObject;
import me.superckl.biometweaker.script.object.decoration.OreDecorationScriptObject;
import me.superckl.biometweaker.script.object.decoration.TreesDecorationScriptObject;
import me.superckl.biometweaker.util.LogHelper;
import me.superckl.biometweaker.util.ObfNameHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		final BiomeEventHandler handler = new BiomeEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.TERRAIN_GEN_BUS.register(handler);
		MinecraftForge.ORE_GEN_BUS.register(handler);
		final EntityEventHandler eHandler = new EntityEventHandler();
		MinecraftForge.EVENT_BUS.register(eHandler);
	}

	@Override
	public void initProperties() {
		BiomePropertyManager.NAME = new PropertyField<>(Biome.class, ObfNameHelper.Fields.BIOMENAME.getName(), String.class);
		BiomePropertyManager.HEIGHT = new PropertyField<>(Biome.class, ObfNameHelper.Fields.BASEHEIGHT.getName(), Float.class);
		BiomePropertyManager.HEIGHT_VARIATION = new PropertyField<>(Biome.class, ObfNameHelper.Fields.HEIGHTVARIATION.getName(), Float.class);
		BiomePropertyManager.TOP_BLOCK = new PropertyBlockState(Biome.class, ObfNameHelper.Fields.TOPBLOCK.getName());
		BiomePropertyManager.TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, ObfNameHelper.Fields.TOPBLOCK.getName());
		BiomePropertyManager.FILLER_BLOCK = new PropertyBlockState(Biome.class, ObfNameHelper.Fields.FILLERBLOCK.getName());
		BiomePropertyManager.FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class, ObfNameHelper.Fields.FILLERBLOCK.getName());
		BiomePropertyManager.TEMPERATURE = new PropertyField<>(Biome.class, ObfNameHelper.Fields.TEMPERATURE.getName(), Float.class);
		BiomePropertyManager.HUMIDITY = new PropertyField<>(Biome.class, ObfNameHelper.Fields.RAINFALL.getName(), Float.class);
		BiomePropertyManager.WATER_TINT = new PropertyField<>(Biome.class, ObfNameHelper.Fields.WATERCOLOR.getName(), Integer.class);
		BiomePropertyManager.ENABLE_RAIN = new PropertyField<>(Biome.class, ObfNameHelper.Fields.ENABLERAIN.getName(), Boolean.class);
		BiomePropertyManager.ENABLE_SNOW = new PropertyField<>(Biome.class, ObfNameHelper.Fields.ENABLESNOW.getName(), Boolean.class);
		BiomePropertyManager.WATER_COLOR = new PropertyField<>(Biome.class, "waterColor", Integer.class);
		BiomePropertyManager.GEN_WEIGHT = new PropertyGenWeight();
		BiomePropertyManager.GEN_VILLAGES = new PropertyGenVillages();
		BiomePropertyManager.GEN_STRONGHOLDS = new PropertyGenStrongholds();
		BiomePropertyManager.IS_SPAWN_BIOME = new PropertySpawnBiome();
		BiomePropertyManager.GEN_TALL_PLANTS = new PropertyGenTallPlants();
		BiomePropertyManager.WATERLILY_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LILYPAD);
		BiomePropertyManager.TREES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.TREE);
		BiomePropertyManager.FLOWERS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.FLOWERS);
		BiomePropertyManager.GRASS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.GRASS);
		BiomePropertyManager.DEAD_BUSH_PER_CHUNK = new PropertyDecorationPerChunk(EventType.DEAD_BUSH);
		BiomePropertyManager.MUSHROOMS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SHROOM);
		BiomePropertyManager.REEDS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.REED);
		BiomePropertyManager.CACTI_PER_CHUNK = new PropertyDecorationPerChunk(EventType.CACTUS);
		BiomePropertyManager.SAND_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SAND);
		BiomePropertyManager.CLAY_PER_CHUNK = new PropertyDecorationPerChunk(EventType.CLAY);
		BiomePropertyManager.BIG_MUSHROOMS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.BIG_SHROOM);
		BiomePropertyManager.DESERT_WELLS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.DESERT_WELL);
		BiomePropertyManager.FOSSILS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.FOSSIL);
		BiomePropertyManager.ICE_PER_CHUNK = new PropertyDecorationPerChunk(EventType.ICE);
		BiomePropertyManager.LAKES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LAKE_WATER);
		BiomePropertyManager.LAVA_LAKES_PER_CHUNK = new PropertyDecorationPerChunk(EventType.LAKE_LAVA);
		BiomePropertyManager.PUMPKINS_PER_CHUNK = new PropertyDecorationPerChunk(EventType.PUMPKIN);
		BiomePropertyManager.ROCK_PER_CHUNK = new PropertyDecorationPerChunk(EventType.ROCK);
		BiomePropertyManager.SAND2_PER_CHUNK = new PropertyDecorationPerChunk(EventType.SAND_PASS2);

		if(BiomeTweaker.getInstance().isTweakEnabled("oceanTopBlock")){
			BiomePropertyManager.OCEAN_TOP_BLOCK = new PropertyBlockState(Biome.class, "oceanTopBlock");
			BiomePropertyManager.OCEAN_TOP_BLOCK_META = new PropertyBlockStateMeta(Biome.class, "oceanTopBlock");
		}
		if(BiomeTweaker.getInstance().isTweakEnabled("oceanFillerBlock")) {
			BiomePropertyManager.OCEAN_FILLER_BLOCK = new PropertyBlockState(Biome.class, "oceanFillerBlock");
			BiomePropertyManager.OCEAN_FILLER_BLOCK_META = new PropertyBlockStateMeta(Biome.class,"oceanFillerBlock");
		}
		if(BiomeTweaker.getInstance().isTweakEnabled("actualFillerBlocks"))
			BiomePropertyManager.ACTUAL_FILLER_BLOCKS = new PropertyField<>(Biome.class, "actualFillerBlocks", IBlockState[].class);
		if(BiomeTweaker.getInstance().isTweakEnabled("grassColor"))
			BiomePropertyManager.GRASS_COLOR = new PropertyField<>(Biome.class, "grassColor", Integer.class);
		if(BiomeTweaker.getInstance().isTweakEnabled("foliageColor"))
			BiomePropertyManager.FOLIAGE_COLOR = new PropertyField<>(Biome.class, "foliageColor", Integer.class);
		if(BiomeTweaker.getInstance().isTweakEnabled("skyColor"))
			BiomePropertyManager.SKY_COLOR = new PropertyField<>(Biome.class, "skyColor", Integer.class);

		BiomePropertyManager.populatePropertyMap();
	}

	@Override
	public void setupScripts() {
		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(BiomesScriptObject.class, BiomesScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate BiomeScriptObject command listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(TweakerScriptObject.class, TweakerScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate Tweaker command listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(OreDecorationScriptObject.class, OreDecorationScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate OreDecorationScriptObject command listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(TreesDecorationScriptObject.class, TreesDecorationScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate TreesDecorationScriptObject command listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(ClusterDecorationScriptObject.class, ClusterDecorationScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate ClusterDecorationScriptObject command listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		ScriptHandler.registerStaticObject("Tweaker", TweakerScriptObject.class);

		try {
			ConstructorListing<ScriptObject> listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomes", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.TYPE_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomesOfTypes", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.ALL_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forAllBiomes", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.ALL_BUT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forAllBiomesExcept", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.INTERSECT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("intersectionOf", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.SUBTRACT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("subtractFrom", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.PROPERTY_RANGE_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomesWithPropertyRange", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(), OreDecorationScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("newOreDecoration", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(), TreesDecorationScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("newTreeDecoration", listing);

			listing = new ConstructorListing<ScriptObject>();
			listing.addEntry(Lists.newArrayList(), ClusterDecorationScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("newClusterDecoration", listing);

		} catch (final Exception e2) {
			LogHelper.error("Failed to populate object listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}
	}

}
