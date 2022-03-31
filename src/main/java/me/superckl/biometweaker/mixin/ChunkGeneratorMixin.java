package me.superckl.biometweaker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.superckl.biometweaker.common.world.gen.BlockReplacer;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin implements BiomeManager.NoiseBiomeSource{

	@Inject(method = "applyBiomeDecoration(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureFeatureManager;)V",
			at = @At(value = "HEAD", id = "PRE"))
	public void preDecorate(final WorldGenLevel pLevel, final ChunkAccess pChunk, final StructureFeatureManager pStructureFeatureManager, final CallbackInfo ci) {
		BlockReplacer.runReplacement(PlacementStage.PRE_DECORATE, pLevel, pChunk);
	}

	@Inject(method = "applyBiomeDecoration(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureFeatureManager;)V",
			at = @At(value = "TAIL", id = "POST"))
	public void postDecorate(final WorldGenLevel pLevel, final ChunkAccess pChunk, final StructureFeatureManager pStructureFeatureManager, final CallbackInfo ci) {
		BlockReplacer.runReplacement(PlacementStage.POST_DECORATE, pLevel, pChunk);
	}

}
