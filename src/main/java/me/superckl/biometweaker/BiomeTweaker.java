package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Cleanup;
import lombok.Getter;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.proxy.IProxy;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.ScriptCommandManager.ApplicationStage;
import me.superckl.biometweaker.server.command.CommandInfo;
import me.superckl.biometweaker.server.command.CommandOutput;
import me.superckl.biometweaker.server.command.CommandReload;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import me.superckl.biometweaker.util.ReflectionHelper;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory = ModData.GUI_FACTORY, acceptableRemoteVersions = "*", certificateFingerprint = ModData.FINGERPRINT)
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
	public void onPreInit(final FMLPreInitializationEvent e){
		LogHelper.info("Beginning script parsing...");
		long time = System.currentTimeMillis();
		for(final JsonElement listElement:Config.INSTANCE.getIncludes()){
			File subFile = null;
			try {
				final String item = listElement.getAsString();
				subFile = new File(Config.INSTANCE.getWhereAreWe(), item);
				if(!subFile.exists()){
					LogHelper.debug("Included subfile not found. A blank one will be generated.");
					subFile.createNewFile();
				}
				ScriptParser.parseScriptFile(subFile);
				Config.INSTANCE.getCommandManager().setCurrentStage(ApplicationStage.FINISHED_LOAD);
			} catch (Exception e1) {
				LogHelper.error("Failed to parse a script file! File: "+subFile);
				e1.printStackTrace();
			}
		}
		long diff = System.currentTimeMillis()-time;
		LogHelper.debug("Script parsing took "+diff+"ms.");
		BiomeTweaker.proxy.registerHandlers();
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.PRE_INIT);
	}

	@EventHandler
	public void onInit(final FMLInitializationEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.INIT);
	}

	@EventHandler
	public void onPostInit(final FMLPostInitializationEvent e){
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.POST_INIT);
	}

	@EventHandler
	public void onLoadComplete(final FMLLoadCompleteEvent e) throws IOException{
		Config.INSTANCE.getCommandManager().applyCommandsFor(ApplicationStage.FINISHED_LOAD);
		this.generateOutputFiles();
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
