package me.superckl.biometweaker;


import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.command.BasicScriptCommandManager;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetPlacementStage;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetWorld;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.server.command.CommandOutput;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(BiomeTweakerAPI.MOD_ID)
public class BiomeTweaker {

	@Getter
	private static BiomeTweaker INSTANCE;
	public static final Logger LOG = LogManager.getFormatterLogger(BiomeTweakerAPI.MOD_ID);
	@Getter
	private final BasicScriptCommandManager commandManager;
	@Getter
	private final File scriptDir;
	@Getter
	private final File outputDir;

	public BiomeTweaker() {
		BiomeTweaker.INSTANCE = this;
		ScriptSetup.setupScripts(ModList.get().getAllScanData());
		try {
			ScriptSetup.initProperties();
		} catch (final ClassNotFoundException e) {
			BiomeTweaker.LOG.warn("Failed to setup properties, set commands may not work!");
			e.printStackTrace();
		}

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
		e.enqueueWork(() -> {
			this.commandManager.applyCommandsFor(ApplicationStage.SETUP);
			MinecraftForge.EVENT_BUS.register(new BiomeEvents());
			MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
		});
	}

	private void loadComplete(final FMLLoadCompleteEvent e) {
		e.enqueueWork(() -> {
			this.commandManager.applyCommandsFor(ApplicationStage.LOAD_COMPLETE);
		});
	}

	private void registerCommands(final RegisterCommandsEvent e) {
		e.getDispatcher().register(Commands.literal("biometweaker").requires(cs -> cs.hasPermission(2)).then(Commands.literal("output").executes(CommandOutput::output)));
	}

	public void parseScripts(){
		try {
			BiomeTweaker.LOG.info("Beginning script parsing...");
			long diff;
			final long time = System.currentTimeMillis();
			this.scriptDir.mkdirs();
			for (final File script : this.scriptDir.listFiles((dir, name) -> name.endsWith(".cfg")))
				try {
					this.parseScript(script);
				} catch (final Exception e1) {
					BiomeTweaker.LOG.error("Failed to parse a script file! File: " + script);
					e1.printStackTrace();
				}
			diff = System.currentTimeMillis() - time;
			BiomeTweaker.LOG.info("Finished script parsing.");
			BiomeTweaker.LOG.debug("Script parsing took "+diff+"ms.");
		} catch (final Exception e) {
			throw new RuntimeException("An unexpected error occurred while processing script files. Parsing may be incomplete. Ensure BiomeTweakerCore was called successfully.", e);
		}
	}

	public void parseScript(final File file) throws IOException{
		if(!file.exists()){
			BiomeTweaker.LOG.debug("Subfile %s not found. A blank one will be generated.", file.getName());
			file.createNewFile();
		}
		ScriptParser.parseScriptFile(file);
		//Reset other various stages
		this.commandManager.addCommand(new ScriptCommandSetPlacementStage(BlockReplacementManager.getDefaultStage().name()));
		this.commandManager.addCommand(new ScriptCommandSetWorld(null));
		this.commandManager.setCurrentStage(BasicScriptCommandManager.getDefaultStage());
	}



}
