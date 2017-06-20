package me.superckl.biometweaker.common.handler;

import java.util.Set;

import com.google.common.collect.Sets;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.WorldSavedDataASMTweaks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldLoad(final WorldEvent.Load e){
		final WorldSavedDataASMTweaks data = WorldSavedDataASMTweaks.get(e.getWorld());
		if(data.isWasntSaved()){
			data.markDirty();
			return;
		}
		final Set<String> savedTweaks = data.getTweaks();
		final Set<String> currentTweaks = BiomeTweaker.getInstance().getEnabledTweaks();
		if(savedTweaks.size() != currentTweaks.size() || Sets.intersection(savedTweaks, currentTweaks).size() != savedTweaks.size()){
			final boolean cont = StartupQuery.confirm("WARNING\n\nBiomeTweaker has detected an inconsistency in enabled and saved tweaks.\n "
					+ "This could cause unpredicatable world generation and possibly corruption.\n "
					+ "Please ensure you did not change any BiomeTweakerCore configuration since\n"
					+ "last loading this world. Proceed at your own risk.\n\n"
					+ "Enabled: "+ currentTweaks.toString()+ "\n"
					+ "Saved: " + savedTweaks.toString() + "\n\n"
					+ "Continue anyway?");
			if(!cont)
				FMLCommonHandler.instance().exitJava(1, false);
			else
				data.markDirty();
		}
	}

}
