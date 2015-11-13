package me.superckl.biometweaker.core;

import lombok.RequiredArgsConstructor;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

@RequiredArgsConstructor
public enum ASMNameHelper {

	class_biomeGenBase("net.minecraft.world.biome.BiomeGenBase", "net.minecraft.world.biome.BiomeGenBase"),
	class_blockOldLeaf("net.minecraft.block.BlockOldLeaf", "net.minecraft.block.BlockOldLeaf"),

	method_genBiomeTerrain("func_180628_b", "func_180628_b"),
	method_genTerrainBlocks("genTerrainBlocks", "func_180622_a"),
	method_getBiomeGrassColor("func_180627_b", "func_180627_b"),
	method_getBiomeFoliageColor("func_180625_c", "func_180625_c"),
	method_colorMultiplier("colorMultiplier", "func_180662_a"),
	method_getBiomeGenForCoords("getBiomeGenForCoords","func_180494_b"),

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
