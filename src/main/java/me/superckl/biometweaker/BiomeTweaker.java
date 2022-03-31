package me.superckl.biometweaker;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import lombok.Cleanup;
import lombok.Getter;
import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.command.BasicScriptCommandManager;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(BiomeTweakerAPI.MOD_ID)
public class BiomeTweaker {

	@Getter
	private static BiomeTweaker INSTANCE;
	@Getter
	private final BasicScriptCommandManager commandManager;
	private final File scriptDir;
	private final File outputDir;

	public BiomeTweaker() {
		BiomeTweaker.INSTANCE = this;
		LogHelper.setLogger(LogManager.getFormatterLogger(BiomeTweakerAPI.MOD_ID));
		ScriptSetup.setupScripts(ModList.get().getAllScanData());
		ScriptSetup.initProperties();

		this.scriptDir = FMLPaths.CONFIGDIR.get().resolve(BiomeTweakerAPI.MOD_ID+"/scripts/").toFile();
		this.outputDir = FMLPaths.CONFIGDIR.get().resolve(BiomeTweakerAPI.MOD_ID+"/output/").toFile();

		this.commandManager = new BasicScriptCommandManager();
		ScriptCommandRegistry.INSTANCE.registerScriptCommandManager(BiomeTweakerAPI.MOD_ID, this.commandManager);

		BiomeTweakerAPI.setBiomesScriptObjectClass(BiomesScriptObject.class);
		BiomeTweakerAPI.setTweakerScriptObjectClass(TweakerScriptObject.class);
		BiomeTweakerAPI.setCommandAdder(command -> this.commandManager.addCommand(command));

		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::commonSetup);
		bus.addListener(this::loadComplete);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.setup());

		this.parseScripts();

		this.commandManager.applyCommandsFor(ApplicationStage.CONSTRUCTION);
	}

	private void commonSetup(final FMLCommonSetupEvent e) {
		e.enqueueWork(() -> this.commandManager.applyCommandsFor(ApplicationStage.SETUP));
	}

	private void loadComplete(final FMLLoadCompleteEvent e) {
		e.enqueueWork(() -> this.commandManager.applyCommandsFor(ApplicationStage.LOAD_COMPLETE));
		this.generateOutputFiles();
	}

	public void parseScripts(){
		try {
			LogHelper.info("Beginning script parsing...");
			long diff;
			final long time = System.currentTimeMillis();
			this.scriptDir.mkdirs();
			for (final File script : this.scriptDir.listFiles((dir, name) -> name.endsWith(".cfg")))
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
		//this.commandManager.addCommand(new ScriptCommandSetPlacementStage("BIOME_BLOCKS"));
		//this.commandManager.addCommand(new ScriptCommandSetWorld(null));
		this.commandManager.setCurrentStage(BasicScriptCommandManager.getDefaultStage());
	}

	public void generateOutputFiles(){
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final File baseDir = this.outputDir;
		if(Config.getInstance().getOutputBiomes().get())
			try {
				LogHelper.info("Generating Biome status report...");
				final JsonArray array = new JsonArray();
				final Iterator<Biome> it = ForgeRegistries.BIOMES.getValues().iterator();
				while(it.hasNext()){
					final Biome gen = it.next();
					if(gen == null)
						continue;

					array.add(Biome.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, gen).result().orElseThrow());
				}
				final File biomeDir = new File(baseDir, "/biome/");
				biomeDir.mkdirs();

				for(final File file:biomeDir.listFiles())
					if(file.getName().endsWith(".json"))
						file.delete();

				if(Config.getInstance().getSeparateFiles().get())
					for(final JsonElement element:array){
						final JsonObject obj = (JsonObject) element;
						final StringBuilder fileName = new StringBuilder(obj.get("forge:registry_name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(".json");
						final File biomeOutput = new File(biomeDir, fileName.toString());
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
			} catch (final Exception e) {
				LogHelper.error("Caught an exception while generating biome status report!");
				e.printStackTrace();
			}

		File entityDir;
		JsonArray entityArray;
		if(Config.getInstance().getOutputEntities().get())
			try {
				LogHelper.info("Generating LivingEntity status report...");

				entityDir = new File(baseDir, "/entity/");
				entityDir.mkdirs();

				for(final File file:entityDir.listFiles())
					if(file.getName().endsWith(".json"))
						file.delete();

				final Iterator<EntityType<?>> entityIt = ForgeRegistries.ENTITIES.getValues().iterator();
				entityArray = new JsonArray();

				while(entityIt.hasNext()){
					final EntityType<?> entry = entityIt.next();
					if(!LivingEntity.class.isAssignableFrom(entry.getBaseClass()))
						continue;
					entityArray.add(ForgeRegistries.ENTITIES.getCodec().encodeStart(JsonOps.INSTANCE, entry).result().orElseThrow());

				}

				if(Config.getInstance().getSeparateFiles().get())
					for(final JsonElement ele:entityArray){
						final JsonObject obj = (JsonObject) ele;
						final StringBuilder fileName = new StringBuilder(obj.get("forge:registry_name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(".json");
						final File entityOutput = new File(entityDir, fileName.toString());
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
			} catch (final Exception e) {
				LogHelper.error("Caught an exception while generating Living Entity status report!");
				e.printStackTrace();
			}

	}

}
