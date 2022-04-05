package me.superckl.biometweaker;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeEvents {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void modifyBiome(final BiomeLoadingEvent e) {
		final BiomeModificationManager mod = BiomeModificationManager.forBiome(e.getName());
		if(mod.hasClimate())
			e.setClimate(mod.getClimate().modify(e.getClimate()));
		if(mod.hasEffects())
			e.setEffects(mod.getEffects().modify(e.getEffects()));
		if(mod.hasGeneration())
			mod.getGeneration().modify(e.getGeneration());
		if(mod.hasSpawn())
			mod.getSpawn().modify(e.getSpawns());
		if(mod.hasCategory())
			e.setCategory(mod.getCategory());
	}

	@SubscribeEvent
	public void onSleep(final PlayerSleepInBedEvent e) {
		if(e.getPlayer().level.isClientSide || e.getResultStatus() != null)
			return;
		e.getOptionalPos().ifPresent(pos -> {
			final BiomeModificationManager mod = BiomeModificationManager.forBiome(e.getPlayer().level.getBiome(pos).value().getRegistryName());
			if(mod.isDisableSleep()) {
				e.setResult(BedSleepingProblem.NOT_POSSIBLE_HERE);
				((ServerPlayer) e.getPlayer()).displayClientMessage(new TranslatableComponent("biometweaker.sleep.biome"), true);
			}
		});
	}

}
