package me.superckl.biometweaker.common.handler;

import me.superckl.api.superscript.ApplicationStage;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryEventHandler {

	public static IForgeRegistry<Biome> registry = null;

	@SubscribeEvent
	public void onBiomeRegistry(final RegistryEvent.Register<Biome> e) {
		RegistryEventHandler.registry = e.getRegistry();
		BiomeTweaker.getInstance().getCommandManager().applyCommandsFor(ApplicationStage.BIOME_REGISTRY);
		RegistryEventHandler.registry = null;
	}

}
