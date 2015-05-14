package me.superckl.biometweaker.script.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.superckl.biometweaker.common.event.BiomeTweakEvent;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.tuple.Pair;

public class ScriptCommandRegisterBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final String toReplace;
	private final Integer toReplaceMeta;
	private final String replaceWith;
	private final Integer replaceWithMeta;

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2, final Integer replaceWithMeta) {
		this.pack = pack;
		this.toReplace = block1;
		this.replaceWith = block2;
		this.toReplaceMeta = toReplaceMeta;
		this.replaceWithMeta = replaceWithMeta;
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2, final Integer replaceWithMeta) {
		this(pack, block1, null, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final Integer toReplaceMeta, final String block2) {
		this(pack, block1, toReplaceMeta, block2, null);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2) {
		this(pack, block1, null, block2, null);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase > it = this.pack.getIterator();
		final Block toReplace = Block.getBlockFromName(this.toReplace);
		final Block replaceWith = Block.getBlockFromName(this.replaceWith);
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RegisterGenBlockReplacement(this, gen, toReplace, this.toReplaceMeta, replaceWith, this.replaceWithMeta)))
				continue;
			if(!BiomeEventHandler.getBlockReplacements().containsKey(gen.biomeID))
				BiomeEventHandler.getBlockReplacements().put(gen.biomeID, new ArrayList<Pair<Pair<Block, Integer>, Pair<Block, Integer>>>());
			final List<Pair<Pair<Block, Integer>, Pair<Block, Integer>>> list = BiomeEventHandler.getBlockReplacements().get(gen.biomeID);
			list.add(Pair.of(Pair.of(toReplace, this.toReplaceMeta), Pair.of(replaceWith, this.replaceWithMeta)));
			Config.INSTANCE.onTweak(gen.biomeID);
		}
	}

}
