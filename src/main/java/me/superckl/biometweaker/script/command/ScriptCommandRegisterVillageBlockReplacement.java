package me.superckl.biometweaker.script.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.config.Config;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public class ScriptCommandRegisterVillageBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final String toReplace;
	private final String replaceWith;
	private final Integer replaceWithMeta;

	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final String block2, final Integer replaceWithMeta) {
		this.pack = pack;
		this.toReplace = block1;
		this.replaceWith = block2;
		this.replaceWithMeta = replaceWithMeta;
	}

	public ScriptCommandRegisterVillageBlockReplacement(final IBiomePackage pack, final String block1, final String block2) {
		this(pack, block1,  block2, null);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase > it = this.pack.getIterator();
		final Block toReplace = Block.getBlockFromName(this.toReplace);
		if(toReplace == null)
			throw new IllegalArgumentException("Failed to find block "+this.toReplace+"! Tweak will not be applied.");
		final Block replaceWith = Block.getBlockFromName(this.replaceWith);
		if(replaceWith == null)
			throw new IllegalArgumentException("Failed to find block "+this.toReplace+"! Tweak will not be applied.");
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(!BiomeEventHandler.getVillageBlockReplacements().containsKey(gen.biomeID))
				BiomeEventHandler.getVillageBlockReplacements().put(gen.biomeID, new ArrayList<Pair<Block, Pair<Block, Integer>>>());
			//LogHelper.info("Registering replacement for "+gen.biomeID+":"+gen.biomeName);
			final List<Pair<Block, Pair<Block, Integer>>> list = BiomeEventHandler.getVillageBlockReplacements().get(gen.biomeID);
			final Iterator<Pair<Block, Pair<Block, Integer>>> it2 = list.iterator();
			while(it2.hasNext())
				if(it2.next().getKey() == toReplace){
					it2.remove();
					break;
				}
			list.add(Pair.of(toReplace, Pair.of(replaceWith, this.replaceWithMeta)));
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
