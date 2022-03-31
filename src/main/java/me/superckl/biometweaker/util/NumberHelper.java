package me.superckl.biometweaker.util;

import java.util.stream.Stream;

import net.minecraft.world.level.ChunkPos;

public class NumberHelper {

	public static Stream<ChunkPos> fillGrid(final int xRadius, final int zRadius, final ChunkPos base){
		return ChunkPos.rangeClosed(new ChunkPos(base.x - xRadius, base.z - zRadius), new ChunkPos(base.x + xRadius, base.z + zRadius));
	}

}
