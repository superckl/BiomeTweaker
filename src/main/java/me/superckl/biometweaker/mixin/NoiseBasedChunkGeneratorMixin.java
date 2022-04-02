package me.superckl.biometweaker.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.superckl.biometweaker.common.world.gen.BlockReplacer;
import me.superckl.biometweaker.common.world.gen.PlacementStage;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin extends ChunkGenerator{


	public NoiseBasedChunkGeneratorMixin(final Registry<StructureSet> pStructureSets,
			final Optional<HolderSet<StructureSet>> pStructureOverrides, final BiomeSource pBiomeSource) {
		super(pStructureSets, pStructureOverrides, pBiomeSource);
		// TODO Auto-generated constructor stub
	}

	@Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
			at = @At(value = "HEAD", id = "PRE"))
	public void preSurface(final WorldGenRegion pLevel, final StructureFeatureManager pStructureFeatureManager, final ChunkAccess pChunk, final CallbackInfo ci) {
		//LogHelper.info("PRE surface @"+pChunk.getPos());
		BlockReplacer.runReplacement(PlacementStage.PRE_SURFACE, pLevel, pChunk);
	}

	@Inject(method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
			at = @At(value = "TAIL", id = "POST"))
	public void postSurface(final WorldGenRegion pLevel, final StructureFeatureManager pStructureFeatureManager, final ChunkAccess pChunk, final CallbackInfo ci) {
		//LogHelper.info("POS surface @"+pChunk.getPos());
		BlockReplacer.runReplacement(PlacementStage.POST_SURFACE, pLevel, pChunk);
	}

	@Inject(method = "applyCarvers(Lnet/minecraft/server/level/WorldGenRegion;JLnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;)V",
			at = @At(value = "HEAD", id = "PRE"))
	public void preCarvers(final WorldGenRegion pLevel, final long pSeed, final BiomeManager pBiomeManager, final StructureFeatureManager pStructureFeatureManager, final ChunkAccess pChunk, final GenerationStep.Carving pStep, final CallbackInfo ci) {
		//LogHelper.info("PRE carvers @"+pChunk.getPos());
		BlockReplacer.runReplacement(PlacementStage.PRE_CARVERS, pLevel, pChunk);
	}

	@Inject(method = "applyCarvers(Lnet/minecraft/server/level/WorldGenRegion;JLnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;)V",
			at = @At(value = "TAIL", id = "POST"))
	public void postCarvers(final WorldGenRegion pLevel, final long pSeed, final BiomeManager pBiomeManager, final StructureFeatureManager pStructureFeatureManager, final ChunkAccess pChunk, final GenerationStep.Carving pStep, final CallbackInfo ci) {
		//LogHelper.info("POST carvers @"+pChunk.getPos());
		BlockReplacer.runReplacement(PlacementStage.POST_CARVERS, pLevel, pChunk);
	}

	@Inject(method = "doFill(Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;II)Lnet/minecraft/world/level/chunk/ChunkAccess;", at = @At(value = "TAIL", id = "POST"))
	private void fill(final Blender pBlender, final StructureFeatureManager pStructureFeatherManager, final ChunkAccess pChunk, final int pMinCellY, final int pCellCountY, final CallbackInfoReturnable<ChunkAccess> ci) {
		//LogHelper.info(ci.getId()+" @"+pChunk.getPos());
	}

}
