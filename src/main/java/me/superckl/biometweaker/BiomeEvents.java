package me.superckl.biometweaker;

import java.util.Optional;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeEvents {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void modifyBiome(final BiomeLoadingEvent e) {
		final Optional<BiomeModificationManager> modOpt = BiomeModificationManager.forBiomeOpt(e.getName());
		modOpt.ifPresent(mod -> {
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
		});
	}

	@SubscribeEvent
	public void onSleep(final PlayerSleepInBedEvent e) {
		if(e.getPlayer().level.isClientSide || e.getResultStatus() != null)
			return;
		e.getOptionalPos().ifPresent(pos -> {
			final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getPlayer().level.getBiome(pos).value().getRegistryName());
			mod.filter(BiomeModificationManager::isDisableSleep).ifPresent(x -> {
				e.setResult(BedSleepingProblem.NOT_POSSIBLE_HERE);
				((ServerPlayer) e.getPlayer()).displayClientMessage(new TranslatableComponent("biometweaker.sleep.biome"), true);
			});
		});
	}

	@SubscribeEvent
	public void onBonemeal(final BonemealEvent e) {
		if(e.getPlayer().level.isClientSide)
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getPlayer().level.getBiome(e.getPos()).value().getRegistryName());
		mod.filter(BiomeModificationManager::isDisableBonemeal).ifPresent(x -> {
			if(Config.getInstance().getConsumeBonemeal().get())
				e.setResult(Result.ALLOW);
			else
				e.setCanceled(true);
		});
	}

	@SubscribeEvent
	public void onCropGrow(final CropGrowEvent.Pre e) {
		if(e.getWorld().isClientSide())
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getWorld().getBiome(e.getPos()).value().getRegistryName());
		mod.filter(BiomeModificationManager::isDisableCropGrowth).ifPresent(x -> e.setResult(Result.DENY));
	}

	@SubscribeEvent
	public void onSaplingGrow(final SaplingGrowTreeEvent e) {
		if(e.getWorld().isClientSide())
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getWorld().getBiome(e.getPos()).value().getRegistryName());
		mod.filter(BiomeModificationManager::isDisableSaplingGrowth).ifPresent(x -> e.setResult(Result.DENY));
	}

	@SubscribeEvent
	public void onLivingTick(final LivingUpdateEvent e) {
		//Avoid the biome lookup for every entity every tick if there are no effects to add
		if(!BiomeModificationManager.hasMobEffects)
			return;
		final LivingEntity entity = e.getEntityLiving();
		if(entity.level.isClientSide || !entity.isAffectedByPotions())
			return;
		final Optional<BiomeModificationManager> modOpt = BiomeModificationManager.forBiomeOpt(entity.level.getBiome(entity.getOnPos()).value().getRegistryName());
		modOpt.ifPresent(mod -> {
			mod.getMobEffects(entity.getType()).forEach(effect -> {
				if(entity.tickCount % effect.interval() != 0 || entity.getRandom().nextFloat() > effect.chance())
					return;
				if(effect.effect().isInstantenous())
					effect.effect().applyInstantenousEffect(null, null, entity, effect.amplifier(), 1D);
				else
					entity.addEffect(effect.createInstance());
			});
		});
	}

}
