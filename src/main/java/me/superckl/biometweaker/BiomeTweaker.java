package me.superckl.biometweaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Cleanup;
import lombok.Getter;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.command.BasicScriptCommandManager;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.integration.IntegrationManager;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetPlacementStage;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetWorld;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory = ModData.GUI_FACTORY,
acceptableRemoteVersions = "*", certificateFingerprint = ModData.FINGERPRINT, dependencies = ModData.DEPENDENCIES)
public class BiomeTweaker{

	@Instance(ModData.MOD_ID)
	@Getter
	private static BiomeTweaker instance;

	@Getter
	private boolean signed = true;

	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static me.superckl.biometweaker.proxy.SidedProxy proxy;

	@Getter
	private Config config;

	@Getter
	private BasicScriptCommandManager commandManager;
	@Getter
	private final TIntSet tweakedBiomes = new TIntHashSet();
	private final Set<String> enabledTweaks = new HashSet<>();

	@EventHandler
	public void onFingerprintViolation(final FMLFingerprintViolationEvent e){
		this.signed = false;
	}

	@EventHandler
	public void onPreInit(final FMLPreInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker PreInitialization", 5, true);

		LogHelper.setLogger(e.getModLog());

		bar.step("Reading config");
		if(!this.signed){
			LogHelper.warn("Hey... uhm... this is awkward but, it looks like you're using an unofficial version of BiomeTweaker. Where exactly did you get this from?");
			LogHelper.warn("Unless I (superckl) sent you this version, don't expect to get any support for it.");
		}

		this.config = new Config(new File(Loader.instance().getConfigDir(), ModData.MOD_NAME+"/"));
		this.config.loadValues();

		final List<IMCMessage> messages = FMLInterModComms.fetchRuntimeMessages(ModData.MOD_ID);
		for(final IMCMessage message:messages)
			if(message.key.equals("enableTweak") && message.isStringMessage()){
				LogHelper.debug("Received enableTweak IMC message from "+message.getSender()+", enabling tweak: "+message.getStringValue());
				this.enabledTweaks.add(message.getStringValue());
			}

		bar.step("Initializing scripting enviroment");
		final File scripts = new File(this.config.getBtConfigFolder(), "scripts/");
		scripts.mkdirs();

		this.commandManager = new BasicScriptCommandManager();
		ScriptCommandRegistry.INSTANCE.registerScriptCommandManager(ModData.MOD_ID, this.commandManager);

		BiomeTweaker.proxy.initProperties();
		BiomeTweaker.proxy.setupScripts(e.getAsmData());

		bar.step("Pre-Initializing Integration");
		IntegrationManager.INSTANCE.preInit();

		bar.step("Registering handlers");
		BiomeTweaker.proxy.registerHandlers();

		bar.step("Applying scripts");
		this.commandManager.applyCommandsFor(ApplicationStage.PRE_INIT);

		ProgressManager.pop(bar);
	}

	public void parseScripts(){
		try {
			LogHelper.info("Beginning script parsing...");
			long diff = 0;
			final long time = System.currentTimeMillis();
			for (final String item : this.config.getIncludes()) {
				File subFile = null;
				try {
					subFile = new File(this.config.getBtConfigFolder(), item);
					this.parseScript(subFile);
				} catch (final Exception e1) {
					LogHelper.error("Failed to parse a script file! File: " + subFile);
					e1.printStackTrace();
				}
			}
			final File scripts = new File(this.config.getBtConfigFolder(), "scripts/");
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
		this.commandManager.addCommand(new ScriptCommandSetPlacementStage("BIOME_BLOCKS"));
		this.commandManager.addCommand(new ScriptCommandSetWorld(null));
		this.commandManager.setCurrentStage(BasicScriptCommandManager.getDefaultStage());
	}

	@EventHandler
	public void onInit(final FMLInitializationEvent e) throws InterruptedException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker Initialization", 3, true);

		bar.step("Parsing scripts");
		this.parseScripts();

		bar.step("Initializing Integration");
		IntegrationManager.INSTANCE.init();

		bar.step("Applying scripts");
		this.commandManager.applyCommandsFor(ApplicationStage.INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onPostInit(final FMLPostInitializationEvent e){
		final ProgressBar bar = ProgressManager.push("BiomeTweaker PostInitialization", 2, true);

		bar.step("Post-Initializing Integration");
		IntegrationManager.INSTANCE.postInit();

		bar.step("Applying scripts");
		this.commandManager.applyCommandsFor(ApplicationStage.POST_INIT);

		ProgressManager.pop(bar);
	}

	@EventHandler
	public void onLoadComplete(final FMLLoadCompleteEvent e) throws IOException{
		final ProgressBar bar = ProgressManager.push("BiomeTweaker LoadComplete", 2, true);

		bar.step("Applying scripts");
		this.commandManager.applyCommandsFor(ApplicationStage.FINISHED_LOAD);

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
		final File baseDir = new File(this.config.getBtConfigFolder(), "output/");
		final File biomeDir = new File(baseDir, "/biome/");
		biomeDir.mkdirs();

		for(final File file:biomeDir.listFiles())
			if(file.getName().endsWith(".json"))
				file.delete();

		if(this.config.isOutputSeperateFiles())
			for(final JsonElement element:array){
				final JsonObject obj = (JsonObject) element;
				final StringBuilder fileName = new StringBuilder(obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_"))
						.append(" (").append(obj.get("Resource Location").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(")").append(".json");
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

		if(this.config.isOutputSeperateFiles())
			for(final JsonElement ele:entityArray){
				final JsonObject obj = (JsonObject) ele;
				final StringBuilder fileName = new StringBuilder(obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_"))
						.append(" (").append(obj.get("Registry ID").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(")").append(".json");
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

		if(this.config.isOutputSeperateFiles())
			for(final JsonElement ele:dimArray){
				final JsonObject obj = (JsonObject) ele;
				final StringBuilder fileName = new StringBuilder(obj.get("Name").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_"))
						.append(" (").append(obj.get("Suffix").getAsString().replaceAll("[^a-zA-Z0-9.-]", "_")).append(")").append(".json");
				final File dimOutput = new File(dimDir, fileName.toString());
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
		this.commandManager.applyCommandsFor(ApplicationStage.SERVER_STARTING);
	}

	@EventHandler
	public void onServerStarted(final FMLServerStartedEvent e){
		this.commandManager.applyCommandsFor(ApplicationStage.SERVER_STARTED);
	}

	public boolean isTweakEnabled(final String tweak){
		return this.enabledTweaks.contains(tweak);
	}

	public void addCommand(final ScriptCommand command){
		this.commandManager.addCommand(command);
	}

	public void onTweak(final int biomeID){
		this.tweakedBiomes.add(biomeID);
	}

	public Set<String> getEnabledTweaks(){
		return ImmutableSet.copyOf(this.enabledTweaks);
	}

}
