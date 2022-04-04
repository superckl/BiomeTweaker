package me.superckl.biometweaker.script.command.effects;

import java.util.Optional;

import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setAmbientAdditions")
public class ScriptCommandSetAdditions extends ScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation type;
	private final float tickChance;

	public ScriptCommandSetAdditions(final BiomePackage pack, final String rLoc, final float chance) {
		this.pack = pack;
		this.type = new ResourceLocation(rLoc);
		this.tickChance = chance;
	}

	@Override
	public void perform() throws Exception {
		if(!ForgeRegistries.SOUND_EVENTS.containsKey(this.type))
			throw new IllegalArgumentException("Unknown particle type "+this.type);
		final SoundEvent type = ForgeRegistries.SOUND_EVENTS.getValue(this.type);
		final AmbientAdditionsSettings settings = new AmbientAdditionsSettings(type, this.tickChance);
		this.pack.locIterator().forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getEffects().setAdditions(Optional.of(settings)));
	}

}
