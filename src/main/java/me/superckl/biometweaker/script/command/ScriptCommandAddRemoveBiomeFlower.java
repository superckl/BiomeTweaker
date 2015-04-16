package me.superckl.biometweaker.script.command;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.IBiomePackage;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.FlowerEntry;

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
				ScriptCommandAddRemoveBiomeFlower.field = BiomeGenBase.class.getDeclaredField("flowers");
				ScriptCommandAddRemoveBiomeFlower.field.setAccessible(true);
			}
			final Block block = Block.getBlockFromName(this.block);
			final Iterator<BiomeGenBase> it = this.pack.getIterator();
			while(it.hasNext()){
				final BiomeGenBase gen = it.next();
				final List<FlowerEntry> flowers = (List<FlowerEntry>) ScriptCommandAddRemoveBiomeFlower.field.get(gen);
				final Iterator<FlowerEntry> itF = flowers.iterator();
				while(itF.hasNext()){
					final FlowerEntry entry = itF.next();
					if((entry.block == block) && (entry.metadata == this.meta))
						itF.remove();
				}
				Config.INSTANCE.onTweak(gen.biomeID);
			}
		}else{
			final Block block = Block.getBlockFromName(this.block);
			final Iterator<BiomeGenBase> it = this.pack.getIterator();
			while(it.hasNext()){
				final BiomeGenBase gen = it.next();
				gen.addFlower(block, this.meta, this.weight);
				Config.INSTANCE.onTweak(gen.biomeID);
			}
		}
	}

}
