package me.superckl.biometweaker.core;

import lombok.RequiredArgsConstructor;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

@RequiredArgsConstructor
public enum ASMNameHelper {

	class_biomeGenBase("net.minecraft.world.biome.BiomeGenBase", "net.minecraft.world.biome.BiomeGenBase"),
	class_blockOldLeaf("net.minecraft.block.BlockOldLeaf", "net.minecraft.block.BlockOldLeaf"),

	method_genBiomeTerrain("genBiomeTerrain", "func_150560_b"),
	method_genTerrainBlocks("genTerrainBlocks", "func_150573_a"),
	method_getBiomeGrassColor("getBiomeGrassColor", "func_150558_b"),
	method_getBiomeFoliageColor("getBiomeFoliageColor", "func_150571_c"),
	method_colorMultiplier("colorMultiplier", "func_149720_d"),
	method_getBiomeGenForCoords("getBiomeGenForCoords","func_72807_a"),

	field_stone("stone", "field_150348_b"),
	field_water("water", "field_150355_j"),
	field_topBlock("topBlock", "field_76752_A"),
	field_fillerBlock("fillerBlock", "field_76753_B"),

	desc_genBiomeTerrain("(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V", "(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V"),
	desc_genTerrainBlocks("(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V", "(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V");

	private final String deobf;
	private final String obf;

	public String get(){
		return ObfHelper.isObfuscated() ? this.obf:this.deobf;
	}

}
