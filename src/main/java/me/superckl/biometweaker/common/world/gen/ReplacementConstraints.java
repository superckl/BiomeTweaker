package me.superckl.biometweaker.common.world.gen;

import lombok.Data;
import net.minecraft.block.state.IBlockState;

@Data
public class ReplacementConstraints {

	private IBlockState block;
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

}
