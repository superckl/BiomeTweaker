package me.superckl.biometweaker.common.handler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import me.superckl.biometweaker.common.world.gen.layer.GenLayerReplacement;
import me.superckl.biometweaker.util.LogHelper;
import me.superckl.biometweaker.util.NumberHelper;
import net.minecraft.block.Block;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;

public class BiomeEventHandler {

	public static byte globalSize = -1;
	public static final Map<WorldType, Byte> sizes = Maps.newIdentityHashMap();

	@Getter
	private static final TIntObjectMap<List<Pair<Pair<Block, Integer>, List<WeightedBlockEntry>>>> blockReplacements = new TIntObjectHashMap<List<Pair<Pair<Block, Integer>, List<WeightedBlockEntry>>>>();
	@Getter
	private static final boolean[] contigReplaces = new boolean[256];
	@Getter
	private static final TIntIntMap biomeReplacements = new TIntIntHashMap();
	@Getter
	private static final TIntObjectMap<List<String>> decorateTypes = new TIntObjectHashMap<List<String>>();
	@Getter
	private static final TIntObjectMap<List<String>> populateTypes = new TIntObjectHashMap<List<String>>();
	@Getter
	private static final TIntIntMap waterlilyPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap treesPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap flowersPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap grassPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap deadBushPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap mushroomPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap reedsPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap cactiPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap sandPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap clayPerChunk = new TIntIntHashMap();
	@Getter
	private static final TIntIntMap bigMushroomsPerChunk = new TIntIntHashMap();

	private Field grassColor;
	private Field foliageColor;
	private Field waterColor;
	private final int[] colorCache = new int[768];
	private final Random random = new Random();

	private final Map<World, Map<ChunkCoordIntPair, Map<Integer, Map<Block, Map<Integer, WeightedBlockEntry>>>>> replacedBiomes = Maps.newHashMap();

