package me.superckl.api.biometweaker.world.gen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import net.minecraft.world.level.block.state.BlockState;

@Data
@Accessors(fluent = true)
public class ReplacementConstraints {

	private BlockStateBuilder<? extends BlockState> builder;
	private int minY = Integer.MIN_VALUE;
	private int maxY = Integer.MAX_VALUE;
	private int minX = Integer.MIN_VALUE;
	private int maxX = Integer.MAX_VALUE;
	private int minZ = Integer.MIN_VALUE;
	private int maxZ = Integer.MAX_VALUE;
	private int minChunkX = Integer.MIN_VALUE;
	private int maxChunkX = Integer.MAX_VALUE;
	private int minChunkZ = Integer.MIN_VALUE;
	private int maxChunkZ = Integer.MAX_VALUE;
	private boolean ignoreMeta = false;
	private boolean contiguous = false;

	@Setter(AccessLevel.NONE)
	private transient BlockState block;

	public BlockState getState() {
		if(this.block == null) {
			if(this.builder == null)
				throw new IllegalStateException("No BlockState has been specified!");
			this.block = this.builder.build();
		}
		return this.block;
	}

	public boolean hasBlock() {
		return this.block != null || this.builder != null;
	}

}
