package me.superckl.biometweaker.script.command.generation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;

public class ScriptCommandRegisterVillageBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final String toReplace;
	private final String replaceWith;
	private final Integer toReplaceMeta;
	private final Integer replaceWithMeta;

	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2, final Integer replaceWithMeta) {
		this.pack = pack;
		this.toReplace = block1;
		this.toReplaceMeta = toReplaceMeta;
		this.replaceWith = block2;
		this.replaceWithMeta = replaceWithMeta;
	}

	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final String block2) {
		this(pack, block1,  null, block2, null);
	}
	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2) {
		this(pack, block1,  toReplaceMeta, block2, null);
	}
	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final String block2, final Integer replaceWithMeta) {
		this(pack, block1,  null, block2, replaceWithMeta);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome > it = this.pack.getIterator();
		final Block toReplace = Block.getBlockFromName(this.toReplace);
		if(toReplace == null)
			throw new IllegalArgumentException("Failed to find block "+this.toReplace+"! Tweak will not be applied.");
		final Block replaceWith = Block.getBlockFromName(this.replaceWith);
		if(replaceWith == null)
			throw new IllegalArgumentException("Failed to find block "+this.replaceWith+"! Tweak will not be applied.");
		while(it.hasNext()){
			final Biome gen = it.next();
			if(!BiomeEventHandler.getVillageBlockReplacements().containsKey(Biome.getIdForBiome(gen)))
				BiomeEventHandler.getVillageBlockReplacements().put(Biome.getIdForBiome(gen), new ArrayList<Pair<Pair<Block, Integer>, Pair<Block, Integer>>>());
			final List<Pair<Pair<Block, Integer>, Pair<Block, Integer>>> list = BiomeEventHandler.getVillageBlockReplacements().get(Biome.getIdForBiome(gen));
			final Iterator<Pair<Pair<Block, Integer>, Pair<Block, Integer>>> it2 = list.iterator();
			final Pair<Block, Integer> toReplacePair = Pair.of(toReplace, this.toReplaceMeta);
			while(it2.hasNext()){
				final Pair<Pair<Block, Integer>, Pair<Block, Integer>> pair = it2.next();
				if(pair.getKey().equals(toReplacePair)){
					it2.remove();
					break;
				}
			}
			list.add(Pair.of(toReplacePair, Pair.of(replaceWith, this.replaceWithMeta)));
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
		}
	}

}
