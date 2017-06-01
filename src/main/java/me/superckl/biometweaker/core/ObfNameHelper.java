package me.superckl.biometweaker.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public final class ObfNameHelper {

	@RequiredArgsConstructor
	public static enum Classes{

		BIOME("net.minecraft.world.biome.Biome"),
		BLOCKOLDLEAD("net.minecraft.block.BlockOldLeaf"),
		IBLOCKSTATE("net.minecraft.block.state.IBlockState"),
		BLOCK("net.minecraft.block.Block"),
		CHUNKPRIMER("net.minecraft.world.chunk.ChunkPrimer"),
		BLOCKS("net.minecraft.init.Blocks");

		@Getter
		private final String name;

		public String getInternalName(){
			return ASMHelper.toInternalClassName(this.name);
		}
	}

	@RequiredArgsConstructor
	public enum Methods{

		GENBIOMETERRAIN(Classes.BIOME, "func_180628_b"),
		GENTERRAINBLOCKS(Classes.BIOME, "func_180622_a"),
		GETBIOMEGRASSCOLOR(Classes.BIOME, "func_180627_b"),
		GETBIOMEFOLIAGECOLOR(Classes.BIOME, "func_180625_c"),
		GETBLOCK(Classes.IBLOCKSTATE, "func_177230_c"),
		GETDEFAULTSTATE(Classes.BLOCK, "func_176223_P"),
		SETBLOCKSTATE(Classes.CHUNKPRIMER, "func_177855_a"),
		GETSKYCOLORBYTEMP(Classes.BIOME, "func_76731_a");

		private final Classes clazz;
		private final String name;

		public String getName(final String descriptor){
			final String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(this.clazz.getInternalName());
			return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, this.name, descriptor);
		}

	}

	@RequiredArgsConstructor
	public enum Fields {

		STONE(Classes.BLOCKS, "field_150348_b"),
		BIOMEGENBASE_STONE(Classes.BIOME, "field_185365_a"),
		WATER(Classes.BLOCKS, "field_150355_j"),
		GRAVEL(Classes.BLOCKS, "field_150351_n"),
		BIOMEGENBASE_GRAVEL(Classes.BIOME, "field_185368_d"),
		TOPBLOCK(Classes.BIOME, "field_76752_A"),
		FILLERBLOCK(Classes.BIOME, "field_76753_B"),
		BIOMENAME(Classes.BIOME, "field_76791_y"),
		BASEHEIGHT(Classes.BIOME, "field_76748_D"),
		HEIGHTVARIATION(Classes.BIOME, "field_76749_E"),
		TEMPERATURE(Classes.BIOME, "field_76750_F"),
		RAINFALL(Classes.BIOME, "field_76751_G"),
		WATERCOLOR(Classes.BIOME, "field_76759_H"),
		ENABLESNOW(Classes.BIOME, "field_76766_R"),
		ENABLERAIN(Classes.BIOME, "field_76765_S");

		private final Classes clazz;
		private final String name;

		public String getName(){
			return ObfuscationReflectionHelper.remapFieldNames(this.clazz.getName(), this.name)[0];
		}

	}

	@RequiredArgsConstructor
	public enum Descriptors{

		GENBIOMETERRAIN("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V"),
		GENTERRAINBLOCKS("(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/chunk/ChunkPrimer;IID)V");

		@Getter
		private final String descriptor;

	}

}
