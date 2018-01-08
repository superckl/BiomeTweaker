package me.superckl.biometweaker.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public static enum Classes{

		BIOMEHOOKS("me.superckl.biometweakercore.util.BiomeHooks"),
		BIOMEHELPER("me.superckl.biometweaker.util.BiomeHelper"),
		BIOME("net.minecraft.world.biome.Biome"),
		IBLOCKSTATE("net.minecraft.block.state.IBlockState"),
		BLOCK("net.minecraft.block.Block"),
		CHUNKPRIMER("net.minecraft.world.chunk.ChunkPrimer"),
		BLOCKS("net.minecraft.init.Blocks"),
		BIOMEFOREST("net.minecraft.world.biome.BiomeForest"),
		BIOMEJUNGLE("net.minecraft.world.biome.BiomeJungle"),
		BIOMESNOW("net.minecraft.world.biome.BiomeSnow"),
		BIOMETAIGA("net.minecraft.world.biome.BiomeTaiga"),
		BIOMEPLAINS("net.minecraft.world.biome.BiomePlains"),
		BIOMEMESA("net.minecraft.world.biome.BiomeMesa"),
		BIOMEHILLS("net.minecraft.world.biome.BiomeHills");

		@Getter
		private final String name;

		public String getInternalName(){
			return this.name.replace('.', '/');
		}
	}

	@RequiredArgsConstructor
	public enum Fields {

		ACTUALFILLERBLOCKS(Classes.BIOME, "actualFillerBlocks", "[Lnet/minecraft/block/state/IBlockState;"),
		STONE(Classes.BLOCKS, "field_150348_b", "Lnet/minecraft/block/Block;"),
		BIOMEGENBASE_STONE(Classes.BIOME, "field_185365_a", "Lnet/minecraft/block/state/IBlockState;"),
		WATER(Classes.BLOCKS, "field_150355_j", "Lnet/minecraft/block/Block;"),
		GRAVEL(Classes.BLOCKS, "field_150351_n", "Lnet/minecraft/block/Block;"),
		BIOMEGENBASE_GRAVEL(Classes.BIOME, "field_185368_d", "Lnet/minecraft/block/state/IBlockState;"),
		TOPBLOCK(Classes.BIOME, "field_76752_A", "Lnet/minecraft/block/state/IBlockState;"),
		FILLERBLOCK(Classes.BIOME, "field_76753_B", "Lnet/minecraft/block/state/IBlockState;"),
		OCEANTOPBLOCK(Classes.BIOME, "oceanTopBlock", "Lnet/minecraft/block/state/IBlockState;"),
		OCEANFILLERBLOCK(Classes.BIOME, "oceanFillerBlock", "Lnet/minecraft/block/state/IBlockState;"),
		GRASSCOLOR(Classes.BIOME, "grassColor", "I"),
		FOLIAGECOLOR(Classes.BIOME, "foliageColor", "I"),
		SKYCOLOR(Classes.BIOME, "skyColor", "I"),
		BIOMENAME(Classes.BIOME, "field_76791_y", "Ljava/lang/String;"),
		BASEHEIGHT(Classes.BIOME, "field_76748_D", "F"),
		HEIGHTVARIATION(Classes.BIOME, "field_76749_E", "F"),
		TEMPERATURE(Classes.BIOME, "field_76750_F", "F"),
		RAINFALL(Classes.BIOME, "field_76751_G", "F"),
		WATERCOLOR(Classes.BIOME, "field_76759_H", "I"),
		ENABLESNOW(Classes.BIOME, "field_76766_R", "Z"),
		ENABLERAIN(Classes.BIOME, "field_76765_S", "Z"),
		BIOMEFOREST_TYPE(Classes.BIOMEFOREST, "field_150632_aF", "Lnet/minecraft/world/biome/BiomeForest/Type;"),
		BIOMETAIGA_TYPE(Classes.BIOMETAIGA, "field_150644_aH", "Lnet/minecraft/world/biome/BiomeTaiga/Type;"),
		BIOMEHILLS_TYPE(Classes.BIOMEHILLS, "field_150638_aH", "Lnet/minecraft/world/biome/BiomeHills/Type;"),
		SUPERICY(Classes.BIOMESNOW, "field_150615_aC", "Z"),
		SUNFLOWERS(Classes.BIOMEPLAINS, "field_150628_aC", "Z"),
		BRYCEPILLARS(Classes.BIOMEMESA, "field_150626_aH", "Z"),
		HASFOREST(Classes.BIOMEMESA, "field_150620_aI", "Z"),
		ISEDGE(Classes.BIOMEJUNGLE, "field_150614_aC", "Z");

		private final Classes clazz;
		private final String name;
		@Getter
		private final String descriptor;

		public String getName(){
			return ObfuscationReflectionHelper.remapFieldNames(this.clazz.getName(), this.name)[0];
		}

	}

}
