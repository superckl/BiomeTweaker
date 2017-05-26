package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.ScriptCommandManager;
import me.superckl.api.superscript.ScriptCommandManager.ApplicationStage;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ConstructorListing;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.integration.IntegrationManager;
import me.superckl.biometweaker.proxy.IProxy;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetReplacementStage;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetWorld;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.server.command.CommandInfo;
import me.superckl.biometweaker.server.command.CommandListBiomes;
import me.superckl.biometweaker.server.command.CommandOutput;
import me.superckl.biometweaker.server.command.CommandReload;
import me.superckl.biometweaker.server.command.CommandReloadScript;
import me.superckl.biometweaker.server.command.CommandSetBiome;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.DimensionHelper;
import me.superckl.biometweaker.util.EntityHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory = ModData.GUI_FACTORY, acceptableRemoteVersions = "*", certificateFingerprint = ModData.FINGERPRINT, dependencies = "before:biomesoplenty; after:galacticraftcore")
public class BiomeTweaker {

	@Instance(ModData.MOD_ID)
	@Getter
	private static BiomeTweaker instance;

	@Getter
	private boolean signed = true;

	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static IProxy proxy;

	@EventHandler
	public void onFingerprintViolation(final FMLFingerprintViolationEvent e){
		this.signed = false;
		LogHelper.warn("Hey... uhm... this is akward but, it looks like you're using an unofficial version of BiomeTweaker. Where exactly did you get this from?");
		LogHelper.warn("Unless I (superckl) sent you this version, don't expect to get any support for it.");
	}

	@EventHandler
	public void onConstruction(final FMLConstructionEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Construction", 1, true);
		bar.step("Populating script commands");
		try {
			ScriptCommandRegistry.INSTANCE.registerClassListing(BiomesScriptObject.class, BiomesScriptObject.populateCommands());
			ScriptCommandRegistry.INSTANCE.registerClassListing(TweakerScriptObject.class, TweakerScriptObject.populateCommands());
		} catch (final Exception e2) {
			LogHelper.error("Failed to populate command listings! Some tweaks may not be applied.");
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

		} catch (final Exception e2) {
			LogHelper.error("Failed to populate object listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onPreInit(final FMLPreInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker PreInitialization", 4, true);

		bar.step("Pre-Initializing Integration");
		IntegrationManager.INSTANCE.preInit();

		bar.step("Parsing scripts");
		this.parseScripts();

		bar.step("Registering handlers");
		BiomeTweaker.proxy.registerHandlers();

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.PRE_INIT);

		ProgressManager.pop(bar);
	}

	public void parseScripts(){
		try {
			LogHelper.info("Beginning script parsing...");
			long diff = 0;
			final long time = System.currentTimeMillis();
			for (final JsonElement listElement : Config.INSTANCE.getIncludes()) {
				File subFile = null;
				try {
					final String item = listElement.getAsString();
					subFile = new File(Config.INSTANCE.getWhereAreWe(), item);
					this.parseScript(subFile);
				} catch (final Exception e1) {
					LogHelper.error("Failed to parse a script file! File: " + subFile);
					e1.printStackTrace();
				}
			}
			final File scripts = new File(Config.INSTANCE.getWhereAreWe(), "scripts/");
			for (final File script : scripts.listFiles((FilenameFilter) (dir, name) -> name.endsWith(".cfg")))
				try {
					this.parseScript(script);
				} catch (final Exception e1) {
					LogHelper.error("Failed to parse a script file! File: " + script);
					e1.printStackTrace();
				}
			diff = System.currentTimeMillis() - time;
			LogHelper.info("Finished script parsing.");
			LogHelper.debug("Script parsing took "+diff+"ms.");
		} catch (final Exception e) {
			throw new RuntimeException("An unexpected error occurred while processing script files. Parsing may be incomplete. Ensure BiomeTweakerCore was called successfully.", e);
		}
	}

	public void parseScript(final File file) throws IOException{
		if(!file.exists()){
			LogHelper.debug(String.format("Subfile %s not found. A blank one will be generated.", file.getName()));
			file.createNewFile();
		}
		ScriptParser.parseScriptFile(file);
		//Reset other various stages
		Config.INSTANCE.getCommandManager().addCommand(new ScriptCommandSetReplacementStage("BIOME_BLOCKS"));
		Config.INSTANCE.getCommandManager().addCommand(new ScriptCommandSetWorld(null));
		Config.INSTANCE.getCommandManager().setCurrentStage(ScriptCommandManager.getDefaultStage());
	}

