package me.superckl.biometweaker.common.event;

import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.script.command.IScriptCommand;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@RequiredArgsConstructor
public abstract class BiomeTweakEvent extends Event{

	private final IScriptCommand command;
	private final BiomeGenBase biome;

	@Cancelable
	public static class Create extends BiomeTweakEvent{

		public Create(final IScriptCommand command, final BiomeGenBase biome) {
			super(command, biome);
		}

	}

	@Cancelable
	public static class Remove extends BiomeTweakEvent{

		private final BiomeEntry entry;

		public Remove(final IScriptCommand command, final BiomeGenBase biome, final BiomeEntry entry) {
			super(command, biome);
			this.entry = entry;
		}

	}

}
