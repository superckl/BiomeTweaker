package me.superckl.api.biometweaker.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;


public abstract class BlockStateBuilder<V extends BlockState> {

	protected ResourceLocation rLoc = new ResourceLocation("minecraft:stone");
	protected Map<String, String> properties = new HashMap<>();

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
