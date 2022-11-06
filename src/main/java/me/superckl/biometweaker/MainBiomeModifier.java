package me.superckl.biometweaker;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record MainBiomeModifier() implements BiomeModifier {

	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final Builder builder) {
		BiomeModificationManager.forBiomeOpt(biome.unwrapKey().get()).ifPresent(mod -> {
			switch(phase) {
			case ADD:
				if(mod.hasSpawn())
					mod.getSpawn().doAdd(builder.getMobSpawnSettings());
				if(mod.hasGeneration())
					mod.getGeneration().doAdd(builder.getGenerationSettings());
				break;
			case MODIFY:
				if(mod.hasClimate())
					mod.getClimate().modify(builder.getClimateSettings());
				if(mod.hasEffects())
					mod.getEffects().modify(builder.getSpecialEffects());
				if(mod.hasSpawn())
					mod.getSpawn().modify(builder.getMobSpawnSettings());
				break;
			case REMOVE:
				if(mod.hasSpawn())
					mod.getSpawn().doRemove(builder.getMobSpawnSettings());
				if(mod.hasGeneration())
					mod.getGeneration().doRemove(builder.getGenerationSettings());
				break;
			default:
				break;
			}
		});
	}

	@Override
	public Codec<MainBiomeModifier> codec() {
		return BiomeTweaker.MAIN_MODIFIER_CODEC.get();
	}

}
