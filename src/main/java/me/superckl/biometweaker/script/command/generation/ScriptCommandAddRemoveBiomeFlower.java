package me.superckl.biometweaker.script.command.generation;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.FlowerEntry;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveBiomeFlower implements IScriptCommand{

	private static Field field;

	private final IBiomePackage pack;
	private final boolean remove;
	private final String block;
	private final int meta;
	private final int weight;

	public ScriptCommandAddRemoveBiomeFlower(final IBiomePackage pack, final String block, final int meta) {
		this(pack, true, block, meta, 0);
	}

	public ScriptCommandAddRemoveBiomeFlower(final IBiomePackage pack, final String block, final int meta, final int weight) {
		this(pack, false, block, meta, weight);
	}

	@Override
	public void perform() throws Exception {
		if(this.remove){
			if(ScriptCommandAddRemoveBiomeFlower.field == null){
				ScriptCommandAddRemoveBiomeFlower.field = Biome.class.getDeclaredField("flowers");
				ScriptCommandAddRemoveBiomeFlower.field.setAccessible(true);
			}
			final Block block = Block.getBlockFromName(this.block);
			if(block == null)
				throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
			final Iterator<Biome> it = this.pack.getIterator();
			while(it.hasNext()){
				final Biome gen = it.next();
				if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveFlower(this, gen, block, this.meta)))
					continue;
				final List<FlowerEntry> flowers = WarningHelper.uncheckedCast(ScriptCommandAddRemoveBiomeFlower.field.get(gen));
				final Iterator<FlowerEntry> itF = flowers.iterator();
				while(itF.hasNext()){
					final FlowerEntry entry = itF.next();
					if((entry.state.getBlock() == block) && (block.getMetaFromState(entry.state) == this.meta))
						itF.remove();
				}
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
			}
		}else{
			final Block block = Block.getBlockFromName(this.block);
			if(block == null)
				throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
			final Iterator<Biome> it = this.pack.getIterator();
			while(it.hasNext()){
				final Biome gen = it.next();
				if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddFlower(this, gen, block, this.meta, this.weight)))
					continue;
				gen.addFlower(block.getStateFromMeta(this.meta), this.weight);
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
			}
		}
	}

}
