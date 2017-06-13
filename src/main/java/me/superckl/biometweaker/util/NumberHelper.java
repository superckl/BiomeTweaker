package me.superckl.biometweaker.util;

import net.minecraft.util.math.ChunkPos;

public class NumberHelper {

	public static ChunkPos[] fillGrid(final int radius, final ChunkPos base){
		return NumberHelper.fillGrid(radius, radius, base);
	}

	public static ChunkPos[] fillGrid(final int xRadius, final int zRadius, final ChunkPos base){
		final int xDiam = (2*xRadius)+1, zDiam = (2*zRadius)+1;
		final int xStart = base.x - xRadius, zStart = base.z - zRadius;
		final ChunkPos[] array = new ChunkPos[xDiam*zDiam];
		for(int x = 0; x < xDiam; x++)
			for(int z = 0; z < zDiam; z++)
				array[(x*zDiam) + z] = new ChunkPos(x+xStart, z+zStart);
		return array;
	}

}
