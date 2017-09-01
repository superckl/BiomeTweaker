package me.superckl.api.biometweaker.block;

import java.util.Iterator;
import java.util.Map.Entry;

import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BasicBlockStateBuilder extends BlockStateBuilder<IBlockState>{

	@Override
	public IBlockState build() {
		final Block block = Block.REGISTRY.getObject(this.rLoc);
		if(block == null)
			throw new IllegalArgumentException("No block found for resource location "+this.rLoc);
		final BlockStateContainer container = block.getBlockState();
		IBlockState state = container.getBaseState();
		Iterator<Entry<String, String>> it = this.properties.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			final IProperty<?> prop = container.getProperty(entry.getKey());
			if(prop == null){
				LogHelper.error("No property "+entry.getKey()+" found for block "+this.rLoc+". Skipping it.");
				it.remove();
				continue;
			}
			if(!prop.parseValue(entry.getValue()).isPresent()){
				LogHelper.error("Value "+entry.getValue()+" is not valid for property "+entry.getKey()+". Skipping it.");
				continue;
			}
			state = this.withProperty(state, prop, entry.getValue());
		}
		return state;
	}

	/**
	 * This method is just a workaround for wildcards. Only to be used internally.
	 */
	private <T extends Comparable<T>> IBlockState withProperty(final IBlockState state, final IProperty<T> prop, final String value){
		return state.withProperty(prop, prop.parseValue(value).get());
	}

}
