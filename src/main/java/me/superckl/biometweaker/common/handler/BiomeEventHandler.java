package me.superckl.biometweaker.common.handler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.custom_hash.TObjectByteCustomHashMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import lombok.Getter;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager.ReplacementStage;
import me.superckl.biometweaker.common.world.gen.BlockReplacer;
import me.superckl.biometweaker.common.world.gen.layer.GenLayerReplacement;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent.ReplaceBiomeBlocks;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BiomeEventHandler {

	public static byte globalSize = -1;
	public static final TObjectByteCustomHashMap<Object> sizes = new TObjectByteCustomHashMap<>(IdentityHashingStrategy.INSTANCE);

	@Getter
	private static final TIntObjectMap<List<Pair<Pair<Block, Integer>, Pair<Block, Integer>>>> villageBlockReplacements = new TIntObjectHashMap<>();
	@Getter
	private static final TIntIntMap biomeReplacements = new TIntIntHashMap();
	@Getter
	private static final TIntObjectMap<List<String>> decorateTypes = new TIntObjectHashMap<>();
	@Getter
	private static final TIntObjectMap<List<String>> populateTypes = new TIntObjectHashMap<>();
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
	private final int[] colorCache = new int[512];



	public BiomeEventHandler() {
		Arrays.fill(this.colorCache, -2);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onReplaceBlocks(final ReplaceBiomeBlocks e){
		BlockReplacer.runReplacement(ReplacementStage.BIOME_BLOCKS, e.getWorld(), new ChunkPos(e.getX(), e.getZ()), e.getPrimer());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPrePopulateBiome(final PopulateChunkEvent.Pre e){
		BlockReplacer.runReplacement(ReplacementStage.PRE_POPULATE, e.getWorld(), new ChunkPos(e.getChunkX(), e.getChunkZ()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPostPopulateBiome(final PopulateChunkEvent.Post e){
		BlockReplacer.runReplacement(ReplacementStage.POST_POPULATE, e.getWorld(), new ChunkPos(e.getChunkX(), e.getChunkZ()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreDecorateBiome(final DecorateBiomeEvent.Pre e){
		BlockReplacer.runReplacement(ReplacementStage.PRE_DECORATE, e.getWorld(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreGenerateOres(final OreGenEvent.Pre e){
		BlockReplacer.runReplacement(ReplacementStage.PRE_ORES, e.getWorld(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreGenerateOres(final OreGenEvent.Post e){
		BlockReplacer.runReplacement(ReplacementStage.POST_ORES, e.getWorld(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPostDecorateBiome(final DecorateBiomeEvent.Post e){
		BlockReplacer.runReplacement(ReplacementStage.POST_DECORATE, e.getWorld(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetGrassColor(final GetGrassColor e){
		try {
			if(this.grassColor == null)
				this.grassColor = Biome.class.getDeclaredField("grassColor");
			final int id = Biome.getIdForBiome(e.getBiome());
			int newColor = this.colorCache[id];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[id]) != -2)
				e.setNewColor(newColor);
			else{
				newColor = this.grassColor.getInt(e.getBiome());
				this.colorCache[id] = newColor;
				if(newColor == -1)
					return;
				e.setNewColor(newColor);
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
				this.foliageColor = Biome.class.getDeclaredField("foliageColor");
			final int id = Biome.getIdForBiome(e.getBiome());
			int newColor = this.colorCache[id+256];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[id+256]) != -2)
				e.setNewColor(newColor);
			else{
				newColor = this.foliageColor.getInt(e.getBiome());
				this.colorCache[id+256] = newColor;
				if(newColor == -1)
					return;
				e.setNewColor(newColor);
			}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process getFoliageColor event!");
			e1.printStackTrace();
		}
	}
	//No longer needed
	/*
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetWaterColor(final GetWaterColor e){
		try {
			final int id = Biome.getIdForBiome(e.getBiome());
			int newColor = this.colorCache[id+512];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[id+512]) != -2)
				e.setNewColor(newColor);
			else{
				newColor = e.getBiome().waterColor;
				this.colorCache[id+512] = newColor;
				if(newColor == -1)
					return;
				e.setNewColor(newColor);
			}
		} catch (final Exception e1) {
			LogHelper.error("Failed to process getWaterColor event!");
			e1.printStackTrace();
		}
	}*/

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBiomeDecorate(final DecorateBiomeEvent.Decorate e){
		if(BiomeEventHandler.decorateTypes.isEmpty())
			return;
		final Biome gen = e.getWorld().getBiome(e.getPos());
		final boolean isAll = BiomeEventHandler.decorateTypes.containsKey(-1);
		if((isAll || BiomeEventHandler.decorateTypes.containsKey(Biome.getIdForBiome(gen))) && (BiomeEventHandler.decorateTypes.get(isAll ? -1:Biome.getIdForBiome(gen)).contains(e.getType().name()) || BiomeEventHandler.decorateTypes.get(isAll ? -1:Biome.getIdForBiome(gen)).contains("all")))
			e.setResult(Result.DENY);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBiomePopulate(final PopulateChunkEvent.Populate e){
		if(BiomeEventHandler.populateTypes.isEmpty())
			return;
		final Biome gen = e.getWorld().getBiome(new BlockPos(e.getChunkX(), 0, e.getChunkZ()));
		final boolean isAll = BiomeEventHandler.populateTypes.containsKey(-1);
		if((isAll || BiomeEventHandler.populateTypes.containsKey(Biome.getIdForBiome(gen))) && (BiomeEventHandler.populateTypes.get(isAll ? -1:Biome.getIdForBiome(gen)).contains(e.getType().name()) || BiomeEventHandler.populateTypes.get(isAll ? -1:Biome.getIdForBiome(gen)).contains("all")))
			e.setResult(Result.DENY);
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onCreateBiomeDecorator(final BiomeEvent.CreateDecorator e){
		final int id = Biome.getIdForBiome(e.getBiome());
		if(BiomeEventHandler.waterlilyPerChunk.containsKey(id))
			e.getNewBiomeDecorator().waterlilyPerChunk = BiomeEventHandler.waterlilyPerChunk.get(id);
		if(BiomeEventHandler.treesPerChunk.containsKey(id))
			e.getNewBiomeDecorator().treesPerChunk = BiomeEventHandler.treesPerChunk.get(id);
		if(BiomeEventHandler.flowersPerChunk.containsKey(id))
			e.getNewBiomeDecorator().flowersPerChunk = BiomeEventHandler.flowersPerChunk.get(id);
		if(BiomeEventHandler.grassPerChunk.containsKey(id))
			e.getNewBiomeDecorator().grassPerChunk = BiomeEventHandler.grassPerChunk.get(id);
		if(BiomeEventHandler.deadBushPerChunk.containsKey(id))
			e.getNewBiomeDecorator().deadBushPerChunk = BiomeEventHandler.deadBushPerChunk.get(id);
		if(BiomeEventHandler.mushroomPerChunk.containsKey(id))
			e.getNewBiomeDecorator().mushroomsPerChunk = BiomeEventHandler.mushroomPerChunk.get(id);
		if(BiomeEventHandler.reedsPerChunk.containsKey(id))
			e.getNewBiomeDecorator().reedsPerChunk = BiomeEventHandler.reedsPerChunk.get(id);
		if(BiomeEventHandler.cactiPerChunk.containsKey(id))
			e.getNewBiomeDecorator().cactiPerChunk = BiomeEventHandler.cactiPerChunk.get(id);
		if(BiomeEventHandler.sandPerChunk.containsKey(id))
			e.getNewBiomeDecorator().sandPerChunk = BiomeEventHandler.sandPerChunk.get(id);
		if(BiomeEventHandler.clayPerChunk.containsKey(id))
			e.getNewBiomeDecorator().clayPerChunk = BiomeEventHandler.clayPerChunk.get(id);
		if(BiomeEventHandler.bigMushroomsPerChunk.containsKey(id))
			e.getNewBiomeDecorator().bigMushroomsPerChunk = BiomeEventHandler.bigMushroomsPerChunk.get(id);
	}

	@SubscribeEvent
	public void onGetBiomeSize(final WorldTypeEvent.BiomeSize e){
		if(BiomeEventHandler.globalSize != -1)
			e.setNewSize(BiomeEventHandler.globalSize);
		else if(BiomeEventHandler.sizes.containsKey(e.getWorldType()))
			e.setNewSize(BiomeEventHandler.sizes.get(e.getWorldType()));
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onInitBiomeGens(final WorldTypeEvent.InitBiomeGens e){
		e.getNewBiomeGens()[0] = new GenLayerReplacement(e.getNewBiomeGens()[0]);
		e.getNewBiomeGens()[1] = new GenLayerReplacement(e.getNewBiomeGens()[1]);
	}

	@SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
	public void onReplaceVillageBlockID(final BiomeEvent.GetVillageBlockID e){
		if((e.getBiome() == null) || (e.getOriginal() == null))
			return;
		final List<Pair<Pair<Block, Integer>, Pair<Block, Integer>>> list = BiomeEventHandler.villageBlockReplacements.get(Biome.getIdForBiome(e.getBiome()));
		if((list == null) || list.isEmpty())
			return;
		for(final Pair<Pair<Block, Integer>, Pair<Block, Integer>> fPair:list)
			if(fPair.getKey().getKey() == (e.getReplacement() == null ? e.getOriginal().getBlock():e.getReplacement().getBlock())){
				Integer meta = fPair.getKey().getValue();
				boolean shouldDo = meta == null;
				if(!shouldDo)
					shouldDo = meta == fPair.getKey().getKey().getMetaFromState(e.getReplacement() == null ? e.getOriginal():e.getReplacement());
				if(shouldDo){
					meta = fPair.getValue().getValue();
					e.setReplacement(fPair.getValue().getKey().getStateFromMeta(meta == null ? 0:meta));
					e.setResult(Result.DENY);
					break;
				}
			}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldLoad(final WorldEvent.Load e){
		if(!BiomeTweakerCore.modifySuccess){
			final boolean cont = StartupQuery.confirm("WARNING\n\nBiomeTweaker has failed to verify the integrity of its ASM modifications.\n "
					+ "This could cause some features to not work or spam errors,\n leading to unpredicatable world generation and possibly corruption.\n "
					+ "Please report this to the issue tracker with a full log file.\n\n"
					+ "https://github.com/superckl/BiomeTweaker/issues\n\n"
					+ "Continue anyway?");
			if(cont)
				BiomeTweakerCore.modifySuccess = true;
			else
				FMLCommonHandler.instance().exitJava(1, false);
		}
	}

}
