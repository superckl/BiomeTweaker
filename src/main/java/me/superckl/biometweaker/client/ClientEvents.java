package me.superckl.biometweaker.client;

import com.mojang.blaze3d.shaders.FogShape;

import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.biometweaker.BiomeModificationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

	@SubscribeEvent(receiveCanceled = true)
	public void onRenderFog(final ViewportEvent.RenderFog e) {
		final BlockPos pos = e.getCamera().getBlockPosition();
		final Holder<Biome> biome = e.getCamera().getEntity().level.getBiome(pos);
		final ResourceLocation rLoc = biome.unwrapKey().get().location();

		BiomeModificationManager.forBiomeOpt(rLoc).filter(BiomeModificationManager::hasFog).map(BiomeModificationManager::getFog).ifPresent(fog -> {
			e.setNearPlaneDistance(e.getNearPlaneDistance()*fog.getNearModifier(pos.getY()));
			e.setFarPlaneDistance(e.getFarPlaneDistance()*fog.getFarModifier(pos.getY()));
			if(fog.hasShape())
				e.setFogShape(this.toClientShape(fog.getShape()));
			e.setCanceled(true);
		});
	}

	private FogShape toClientShape(final BiomeModificationManager.FogShape shape) {
		switch(shape) {
		case CYLINDER:
			return FogShape.CYLINDER;
		case SPHERE:
			return FogShape.SPHERE;
		default:
			throw new IllegalArgumentException("Unknown fog shape "+shape);
		}
	}

	@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = BiomeTweakerAPI.MOD_ID)
	public static class ModEvents{

		@SubscribeEvent
		public static void clientSetup(final FMLClientSetupEvent e) {
			e.enqueueWork(() -> MinecraftForge.EVENT_BUS.register(new ClientEvents()));
			//ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, () -> new ConfigGuiFactory((mc, screen) -> new ConfigGui(screen)));

		}

	}



}
