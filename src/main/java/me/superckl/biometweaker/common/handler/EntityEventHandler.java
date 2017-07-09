package me.superckl.biometweaker.common.handler;

import java.util.List;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.Setter;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEventHandler {

	@Getter
	@Setter
	private static int globalPackSize = -1;
	@Getter
	private static TIntObjectMap<TObjectIntMap<String>> packSizes = new TIntObjectHashMap<>();
	@Getter
	private static TIntObjectMap<List<IBlockState>> noBonemeals = new TIntObjectHashMap<>();

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onGetMaxPackSize(final LivingPackSizeEvent e){
		if(EntityEventHandler.globalPackSize > -1){
			e.setResult(Result.ALLOW);
			e.setMaxPackSize(EntityEventHandler.globalPackSize);
			return;
		}
		if(EntityEventHandler.packSizes.isEmpty())
			return;
		final Biome biome = e.getEntityLiving().world.getBiome(e.getEntityLiving().getPosition());
		final String clazz = e.getEntityLiving().getClass().getName();
		if(EntityEventHandler.packSizes.containsKey(Biome.getIdForBiome(biome))){
			final TObjectIntMap<String> sizes = EntityEventHandler.packSizes.get(Biome.getIdForBiome(biome));
			if(sizes.containsKey(clazz)){
				final int size = sizes.get(clazz);
				if(size > -1){
					e.setResult(Result.ALLOW);
					e.setMaxPackSize(size);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBonemealUse(final BonemealEvent e){
		if(EntityEventHandler.noBonemeals.isEmpty())
			return;
		final Biome biome = e.getWorld().getBiome(e.getPos());
		if(EntityEventHandler.noBonemeals.containsKey(Biome.getIdForBiome(biome))){
			final List<IBlockState> list = EntityEventHandler.noBonemeals.get(Biome.getIdForBiome(biome));
			if(list == null){
				e.setCanceled(true);
				return;
			}
			for(final IBlockState block:list)
				if(new BlockEquivalencePredicate(block).apply(e.getBlock())){
					e.setCanceled(true);
					break;
				}
		}
	}

}