	@EventHandler
	public void onInit(final FMLInitializationEvent e) throws InterruptedException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 2, true);

		bar.step("Initializing Integration");
		IntegrationManager.INSTANCE.init();

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onPostInit(final FMLPostInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 2, true);

		bar.step("Post-Initializing Integration");
		IntegrationManager.INSTANCE.postInit();

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.POST_INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onLoadComplete(final FMLLoadCompleteEvent e) throws IOException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 2, true);

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.FINISHED_LOAD);

		bar.step("Generating output files");
		this.generateOutputFiles();

		ProgressManager.pop(bar);
	}

	public void generateOutputFiles() throws IOException{
		LogHelper.info("Generating Biome status report...");
		final JsonArray array = new JsonArray();
		final Iterator<Biome> it = Biome.REGISTRY.iterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			if(gen == null)
				continue;
			array.add(BiomeHelper.fillJsonObject(gen));
		}
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final File baseDir = new File(BiomeTweakerCore.mcLocation, "/config/BiomeTweaker/output/");
		final File biomeDir = new File(baseDir, "/biome/");
		biomeDir.mkdirs();

		for(final File file:biomeDir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();

		if(Config.INSTANCE.isOutputSeperateFiles())
			for(final JsonElement element:array){
				final JsonObject obj = (JsonObject) element;
				final File biomeOutput = new File(biomeDir, obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")+".json");
				if(biomeOutput.exists())
					biomeOutput.delete();
				biomeOutput.createNewFile();
				@Cleanup
				final
				BufferedWriter writer = new BufferedWriter(new FileWriter(biomeOutput));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{
			final File biomeOutput = new File(biomeDir, "BiomeTweaker - Biome Status Report.json");
			if(biomeOutput.exists())
				biomeOutput.delete();
			biomeOutput.createNewFile();
			@Cleanup
			final
			BufferedWriter writer = new BufferedWriter(new FileWriter(biomeOutput));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(array));
		}

		LogHelper.info("Generating LivingEntity status report...");

		final File entityDir = new File(baseDir, "/entity/");
		entityDir.mkdirs();

		for(final File file:entityDir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();

		final Iterator<EntityEntry> entityIt = ForgeRegistries.ENTITIES.getValues().iterator();
		final JsonArray entityArray = new JsonArray();

		while(entityIt.hasNext()){
			final EntityEntry entry = entityIt.next();
			if(!EntityLiving.class.isAssignableFrom(entry.getEntityClass()))
				continue;
			entityArray.add(EntityHelper.populateObject(entry));

		}

		if(Config.INSTANCE.isOutputSeperateFiles())
			for(final JsonElement ele:entityArray){
				final JsonObject obj = (JsonObject) ele;
				final File entityOutput = new File(entityDir, obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")+".json");
				if(entityOutput.exists())
					entityOutput.delete();
				entityOutput.createNewFile();
				@Cleanup
				final BufferedWriter writer = new BufferedWriter(new FileWriter(entityOutput));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{

			final File entityOutput = new File(entityDir, "BiomeTweaker - EntityLiving Status Report.json");
			if(entityOutput.exists())
				entityOutput.delete();
			entityOutput.createNewFile();
			@Cleanup
			final BufferedWriter writer = new BufferedWriter(new FileWriter(entityOutput));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(entityArray));
		}

		LogHelper.info("Generating Dimension status report...");

		final File dimDir = new File(baseDir, "/dimension/");
		dimDir.mkdirs();

		for(final File file:dimDir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();

		final DimensionType[] dimTypes = DimensionType.values();
		final JsonArray dimArray = new JsonArray();

		for(final DimensionType dimType:dimTypes)
			dimArray.add(DimensionHelper.populateObject(dimType));

		if(Config.INSTANCE.isOutputSeperateFiles())
			for(final JsonElement ele:dimArray){
				final JsonObject obj = (JsonObject) ele;
				final File dimOutput = new File(dimDir, obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")+".json");
				if(dimOutput.exists())
					dimOutput.delete();
				dimOutput.createNewFile();
				@Cleanup
				final BufferedWriter writer = new BufferedWriter(new FileWriter(dimOutput));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{

			final File dimOutput = new File(entityDir, "BiomeTweaker - Dimension Status Report.json");
			if(dimOutput.exists())
				dimOutput.delete();
			dimOutput.createNewFile();
			@Cleanup
			final BufferedWriter writer = new BufferedWriter(new FileWriter(dimOutput));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(entityArray));
		}

	}

	@EventHandler
	public void onServerStarting(final FMLServerStartingEvent e){
		e.registerServerCommand(new CommandReload());
		e.registerServerCommand(new CommandInfo());
		e.registerServerCommand(new CommandOutput());
		e.registerServerCommand(new CommandListBiomes());
		e.registerServerCommand(new CommandSetBiome());
		e.registerServerCommand(new CommandReloadScript());
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.SERVER_STARTING);
	}

	@EventHandler
	public void onServerStarted(final FMLServerStartedEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.SERVER_STARTED);
	}

}
