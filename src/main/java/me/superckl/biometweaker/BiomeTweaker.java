package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.ScriptCommandManager.ApplicationStage;
import me.superckl.api.superscript.ScriptCommandRegistry;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ConstructorListing;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.proxy.IProxy;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.server.command.CommandInfo;
import me.superckl.biometweaker.server.command.CommandListBiomes;
import me.superckl.biometweaker.server.command.CommandOutput;
import me.superckl.biometweaker.server.command.CommandReload;
import me.superckl.biometweaker.server.command.CommandReloadScript;
import me.superckl.biometweaker.server.command.CommandSetBiome;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import me.superckl.biometweaker.util.ReflectionHelper;
import net.minecraft.world.biome.BiomeGenBase;
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

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory = ModData.GUI_FACTORY, acceptableRemoteVersions = "*", certificateFingerprint = ModData.FINGERPRINT, dependencies = "before:BiomesOPlenty; after:GalacticraftCore")
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

		ScriptHandler.registerStaticObject("Tweaker", new TweakerScriptObject());

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

		} catch (final Exception e2) {
			LogHelper.error("Failed to populate object listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onPreInit(final FMLPreInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker PreInitialization", 3, true);

		bar.step("Parsing scripts");
		this.parseScripts();

		bar.step("Registering handlers");
		BiomeTweaker.proxy.registerHandlers();

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.PRE_INIT);

		ProgressManager.pop(bar);
	}

	public void parseScripts(){
		LogHelper.info("Beginning script parsing...");
		final long time = System.currentTimeMillis();
		for(final JsonElement listElement:Config.INSTANCE.getIncludes()){
			File subFile = null;
			try {
				final String item = listElement.getAsString();
				subFile = new File(Config.INSTANCE.getWhereAreWe(), item);
				this.parseScript(subFile);
			} catch (final Exception e1) {
				LogHelper.error("Failed to parse a script file! File: "+subFile);
				e1.printStackTrace();
			}
		}
		final long diff = System.currentTimeMillis()-time;
		LogHelper.info("Finished script parsing.");
		LogHelper.debug("Script parsing took "+diff+"ms.");
	}

	public void parseScript(final File file) throws IOException{
		if(!file.exists()){
			LogHelper.debug(String.format("Subfile %s not found. A blank one will be generated.", file.getName()));
			file.createNewFile();
		}
		ScriptParser.parseScriptFile(file);
		Config.INSTANCE.getCommandManager().setCurrentStage(ApplicationStage.FINISHED_LOAD);
	}

	@EventHandler
	public void onInit(final FMLInitializationEvent e) throws InterruptedException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 1, true);

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onPostInit(final FMLPostInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 1, true);

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.POST_INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onLoadComplete(final FMLLoadCompleteEvent e) throws IOException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 2, true);

		bar.step("Applying scripts");
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.FINISHED_LOAD);

		bar.step("Generating biome output files");
		this.generateOutputFiles();

		ProgressManager.pop(bar);
	}

	public void generateOutputFiles() throws IOException{
		LogHelper.info("Generating biome status report...");
		final JsonArray array = new JsonArray();
		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray()){
			if(gen == null)
				continue;
			array.add(BiomeHelper.fillJsonObject(gen));
		}
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final File dir = new File(BiomeTweakerCore.mcLocation, "/config/BiomeTweaker/output/");
		dir.mkdirs();
		for(final File file:dir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();
		if(Config.INSTANCE.isOutputSeperateFiles())
			for(final JsonElement element:array){
				final JsonObject obj = (JsonObject) element;
				final File output = new File(dir, ""+obj.get("Name").getAsString()+".json");
				if(output.exists())
					output.delete();
				output.createNewFile();
				@Cleanup
				final
				BufferedWriter writer = new BufferedWriter(new FileWriter(output));
				writer.newLine();
				writer.write(gson.toJson(obj));
			}
		else{
			final File output = new File(dir, "BiomeTweaker - Biome Status Report.json");
			if(output.exists())
				output.delete();
			output.createNewFile();
			@Cleanup
			final
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("//Yeah, it's a doozy.");
			writer.newLine();
			writer.write(gson.toJson(array));
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

	//Begin compat. e.g. load biome classes that get loaded too late.

	private final String[] galactCoreClasses = new String[] {"micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseMoon", "micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseOrbit"};
	private final String[] galactMarsClasses = new String[] {"micdoodle8.mods.galacticraft.planets.mars.world.gen.BiomeGenBaseMars"};

	//@Optional.Method(modid = "GalacticraftCore")
	@EventHandler
	public void GalactCoreCompat(final FMLPreInitializationEvent e){
		LogHelper.info("Attempting to load compat. biome classes...");
		this.loadCompatClasses(this.galactCoreClasses);
		this.loadCompatClasses(this.galactMarsClasses);
	}

	private void loadCompatClasses(final String ... classes){
		for(final String clazz:classes)
			if(ReflectionHelper.tryLoadClass(clazz) == null)
				LogHelper.debug("Failed to load compat. biome class "+clazz);
	}

}
