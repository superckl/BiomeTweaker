package me.superckl.biometweaker;

import com.mojang.blaze3d.shaders.FogShape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientBiomeEvents {

	@SubscribeEvent(receiveCanceled = true)
	public void onRenderFog(final EntityViewRenderEvent.RenderFogEvent e) {
		final BlockPos pos = e.getCamera().getBlockPosition();
		final Holder<Biome> biome = e.getCamera().getEntity().level.getBiome(pos);
		final ResourceLocation rLoc = biome.unwrap().map(ResourceKey::location, Biome::getRegistryName);

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

}