	public BiomeEventHandler() {
		Arrays.fill(this.colorCache, -2);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onReplaceBlocks(final ReplaceBiomeBlocks e){
		try {
			if(!this.replacedBiomes.containsKey(e.world))
				this.replacedBiomes.put(e.world, Maps.<ChunkCoordIntPair, Map<Integer, Map<Block, Map<Integer, WeightedBlockEntry>>>>newHashMap());
			final Map<Integer, Map<Block, Map<Integer, WeightedBlockEntry>>> shouldDoBMap = this.findMap(e.world, new ChunkCoordIntPair(e.chunkX, e.chunkZ));
			for (int k = 0; k < 16; ++k)
				for (int l = 0; l < 16; ++l)
				{
					BiomeGenBase biomegenbase = e.biomeArray[l + (k * 16)];
					/*if(BiomeEventHandler.biomeReplacements.containsKey(biomegenbase.biomeID)){
						final int id = BiomeEventHandler.biomeReplacements.get(biomegenbase.biomeID);
						biomegenbase = BiomeGenBase.getBiome(id);
						e.biomeArray[l + (k * 16)] = biomegenbase;
					}*/
					if(!BiomeEventHandler.blockReplacements.containsKey(biomegenbase.biomeID))
						continue;
					if(!shouldDoBMap.containsKey(biomegenbase.biomeID))
						shouldDoBMap.put(biomegenbase.biomeID, Maps.<Block, Map<Integer, WeightedBlockEntry>>newIdentityHashMap());
					final Map<Block, Map<Integer, WeightedBlockEntry>> shouldDoMap = shouldDoBMap.get(biomegenbase.biomeID);
					final List<Pair<Pair<Block, Integer>, List<WeightedBlockEntry>>> list = BiomeEventHandler.blockReplacements.get(biomegenbase.biomeID);
					final int i1 = k;
					final int j1 = l;
					final int k1 = e.blockArray.length / 256;
					for (int l1 = k1-1; l1 >= 0; --l1)
					{
						final int i2 = (((j1 * 16) + i1) * k1) + l1;
						final Block block2 = e.blockArray[i2];
						WeightedBlockEntry toUse = null;
						if(shouldDoMap.containsKey(block2)){
							final Map<Integer, WeightedBlockEntry> map = shouldDoMap.get(block2);
							if(map.containsKey(e.metaArray == null ? 0:e.metaArray[i2]))
								toUse = map.get(e.metaArray == null ? 0:e.metaArray[i2]);
							else if(map.containsKey(-1))
								toUse = map.get(-1);
						}
						Integer meta;
						if(toUse == null)
							for(final Pair<Pair<Block, Integer>, List<WeightedBlockEntry>> pair:list)
								if(pair.getKey().getKey() == block2){
									meta = pair.getKey().getValue();
									final boolean shouldDo = (meta == null) || ((e.metaArray == null) && (meta == 0)) || (e.metaArray[i2] == meta);
									if(shouldDo){
										toUse = (WeightedBlockEntry) WeightedRandom.getRandomItem(this.random, pair.getValue());
										if(!shouldDoMap.containsKey(block2))
											shouldDoMap.put(block2, new HashMap<Integer, WeightedBlockEntry>());
										final Map<Integer, WeightedBlockEntry> map = shouldDoMap.get(block2);
										map.put(meta == null ? -1:meta, toUse);
									}
								}
						if(toUse != null){
							e.blockArray[i2] = toUse.block.getKey();
							if(e.metaArray != null)
								e.metaArray[i2] = (byte) ((meta = toUse.block.getValue()) == null ? 0:meta);
						}
					}
				}
			final Iterator<Integer> it = shouldDoBMap.keySet().iterator();
			while(it.hasNext())
				if(!BiomeEventHandler.contigReplaces[it.next()])
					it.remove();
			this.replacedBiomes.get(e.world).put(new ChunkCoordIntPair(e.chunkX, e.chunkZ), shouldDoBMap);
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
	public void onBiomePopulate(final PopulateChunkEvent.Populate e){
		final BiomeGenBase gen = e.world.getBiomeGenForCoords(e.chunkX, e.chunkZ);
		final boolean isAll = BiomeEventHandler.populateTypes.containsKey(-1);
		if((isAll || BiomeEventHandler.populateTypes.containsKey(gen.biomeID)) && (BiomeEventHandler.populateTypes.get(isAll ? -1:gen.biomeID).contains(e.type.name()) || BiomeEventHandler.populateTypes.get(isAll ? -1:gen.biomeID).contains("all")))
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

	@SubscribeEvent
	public void onGetBiomeSize(final WorldTypeEvent.BiomeSize e){
		if(BiomeEventHandler.globalSize != -1)
			e.newSize = BiomeEventHandler.globalSize;
		else if(BiomeEventHandler.sizes.containsKey(e.worldType))
			e.newSize = BiomeEventHandler.sizes.get(e.worldType);
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onInitBiomeGens(WorldTypeEvent.InitBiomeGens e){
		e.newBiomeGens[0] = new GenLayerReplacement(e.newBiomeGens[0]);
		e.newBiomeGens[1] = new GenLayerReplacement(e.newBiomeGens[1]);
	}

	private Map<Integer, Map<Block, Map<Integer, WeightedBlockEntry>>> findMap(final World world, final ChunkCoordIntPair pair){
		final Map<ChunkCoordIntPair, Map<Integer, Map<Block, Map<Integer, WeightedBlockEntry>>>> map = this.replacedBiomes.get(world);

		final ChunkCoordIntPair[] pairs = NumberHelper.fillGrid(4, pair);

		for(final ChunkCoordIntPair search:pairs)
			if(map.containsKey(search))
				return map.get(search);

		return Maps.newHashMap();
	}

	@Getter
	public static class WeightedBlockEntry extends WeightedRandom.Item{

		private final Pair<Block, Integer> block;

		public WeightedBlockEntry(final int weight, final Pair<Block, Integer> block) {
			super(weight);
			this.block = block;
		}

	}

}
