package me.superckl.biometweaker.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.superckl.biometweaker.common.world.gen.BlockReplacer;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator{


	public NoiseBasedChunkGeneratorMixin(final Registry<StructureSet> pStructureSets,
			final Optional<HolderSet<StructureSet>> pStructureOverrides, final BiomeSource pBiomeSource) {
		super(pStructureSets, pStructureOverrides, pBiomeSource);
	}

	@Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
			at = @At(value = "HEAD", id = "PRE"))
	public void preSurface(final WorldGenRegion pLevel, final StructureManager pStructureFeatureManager, final RandomState random, final ChunkAccess pChunk, final CallbackInfo ci) {
		BlockReplacer.runReplacement(PlacementStage.PRE_SURFACE, pLevel, pChunk);
	}

	@Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
			at = @At(value = "TAIL", id = "POST"))
	public void postSurface(final WorldGenRegion pLevel, final StructureManager pStructureFeatureManager, final RandomState random, final ChunkAccess pChunk, final CallbackInfo ci) {
		BlockReplacer.runReplacement(PlacementStage.POST_SURFACE, pLevel, pChunk);
	}

	@Inject(method = "applyCarvers(Lnet/minecraft/server/level/WorldGenRegion;JLnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;)V",
			at = @At(value = "HEAD", id = "PRE"))
	public void preCarvers(final WorldGenRegion pLevel, final long pSeed, final RandomState random, final BiomeManager pBiomeManager, final StructureManager pStructureFeatureManager, final ChunkAccess pChunk, final GenerationStep.Carving pStep, final CallbackInfo ci) {
		final PlacementStage stage = pStep == Carving.AIR ? PlacementStage.PRE_CARVERS : PlacementStage.PRE_LIQUID_CARVERS;
		BlockReplacer.runReplacement(stage, pLevel, pChunk);
	}

	@Inject(method = "applyCarvers(Lnet/minecraft/server/level/WorldGenRegion;JLnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;)V",
			at = @At(value = "TAIL", id = "POST"))
	public void postCarvers(final WorldGenRegion pLevel, final long pSeed, final RandomState random, final BiomeManager pBiomeManager, final StructureManager pStructureFeatureManager, final ChunkAccess pChunk, final GenerationStep.Carving pStep, final CallbackInfo ci) {
		final PlacementStage stage = pStep == Carving.AIR ? PlacementStage.POST_CARVERS : PlacementStage.POST_LIQUID_CARVERS;
		BlockReplacer.runReplacement(stage, pLevel, pChunk);
	}

}
