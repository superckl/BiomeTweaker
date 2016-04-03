package me.superckl.biometweaker.core;

import lombok.RequiredArgsConstructor;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

@RequiredArgsConstructor
public enum ASMNameHelper {

	class_biomeGenBase("net.minecraft.world.biome.BiomeGenBase", "net.minecraft.world.biome.BiomeGenBase"),
	class_blockOldLeaf("net.minecraft.block.BlockOldLeaf", "net.minecraft.block.BlockOldLeaf"),

	method_genBiomeTerrain("generateBiomeTerrain", "func_180628_b"),
	method_genTerrainBlocks("genTerrainBlocks", "func_180622_a"),
	method_getBiomeGrassColor("getGrassColorAtPos", "func_180627_b"),
	method_getBiomeFoliageColor("getFoliageColorAtPos", "func_180625_c"),
	//No longer there?
	//method_colorMultiplier("colorMultiplier", "func_180662_a"),
	method_getBiomeGenForCoords("getBiomeGenForCoords","func_180494_b"),
	method_getBlock("getBlock", "func_177230_c"),
	method_getDefaultState("getDefaultState", "func_176223_P"),
	method_setBlockState("setBlockState", "func_177230_c"),

	field_stone("stone", "field_150348_b"),
	field_STONE("STONE", "field_185365_a"),
	field_water("water", "field_150355_j"),
	field_gravel("gravel", "field_150351_n"),
	field_GRAVEL("GRAVEL", "field_185368_d"),
	field_topBlock("topBlock", "field_76752_A"),
	field_fillerBlock("fillerBlock", "field_76753_B"),

	desc_genBiomeTerrain("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V"),
	desc_genTerrainBlocks("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V", "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V");

	private final String deobf;
	private final String obf;

	public String get(){
		return ObfHelper.isObfuscated() ? this.obf:this.deobf;
	}

}
