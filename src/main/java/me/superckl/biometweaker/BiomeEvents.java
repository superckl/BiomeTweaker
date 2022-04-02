package me.superckl.biometweaker;

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

}
