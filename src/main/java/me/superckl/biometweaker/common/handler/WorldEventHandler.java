package me.superckl.biometweaker.common.handler;

import gnu.trove.set.TByteSet;
import gnu.trove.set.hash.TByteHashSet;
import lombok.Getter;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHandler {

	@Getter
	private static final TByteSet resetLight = new TByteHashSet(256);
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e){
		for(byte id:e.getChunk().getBiomeArray())
		if(resetLight.contains(id)){
			e.getChunk().resetRelightChecks();
			//TODO only do once
			return;
		}
	}
	
}
