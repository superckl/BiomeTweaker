package me.superckl.biometweaker.common.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

public class DummyWorld extends World{

	public DummyWorld(final WorldInfo info) {
		super(null, info, new DummyWorldProvider(), new Profiler(), true);
	}

	@Override
	protected IChunkProvider createChunkProvider() {
		return null;
	}

	@Override
	protected boolean isChunkLoaded(final int x, final int z, final boolean allowEmpty) {
		return false;
	}

}
