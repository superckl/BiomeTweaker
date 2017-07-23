package me.superckl.biometweaker.script.command.misc;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import gnu.trove.map.TIntObjectMap;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "disableBonemealUse")
@RequiredArgsConstructor
public class ScriptCommandDisableBonemealUse extends ScriptCommand{

	private final BiomePackage pack;
	private final BlockStateBuilder<?> block;

	public ScriptCommandDisableBonemealUse(final BiomePackage pack) {
		this(pack, null);
	}

	@Override
	public void perform() throws Exception {
		final IBlockState block = this.block == null ? null:this.block.build();
		final Iterator<Biome > it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome biome = it.next();
			final TIntObjectMap<List<IBlockState>> map = EntityEventHandler.getNoBonemeals();
			final int id = Biome.getIdForBiome(biome);
			if(block == null){
				map.put(id, null);
				continue;
			}
			if(!map.containsKey(id))
				map.put(id, Lists.newArrayList());
			final List<IBlockState> list = map.get(id);
			if(list != null)
				list.add(block);
		}
	}

}
