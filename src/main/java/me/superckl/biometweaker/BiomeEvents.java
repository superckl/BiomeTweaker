package me.superckl.biometweaker;

import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.level.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeEvents {

	@SubscribeEvent
	public void onSleep(final PlayerSleepInBedEvent e) {
		if(e.getEntity().level.isClientSide || e.getResultStatus() != null)
			return;
		e.getOptionalPos().ifPresent(pos -> {
			final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getEntity().level.getBiome(pos).value());
			mod.filter(BiomeModificationManager::isDisableSleep).ifPresent(x -> {
				e.setResult(BedSleepingProblem.NOT_POSSIBLE_HERE);
				((ServerPlayer) e.getEntity()).displayClientMessage(Component.translatable("biometweaker.sleep.biome"), true);
			});
		});
	}

	@SubscribeEvent
	public void onBonemeal(final BonemealEvent e) {
		if(e.getEntity().level.isClientSide)
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getEntity().level.getBiome(e.getPos()).value());
		mod.filter(BiomeModificationManager::isDisableBonemeal).ifPresent(x -> {
			if(Config.getInstance().getConsumeBonemeal().get())
				e.setResult(Result.ALLOW);
			else
				e.setCanceled(true);
		});
	}

	@SubscribeEvent
	public void onCropGrow(final CropGrowEvent.Pre e) {
		if(e.getLevel().isClientSide())
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getLevel().getBiome(e.getPos()).value());
		mod.filter(BiomeModificationManager::isDisableCropGrowth).ifPresent(x -> e.setResult(Result.DENY));
	}

	@SubscribeEvent
	public void onSaplingGrow(final SaplingGrowTreeEvent e) {
		if(e.getLevel().isClientSide())
			return;
		final Optional<BiomeModificationManager> mod = BiomeModificationManager.forBiomeOpt(e.getLevel().getBiome(e.getPos()).value());
		mod.filter(BiomeModificationManager::isDisableSaplingGrowth).ifPresent(x -> e.setResult(Result.DENY));
	}

	@SubscribeEvent
	public void onLivingTick(final LivingTickEvent e) {
		//Avoid the biome lookup for every entity every tick if there are no effects to add
		if(!BiomeModificationManager.hasMobEffects)
			return;
		final LivingEntity entity = e.getEntity();
		if(entity.level.isClientSide || !entity.isAffectedByPotions())
			return;
		final Optional<BiomeModificationManager> modOpt = BiomeModificationManager.forBiomeOpt(entity.level.getBiome(entity.getOnPos()).value());
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
