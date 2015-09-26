package me.superckl.biometweaker.util;

import net.minecraft.world.ChunkCoordIntPair;

public class NumberHelper {

	public static ChunkCoordIntPair[] fillGrid(final int radius, final ChunkCoordIntPair base){
		return NumberHelper.fillGrid(radius, radius, base);
	}

	public static ChunkCoordIntPair[] fillGrid(final int xRadius, final int zRadius, final ChunkCoordIntPair base){
		final int xDiam = (2*xRadius)+1, zDiam = (2*zRadius)+1;
		final int xStart = base.chunkXPos - xRadius, zStart = base.chunkZPos - zRadius;
		final ChunkCoordIntPair[] array = new ChunkCoordIntPair[xDiam*zDiam];
		for(int x = 0; x < xDiam; x++)
			for(int z = 0; z < zDiam; z++)
				array[(x*zDiam) + z] = new ChunkCoordIntPair(x+xStart, z+zStart);
		return array;
	}

}
