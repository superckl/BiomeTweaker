package me.superckl.biometweaker.common.handler;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.custom_hash.TObjectByteCustomHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import lombok.Getter;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.biometweaker.common.world.gen.BlockReplacer;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import me.superckl.biometweaker.common.world.gen.feature.Decorator;
import me.superckl.biometweaker.common.world.gen.layer.GenLayerReplacement;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent.ReplaceBiomeBlocks;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BiomeEventHandler {

	public static byte globalSize = -1;
	public static final TObjectByteCustomHashMap<Object> sizes = new TObjectByteCustomHashMap<>(IdentityHashingStrategy.INSTANCE);

	@Getter
	private static final TIntObjectMap<List<Pair<IBlockState, IBlockState>>> villageBlockReplacements = new TIntObjectHashMap<>();
	@Getter
	private static final TIntObjectMap<List<String>> decorateTypes = new TIntObjectHashMap<>();
	@Getter
	private static final TIntObjectMap<List<String>> populateTypes = new TIntObjectHashMap<>();
	@Getter
	private static final TIntObjectMap<List<String>> oreTypes = new TIntObjectHashMap<>();
	@Getter
	private static final Map<EventType, TIntIntMap> decorationsPerChunk = new EnumMap<>(EventType.class);

	private final int[] colorCache = new int[512];



	public BiomeEventHandler() {
		Arrays.fill(this.colorCache, -2);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onReplaceBlocks(final ReplaceBiomeBlocks e){
		//Don't decorate here, too early
		BlockReplacer.runReplacement(PlacementStage.BIOME_BLOCKS, e.getWorld(), e.getWorld().rand, new ChunkPos(e.getX(), e.getZ()), e.getPrimer());
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPrePopulateBiome(final PopulateChunkEvent.Pre e){
		Decorator.runDecoration(PlacementStage.PRE_POPULATE, e.getWorld(), e.getRand(), new ChunkPos(e.getChunkX(), e.getChunkZ()));
		BlockReplacer.runReplacement(PlacementStage.PRE_POPULATE, e.getWorld(), e.getRand(), new ChunkPos(e.getChunkX(), e.getChunkZ()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPostPopulateBiome(final PopulateChunkEvent.Post e){
		Decorator.runDecoration(PlacementStage.POST_POPULATE, e.getWorld(), e.getRand(), new ChunkPos(e.getChunkX(), e.getChunkZ()));
		BlockReplacer.runReplacement(PlacementStage.POST_POPULATE, e.getWorld(), e.getRand(), new ChunkPos(e.getChunkX(), e.getChunkZ()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreDecorateBiome(final DecorateBiomeEvent.Pre e){
		Decorator.runDecoration(PlacementStage.PRE_DECORATE, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()));
		BlockReplacer.runReplacement(PlacementStage.PRE_DECORATE, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreGenerateOres(final OreGenEvent.Pre e){
		Decorator.runDecoration(PlacementStage.PRE_ORES, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()));
		BlockReplacer.runReplacement(PlacementStage.PRE_ORES, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPreGenerateOres(final OreGenEvent.Post e){
		Decorator.runDecoration(PlacementStage.POST_ORES, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()));
		BlockReplacer.runReplacement(PlacementStage.POST_ORES, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPostDecorateBiome(final DecorateBiomeEvent.Post e){
		Decorator.runDecoration(PlacementStage.POST_DECORATE, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()));
		BlockReplacer.runReplacement(PlacementStage.POST_DECORATE, e.getWorld(), e.getRand(), new ChunkPos(e.getPos()), null);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onGetGrassColor(final GetGrassColor e){
		if(BiomePropertyManager.GRASS_COLOR == null)
			return;
		try {
			final int id = Biome.getIdForBiome(e.getBiome());
			int newColor = this.colorCache[id];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[id]) != -2)
				e.setNewColor(newColor);
			else{
				newColor = BiomePropertyManager.GRASS_COLOR.get(e.getBiome());
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
		if(BiomePropertyManager.FOLIAGE_COLOR == null)
			return;
		try {
			final int id = Biome.getIdForBiome(e.getBiome());
			int newColor = this.colorCache[id+256];
			if(newColor == -1)
				return;
			else if((newColor = this.colorCache[id+256]) != -2)
				e.setNewColor(newColor);
			else{
				newColor = BiomePropertyManager.FOLIAGE_COLOR.get(e.getBiome());
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

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBiomeDecorate(final DecorateBiomeEvent.Decorate e){
		if(BiomeEventHandler.decorateTypes.isEmpty())
			return;
		final Biome gen = e.getWorld().getBiome(e.getPos());
		if((BiomeEventHandler.decorateTypes.containsKey(Biome.getIdForBiome(gen))) && (BiomeEventHandler.decorateTypes.get(Biome.getIdForBiome(gen)).contains(e.getType().name()) || BiomeEventHandler.decorateTypes.get(Biome.getIdForBiome(gen)).contains("all")))
			e.setResult(Result.DENY);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBiomePopulate(final PopulateChunkEvent.Populate e){
		if(BiomeEventHandler.populateTypes.isEmpty())
			return;
		final Biome gen = e.getWorld().getBiome(new ChunkPos(e.getChunkX(), e.getChunkZ()).getBlock(8, 0, 8));
		if((BiomeEventHandler.populateTypes.containsKey(Biome.getIdForBiome(gen))) && (BiomeEventHandler.populateTypes.get(Biome.getIdForBiome(gen)).contains(e.getType().name()) || BiomeEventHandler.populateTypes.get(Biome.getIdForBiome(gen)).contains("all")))
			e.setResult(Result.DENY);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onOreGen(final OreGenEvent.GenerateMinable e){
		if(BiomeEventHandler.oreTypes.isEmpty())
			return;
		final Biome gen = e.getWorld().getBiome(e.getPos());
		if((BiomeEventHandler.oreTypes.containsKey(Biome.getIdForBiome(gen))) && (BiomeEventHandler.oreTypes.get(Biome.getIdForBiome(gen)).contains(e.getType().name()) || BiomeEventHandler.oreTypes.get(Biome.getIdForBiome(gen)).contains("all")))
			e.setResult(Result.DENY);
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onCreateBiomeDecorator(final BiomeEvent.CreateDecorator e){
		final int id = Biome.getIdForBiome(e.getBiome());
		for(final Entry<EventType, TIntIntMap> entry:BiomeEventHandler.decorationsPerChunk.entrySet()){
			if(!entry.getValue().containsKey(id))
				continue;
			switch(entry.getKey()){
			case LILYPAD:{
				e.getNewBiomeDecorator().waterlilyPerChunk = entry.getValue().get(id);
				break;
			}
			case TREE:{
				e.getNewBiomeDecorator().treesPerChunk = entry.getValue().get(id);
				break;
			}
			case FLOWERS:{
				e.getNewBiomeDecorator().flowersPerChunk = entry.getValue().get(id);
				break;
			}
			case GRASS:{
				e.getNewBiomeDecorator().grassPerChunk = entry.getValue().get(id);
				break;
			}
			case DEAD_BUSH:{
				e.getNewBiomeDecorator().deadBushPerChunk = entry.getValue().get(id);
				break;
			}
			case SHROOM:{
				e.getNewBiomeDecorator().mushroomsPerChunk = entry.getValue().get(id);
				break;
			}
			case REED:{
				e.getNewBiomeDecorator().reedsPerChunk = entry.getValue().get(id);
				break;
			}
			case CACTUS:{
				e.getNewBiomeDecorator().cactiPerChunk = entry.getValue().get(id);
				break;
			}
			case SAND:{
				e.getNewBiomeDecorator().sandPatchesPerChunk = entry.getValue().get(id);
				break;
			}
			case CLAY:{
				e.getNewBiomeDecorator().clayPerChunk = entry.getValue().get(id);
				break;
			}
			case BIG_SHROOM:{
				e.getNewBiomeDecorator().bigMushroomsPerChunk = entry.getValue().get(id);
				break;
			}
			default:
				break;
			}
		}
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
		final List<Pair<IBlockState, IBlockState>> list = BiomeEventHandler.villageBlockReplacements.get(Biome.getIdForBiome(e.getBiome()));
		if((list == null) || list.isEmpty())
			return;
		for(final Pair<IBlockState, IBlockState> fPair:list)
			if(fPair.getKey().getBlock() == (e.getReplacement() == null ? e.getOriginal().getBlock():e.getReplacement().getBlock())){
				final Integer meta = fPair.getKey().getBlock().getMetaFromState(fPair.getKey());
				boolean shouldDo = false;
				if(!shouldDo)
					shouldDo = meta == (e.getReplacement() == null ? e.getOriginal().getBlock().getMetaFromState(e.getOriginal()):e.getReplacement().getBlock().getMetaFromState(e.getReplacement()));
				if(shouldDo){
					e.setReplacement(fPair.getValue());
					e.setResult(Result.DENY);
					break;
				}
			}
	}

}
