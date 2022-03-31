package me.superckl.api.biometweaker.block;

import java.util.Iterator;
import java.util.Map.Entry;

import me.superckl.api.biometweaker.APIInfo;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class BasicBlockStateBuilder extends BlockStateBuilder<BlockState>{

	@Override
	public BlockState build() {
		if(!ForgeRegistries.BLOCKS.containsKey(this.rLoc))
			throw new IllegalArgumentException("No block found for resource location "+this.rLoc);
		final Block block = ForgeRegistries.BLOCKS.getValue(this.rLoc);
		final StateDefinition<Block, BlockState> container = block.getStateDefinition();
		BlockState state = block.defaultBlockState();
		final Iterator<Entry<String, String>> it = this.properties.entrySet().iterator();
		while(it.hasNext()){
			final Entry<String, String> entry = it.next();
			final Property<?> prop = container.getProperty(entry.getKey());
			if(prop == null){
				APIInfo.log.error("No property "+entry.getKey()+" found for block "+this.rLoc+". Skipping it.");
				it.remove();
				continue;
			}
			if(!prop.getValue(entry.getValue()).isPresent()){
				APIInfo.log.error("Value "+entry.getValue()+" is not valid for property "+entry.getKey()+". Skipping it.");
				continue;
			}
			state = this.withProperty(state, prop, entry.getValue());
		}
		return state;
	}

	/**
	 * This method is just a workaround for wildcards. Only to be used internally.
	 */
	private <T extends Comparable<T>> BlockState withProperty(final BlockState state, final Property<T> prop, final String value){
		return state.setValue(prop, prop.getValue(value).orElseThrow(() -> new IllegalArgumentException("No property found")));
	}

}
