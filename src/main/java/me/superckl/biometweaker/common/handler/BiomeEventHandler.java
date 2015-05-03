package me.superckl.biometweaker.common.handler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BiomeEventHandler {

	@Getter
	private static final Map<Integer, List<String>> decorateTypes = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> waterlilyPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> treesPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> flowersPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> grassPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> deadBushPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> mushroomPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> reedsPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> cactiPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> sandPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> clayPerChunk = Maps.newHashMap();
	@Getter
	private static final Map<Integer, Integer> bigMushroomsPerChunk = Maps.newHashMap();

	private Field actualFillerBlock;
	private Field liquidFillerBlock;
	private static Field actualFillerBlockMeta;
	private static Field liquidFillerBlockMeta;
	private Field grassColor;
	private Field foliageColor;
	private Field waterColor;
	private final int[] colorCache = new int[768];

	public BiomeEventHandler() {
		Arrays.fill(this.colorCache, -2);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onReplaceBlocks(final ReplaceBiomeBlocks e){
		try {
			if(this.actualFillerBlock == null)
				this.actualFillerBlock = BiomeGenBase.class.getDeclaredField("actualFillerBlock");
			if(this.liquidFillerBlock == null)
				this.liquidFillerBlock = BiomeGenBase.class.getDeclaredField("liquidFillerBlock");
			if(BiomeEventHandler.actualFillerBlockMeta == null)
				BiomeEventHandler.actualFillerBlockMeta = BiomeGenBase.class.getDeclaredField("actualFillerBlockMeta");
			if(BiomeEventHandler.liquidFillerBlockMeta == null)
				BiomeEventHandler.liquidFillerBlockMeta = BiomeGenBase.class.getDeclaredField("liquidFillerBlockMeta");
			for (int k = 0; k < 16; ++k)
				for (int l = 0; l < 16; ++l)
				{
					//TODO actual and liquid default to null. Can specify what block to replace with each.
					final BiomeGenBase biomegenbase = e.biomeArray[l + (k * 16)];
					final Block actualS = (Block) this.actualFillerBlock.get(biomegenbase);
					final Block actualL = (Block) this.liquidFillerBlock.get(biomegenbase);
					final int sM = BiomeEventHandler.actualFillerBlockMeta.getInt(biomegenbase) & 255;
					final int lM = BiomeEventHandler.liquidFillerBlockMeta.getInt(biomegenbase) & 255;
					if((actualS == Blocks.stone) && (actualL == Blocks.water))
						continue;
					final int i1 = k;
					final int j1 = l;
					final int k1 = e.blockArray.length / 256;
					for (int l1 = k1-1; l1 >= 0; --l1)
					{
						final int i2 = (((j1 * 16) + i1) * k1) + l1;
						final Block block2 = e.blockArray[i2];
						if(block2 == Blocks.stone){
							e.blockArray[i2] = actualS;
							if(e.metaArray != null)
								e.metaArray[i2] = (byte) sM;
						}else if(block2 == Blocks.water){
							e.blockArray[i2] = actualL;
							if(e.metaArray != null)
								e.metaArray[i2] = (byte) lM;
						}
					}
				}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process replace biome blocks event.");
			e1.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetGrassColor(final GetGrassColor e){
		try {
			if(this.grassColor == null)
				this.grassColor = BiomeGenBase.class.getDeclaredField("grassColor");
			int newColor = this.colorCache[e.biome.biomeID];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[e.biome.biomeID]) != -2)
				e.newColor = newColor;
			else{
				newColor = this.grassColor.getInt(e.biome);
				this.colorCache[e.biome.biomeID] = newColor;
				if(newColor == -1)
					return;
				e.newColor = newColor;
			}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process getGrassColor event!");
			e1.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetFoliageColor(final GetFoliageColor e){
		try {
			if(this.foliageColor == null)
				this.foliageColor = BiomeGenBase.class.getDeclaredField("foliageColor");
			int newColor = this.colorCache[e.biome.biomeID+256];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[e.biome.biomeID+256]) != -2)
				e.newColor = newColor;
			else{
				newColor = this.foliageColor.getInt(e.biome);
				this.colorCache[e.biome.biomeID+256] = newColor;
				if(newColor == -1)
					return;
				e.newColor = newColor;
			}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process getFoliageColor event!");
			e1.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetWaterColor(final GetWaterColor e){
		try {
			if(this.waterColor == null)
				this.waterColor = BiomeGenBase.class.getDeclaredField("waterColor");
			int newColor = this.colorCache[e.biome.biomeID+512];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[e.biome.biomeID+512]) != -2)
				e.newColor = newColor;
			else{
				newColor = this.waterColor.getInt(e.biome);
				this.colorCache[e.biome.biomeID+512] = newColor;
				if(newColor == -1)
					return;
				e.newColor = newColor;
			}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process getWaterColor event!");
			e1.printStackTrace();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBiomeDecorate(final DecorateBiomeEvent.Decorate e){
		final BiomeGenBase gen = e.world.getBiomeGenForCoords(e.chunkX, e.chunkZ);
		final boolean isAll = BiomeEventHandler.decorateTypes.containsKey(-1);
		if((isAll || BiomeEventHandler.decorateTypes.containsKey(gen.biomeID)) && (BiomeEventHandler.decorateTypes.get(isAll ? -1:gen.biomeID).contains(e.type.name()) || BiomeEventHandler.decorateTypes.get(isAll ? -1:gen.biomeID).contains("all")))
			e.setResult(Result.DENY);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onCreateBiomeDecorator(final BiomeEvent.CreateDecorator e){
		final int id = e.biome.biomeID;
		if(BiomeEventHandler.waterlilyPerChunk.containsKey(id))
			e.newBiomeDecorator.waterlilyPerChunk = BiomeEventHandler.waterlilyPerChunk.get(id);
		if(BiomeEventHandler.treesPerChunk.containsKey(id))
			e.newBiomeDecorator.treesPerChunk = BiomeEventHandler.treesPerChunk.get(id);
		if(BiomeEventHandler.flowersPerChunk.containsKey(id))
			e.newBiomeDecorator.flowersPerChunk = BiomeEventHandler.flowersPerChunk.get(id);
		if(BiomeEventHandler.grassPerChunk.containsKey(id))
			e.newBiomeDecorator.grassPerChunk = BiomeEventHandler.grassPerChunk.get(id);
		if(BiomeEventHandler.deadBushPerChunk.containsKey(id))
			e.newBiomeDecorator.deadBushPerChunk = BiomeEventHandler.deadBushPerChunk.get(id);
		if(BiomeEventHandler.mushroomPerChunk.containsKey(id))
			e.newBiomeDecorator.mushroomsPerChunk = BiomeEventHandler.mushroomPerChunk.get(id);
		if(BiomeEventHandler.reedsPerChunk.containsKey(id))
			e.newBiomeDecorator.reedsPerChunk = BiomeEventHandler.reedsPerChunk.get(id);
		if(BiomeEventHandler.cactiPerChunk.containsKey(id))
			e.newBiomeDecorator.cactiPerChunk = BiomeEventHandler.cactiPerChunk.get(id);
		if(BiomeEventHandler.sandPerChunk.containsKey(id))
			e.newBiomeDecorator.sandPerChunk = BiomeEventHandler.sandPerChunk.get(id);
		if(BiomeEventHandler.clayPerChunk.containsKey(id))
			e.newBiomeDecorator.clayPerChunk = BiomeEventHandler.clayPerChunk.get(id);
		if(BiomeEventHandler.bigMushroomsPerChunk.containsKey(id))
			e.newBiomeDecorator.bigMushroomsPerChunk = BiomeEventHandler.bigMushroomsPerChunk.get(id);
	}

}
