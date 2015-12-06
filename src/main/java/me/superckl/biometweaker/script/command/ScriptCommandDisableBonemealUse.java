package me.superckl.biometweaker.script.command;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import gnu.trove.map.TIntObjectMap;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandDisableBonemealUse implements IScriptCommand{

	private final IBiomePackage pack;
	private final String block;

	public ScriptCommandDisableBonemealUse(final IBiomePackage pack) {
		this(pack, null);
	}

	@Override
	public void perform() throws Exception {
		final Block block = this.block == null ? null:Block.getBlockFromName(this.block);
		if((block == null) && (this.block != null))
			throw new IllegalArgumentException("Failed to find block "+this.block+"! Tweak will not be applied.");
		final Iterator<BiomeGenBase > it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase biome = it.next();
			final TIntObjectMap<List<Block>> map = EntityEventHandler.getNoBonemeals();
			if(block == null){
				map.put(biome.biomeID, null);
				continue;
			}
			if(!map.containsKey(biome.biomeID))
				map.put(biome.biomeID, Lists.<Block>newArrayList());
			final List<Block> list = map.get(biome.biomeID);
			if(list != null)
				list.add(block);
		}
	}

}
