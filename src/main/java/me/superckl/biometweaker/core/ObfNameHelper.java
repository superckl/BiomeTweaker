package me.superckl.biometweaker.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public static enum Classes{

		biome("net.minecraft.world.biome.Biome"),
		blockOldLead("net.minecraft.block.BlockOldLeaf"),
		iBlockState("net.minecraft.block.state.IBlockState"),
		block("net.minecraft.block.Block"),
		chunkPrimer("net.minecraft.world.chunk.ChunkPrimer"),
		blocks("net.minecraft.init.Blocks");

		@Getter
		private final String name;

		public String getInternalName(){
			return ASMHelper.toInternalClassName(this.name);
		}
	}

	@RequiredArgsConstructor
	public enum Methods{

		genBiomeTerrain(Classes.biome, "func_180628_b"),
		genTerrainBlocks(Classes.biome, "func_180622_a"),
		getBiomeGrassColor(Classes.biome, "func_180627_b"),
		getBiomeFoliageColor(Classes.biome, "func_180625_c"),
		getBlock(Classes.iBlockState, "func_177230_c"),
		getDefaultState(Classes.block, "func_176223_P"),
		setBlockState(Classes.chunkPrimer, "func_177855_a"),
		getSkyColorByTemp(Classes.biome, "func_76731_a");

		private final Classes clazz;
		private final String name;

		public String getName(final String descriptor){
			final String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(this.clazz.getInternalName());
			return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, this.name, descriptor);
		}

	}

	@RequiredArgsConstructor
	public enum Fields {

		stone(Classes.blocks, "field_150348_b"),
		BiomeGenBase_stone(Classes.biome, "field_185365_a"),
		water(Classes.blocks, "field_150355_j"),
		gravel(Classes.blocks, "field_150351_n"),
		BiomeGenBase_gravel(Classes.biome, "field_185368_d"),
		topBlock(Classes.biome, "field_76752_A"),
		fillerBlock(Classes.biome, "field_76753_B"),
		biomeName(Classes.biome, "field_76791_y"),
		baseHeight(Classes.biome, "field_76748_D"),
		heightVariation(Classes.biome, "field_76749_E"),
		temperature(Classes.biome, "field_76750_F"),
		rainfall(Classes.biome, "field_76751_G"),
		waterColor(Classes.biome, "field_76759_H"),
		enableSnow(Classes.biome, "field_76766_R"),
		enableRain(Classes.biome, "field_76765_S");

		private final Classes clazz;
		private final String name;

		public String getName(){
			return ObfuscationReflectionHelper.remapFieldNames(this.clazz.getName(), this.name)[0];
		}

	}

	@RequiredArgsConstructor
	public enum Descriptors{

		genBiomeTerrain("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V"),
		genTerrainBlocks("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V");

		@Getter
		private final String descriptor;

	}

}
