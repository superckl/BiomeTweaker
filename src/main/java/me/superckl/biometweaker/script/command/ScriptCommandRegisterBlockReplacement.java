package me.superckl.biometweaker.script.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.common.handler.BiomeEventHandler.WeightedBlockEntry;
import me.superckl.biometweaker.config.Config;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

public class ScriptCommandRegisterBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int weight;
	private final String toReplace;
	private final Integer toReplaceMeta;
	private final String replaceWith;
	private final Integer replaceWithMeta;

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final Integer toReplaceMeta, final String block2, final Integer replaceWithMeta) {
		this.weight = weight;
		this.pack = pack;
		this.toReplace = block1;
		this.replaceWith = block2;
		this.toReplaceMeta = toReplaceMeta;
		this.replaceWithMeta = replaceWithMeta;
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2, final Integer replaceWithMeta) {
		this(pack, 1, block1, toReplaceMeta, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2, final Integer replaceWithMeta) {
		this(pack, block1, null, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final String block2, final Integer replaceWithMeta) {
		this(pack, weight, block1, null, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2) {
		this(pack, block1, toReplaceMeta, block2, null);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final Integer toReplaceMeta, final String block2) {
		this(pack, weight, block1, toReplaceMeta, block2, null);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2) {
		this(pack, block1, null, block2, null);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final String block2) {
		this(pack, weight, block1, null, block2, null);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase > it = this.pack.getIterator();
		final Block toReplace = Block.getBlockFromName(this.toReplace);
		if(toReplace == null)
			throw new IllegalArgumentException("Failed to find block "+this.toReplace+"! Tweak will not be applied.");
		final Block replaceWith = Block.getBlockFromName(this.replaceWith);
		if(replaceWith == null)
			throw new IllegalArgumentException("Failed to find block "+this.replaceWith+"! Tweak will not be applied.");
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RegisterGenBlockReplacement(this, this.weight, gen, toReplace, this.toReplaceMeta, replaceWith, this.replaceWithMeta)))
				continue;
			if(!BiomeEventHandler.getBlockReplacements().containsKey(gen.biomeID))
				BiomeEventHandler.getBlockReplacements().put(gen.biomeID, new ArrayList<Pair<Pair<Block, Integer>, List<WeightedBlockEntry>>>());
			//LogHelper.info("Registering replacement for "+gen.biomeID+":"+gen.biomeName);
			final List<Pair<Pair<Block, Integer>, List<WeightedBlockEntry>>> list = BiomeEventHandler.getBlockReplacements().get(gen.biomeID);
			final Pair<Block, Integer> toReplacePair = Pair.of(toReplace, this.toReplaceMeta);
			List<WeightedBlockEntry> entries = null;
			for(final Pair<Pair<Block, Integer>, List<WeightedBlockEntry>> pair:list)
				if(pair.getKey().equals(toReplacePair)){
					entries = pair.getValue();
					break;
				}
			if(entries == null){
				entries = Lists.newArrayList();
				list.add(Pair.of(toReplacePair, entries));
			}
			entries.add(new WeightedBlockEntry(this.weight, Pair.of(replaceWith, this.replaceWithMeta)));
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
