package me.superckl.biometweaker.common.handler;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class EntityEventHandler {

	@Getter
	@Setter
	private static int globalPackSize = -1;
	@Getter
	private static TIntObjectMap<TObjectIntMap<String>> packSizes = new TIntObjectHashMap<TObjectIntMap<String>>();
	@Getter
	private static TIntObjectMap<List<Block>> noBonemeals = new TIntObjectHashMap<List<Block>>();

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onGetMaxPackSize(final LivingPackSizeEvent e){
		if(EntityEventHandler.globalPackSize > -1){
			e.setResult(Result.ALLOW);
			e.maxPackSize = EntityEventHandler.globalPackSize;
			return;
		}
		if(EntityEventHandler.packSizes.isEmpty())
			return;
		final BiomeGenBase biome = e.entityLiving.worldObj.getBiomeGenForCoords((int) e.entityLiving.posX, (int) e.entityLiving.posZ);
		final String clazz = e.entityLiving.getClass().getName();
		if(EntityEventHandler.packSizes.containsKey(biome.biomeID)){
			final TObjectIntMap<String> sizes = EntityEventHandler.packSizes.get(biome.biomeID);
			if(sizes.containsKey(clazz)){
				final int size = sizes.get(clazz);
				if(size > -1){
					e.setResult(Result.ALLOW);
					e.maxPackSize = size;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBonemealUse(final BonemealEvent e){
		if(EntityEventHandler.noBonemeals.isEmpty())
			return;
		final BiomeGenBase biome = e.world.getBiomeGenForCoords(e.x, e.z);
		if(EntityEventHandler.noBonemeals.containsKey(biome.biomeID)){
			final List<Block> list = EntityEventHandler.noBonemeals.get(biome.biomeID);
			if(list == null){
				e.setCanceled(true);
				return;
			}
			for(final Block block:list)
				if(block == e.block){
					e.setCanceled(true);
					break;
				}
		}
	}

}
