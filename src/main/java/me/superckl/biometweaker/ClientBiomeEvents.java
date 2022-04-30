package me.superckl.biometweaker;

import com.mojang.blaze3d.shaders.FogShape;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientBiomeEvents {

	@SubscribeEvent(receiveCanceled = true)
	public void onRenderFog(final EntityViewRenderEvent.RenderFogEvent e) {
		final Holder<Biome> biome = e.getCamera().getEntity().level.getBiome(e.getCamera().getBlockPosition());
		final ResourceLocation rLoc = biome.unwrap().map(ResourceKey::location, Biome::getRegistryName);

		BiomeModificationManager.forBiomeOpt(rLoc).filter(BiomeModificationManager::hasFog).ifPresent(manager -> {
			manager.getFogStartModifier().ifPresent(mod -> e.setNearPlaneDistance(e.getNearPlaneDistance()*mod));
			manager.getFogEndModifier().ifPresent(mod -> e.setFarPlaneDistance(e.getFarPlaneDistance()*mod));
			if(manager.hasFogShape())
				e.setFogShape(this.toClientShape(manager.getFogShape()));
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
