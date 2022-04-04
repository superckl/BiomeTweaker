package me.superckl.biometweaker.script.command.effects;

import java.util.Optional;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setBackgroundMusic")
public class ScriptCommandSetMusic extends ScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation type;
	private final int minDelay;
	private final int maxDelay;
	private final boolean replaceMusic;

	public ScriptCommandSetMusic(final BiomePackage pack, final String rLoc, final int minDelay, final int maxDelay, final boolean replaceMusic) {
		this.pack = pack;
		this.type = new ResourceLocation(rLoc);
		this.minDelay = minDelay;
		this.maxDelay = maxDelay;
		this.replaceMusic = replaceMusic;
	}

	@Override
	public void perform() throws Exception {
		if(!ForgeRegistries.SOUND_EVENTS.containsKey(this.type))
			throw new IllegalArgumentException("Unknown particle type "+this.type);
		final SoundEvent type = ForgeRegistries.SOUND_EVENTS.getValue(this.type);
		final Music music = new Music(type, this.minDelay, this.maxDelay, this.replaceMusic);
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getEffects().setBackgroundMusic(Optional.of(music)));
	}

}
