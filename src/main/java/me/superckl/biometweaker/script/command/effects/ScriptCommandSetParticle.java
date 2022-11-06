package me.superckl.biometweaker.script.command.effects;

import java.util.Optional;

import me.superckl.api.biometweaker.BiomeLookup;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraftforge.registries.ForgeRegistries;

@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "setAmbientParticle")
public class ScriptCommandSetParticle extends StagedScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation particleType;
	private final float probability;

	public ScriptCommandSetParticle(final BiomePackage pack, final ResourceLocation rLoc, final float probability) {
		this.pack = pack;
		this.particleType = rLoc;
		this.probability = probability;
	}

	@Override
	public void perform() throws Exception {
		if(!ForgeRegistries.PARTICLE_TYPES.containsKey(this.particleType))
			throw new IllegalArgumentException("Unknown particle type "+this.particleType);
		final ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(this.particleType);
		if(!(type instanceof final ParticleOptions opts))
			throw new IllegalArgumentException(String.format("Particle type %s requires additional options. This is currently not supported!", this.particleType));
		final AmbientParticleSettings settings = new AmbientParticleSettings(opts, this.probability);
		this.pack.locIterator(BiomeLookup.fromForge()).forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).getEffects().setParticle(Optional.of(settings)));
	}

	@Override
	public StageRequirement requiredStage() {
		return StageRequirement.LATE;
	}

}
