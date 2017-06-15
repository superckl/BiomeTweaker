package me.superckl.biometweaker.script.command.generation;

import java.util.Iterator;

import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.world.TweakWorldManager;
import me.superckl.biometweaker.common.world.gen.BlockReplacementManager;
import me.superckl.biometweaker.config.Config;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

public class ScriptCommandRegisterBlockReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int weight;
	private final String toReplace;
	private final int toReplaceMeta;
	private final String replaceWith;
	private final int replaceWithMeta;

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final int toReplaceMeta, final String block2, final int replaceWithMeta) {
		this.pack = pack;
		this.weight = weight;
		this.toReplace = block1;
		this.replaceWith = block2;
		this.toReplaceMeta = toReplaceMeta;
		this.replaceWithMeta = replaceWithMeta;
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final int toReplaceMeta, final String block2, final int replaceWithMeta) {
		this(pack, 1, block1, toReplaceMeta, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2, final int replaceWithMeta) {
		this(pack, block1, -1, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final String block2, final int replaceWithMeta) {
		this(pack, weight, block1, -1, block2, replaceWithMeta);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final int toReplaceMeta, final String block2) {
		this(pack, block1, toReplaceMeta, block2, 0);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final int toReplaceMeta, final String block2) {
		this(pack, weight, block1, toReplaceMeta, block2, 0);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final String block1, final String block2) {
		this(pack, block1, -1, block2, 0);
	}

	public ScriptCommandRegisterBlockReplacement(final IBiomePackage pack, final int weight, final String block1, final String block2) {
		this(pack, weight, block1, -1, block2, 0);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		final Block toReplace = Block.getBlockFromName(this.toReplace);
		if(toReplace == null)
			throw new IllegalArgumentException("Failed to find block "+this.toReplace+"! Tweak will not be applied.");
		final Block replaceWith = Block.getBlockFromName(this.replaceWith);
		if(replaceWith == null)
			throw new IllegalArgumentException("Failed to find block "+this.replaceWith+"! Tweak will not be applied.");
		while(it.hasNext()){
			final Biome gen = it.next();
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RegisterGenBlockReplacement(this, this.weight, gen, toReplace, this.toReplaceMeta, replaceWith, this.replaceWithMeta)))
				continue;
			if(TweakWorldManager.getCurrentWorld() == null)
				BlockReplacementManager.registerGlobalBlockReplacement(Biome.getIdForBiome(gen), this.weight, toReplace, this.toReplaceMeta, replaceWith, this.replaceWithMeta);
			else
				BlockReplacementManager.getManagerForWorld(TweakWorldManager.getCurrentWorld()).registerBlockReplacement(Biome.getIdForBiome(gen),
						this.weight, toReplace, this.toReplaceMeta, replaceWith, this.replaceWithMeta);
			Config.INSTANCE.onTweak(Biome.getIdForBiome(gen));
		}
	}

}
