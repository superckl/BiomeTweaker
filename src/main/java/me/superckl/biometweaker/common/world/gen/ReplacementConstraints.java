package me.superckl.biometweaker.common.world.gen;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import net.minecraft.block.state.IBlockState;

@Data
public class ReplacementConstraints {

	private BlockStateBuilder<? extends IBlockState> block;
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

	@Setter(AccessLevel.NONE)
	private transient IBlockState state;

	public IBlockState getState() {
		if(this.state == null) {
			if(this.block == null)
				throw new IllegalStateException("No BlockState has been specified!");
			this.state = this.block.build();
		}
		return this.state;
	}

}
