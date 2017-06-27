package me.superckl.api.biometweaker.block;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBuilder<V extends IBlockState> {

	protected ResourceLocation rLoc = new ResourceLocation("minecraft:stone");
	protected Map<String, String> properties = Maps.newHashMap();

	public abstract V build();

	public ResourceLocation getrLoc() {
		return this.rLoc;
	}

	public void setrLoc(final ResourceLocation rLoc) {
		this.rLoc = rLoc;
	}

	public String getProperty(final String key) {
		return this.properties.get(key);
	}

	public void setProperty(final String key, final String properties) {
		this.properties.put(key, properties);
	}

}
