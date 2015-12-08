package me.superckl.biometweaker.core.module;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.superckl.biometweaker.core.ASMNameHelper;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public class ModuleBiomeGenBase implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
		cNode.visitField(Opcodes.ACC_PUBLIC, "actualFillerBlocks", "[Lnet/minecraft/block/Block;", "[Lnet/minecraft/block/Block;", null);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlocks' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "oceanBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'fillerBlockMeta' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "grassColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'grassColor' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "foliageColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'foliageColor' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "waterColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'waterColor' field into "+transformedName);
		int fixed = 5;
		for(final MethodNode node:cNode.methods)
			if(node.name.equals(ASMNameHelper.method_genBiomeTerrain.get()) && node.desc.equals(ASMNameHelper.desc_genBiomeTerrain.get())){
				InsnList toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 17));
				toFind.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "getBlock", "()Lnet/minecraft/block/Block;", true));
				toFind.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_stone.get(), "Lnet/minecraft/block/Block;"));
				final AbstractInsnNode found = ASMHelper.find(node.instructions, toFind);
				if(found != null){
					final AbstractInsnNode someNode = found.getNext().getNext().getNext();
					if((someNode instanceof JumpInsnNode) && (someNode.getOpcode() == Opcodes.IF_ACMPNE)){
						((JumpInsnNode)someNode).setOpcode(Opcodes.IFEQ);
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlocks", "[Lnet/minecraft/block/Block;"));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 17));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "getBlock", "()Lnet/minecraft/block/Block;", true));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/BiomeHooks", "contains", "([Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;)Z", false));
						if(ASMHelper.findAndReplace(node.instructions, toFind, toInsert) != null){
							ModBiomeTweakerCore.logger.debug("Successfully redirected 'Stone' check to 'contains' method.");
							fixed++;
						}
					}
				}

				toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 3));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 14));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 16));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 13));
				toFind.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_gravel.get(), "Lnet/minecraft/block/Block;"));
				toFind.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/block/Block", "getDefaultState", "()Lnet/minecraft/block/state/IBlockState;", false));
				toFind.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/chunk/ChunkPrimer", "setBlockState", "(IIILnet/minecraft/block/state/IBlockState;)V", false));
				final AbstractInsnNode aNode = ASMHelper.find(node.instructions, toFind);
				if(aNode != null){
					final AbstractInsnNode aaNode = ASMHelper.findNextInstructionWithOpcode(aNode, Opcodes.GETSTATIC);
					if(aaNode != null){
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "oceanBlock", "Lnet/minecraft/block/Block;"));
						node.instructions.insert(aaNode, toInsert);
						node.instructions.remove(aaNode);
						ModBiomeTweakerCore.logger.debug("Successfully inserted 'oceanBlock' instructions.");
						fixed++;
					}
				}

			}else if(node.name.equals("<init>") && node.desc.equals("(IZ)V")){
				InsnList list = new InsnList();
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_1));
				list.add(new TypeInsnNode(Opcodes.ANEWARRAY, "net/minecraft/block/Block"));
				list.add(new InsnNode(Opcodes.DUP));
				list.add(new InsnNode(Opcodes.ICONST_0));
				list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_stone.get(), "Lnet/minecraft/block/Block;"));
				list.add(new InsnNode(Opcodes.AASTORE));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlocks", "[Lnet/minecraft/block/Block;"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted empty array into 'actualFillerBlocks'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_gravel.get(), "Lnet/minecraft/block/Block;"));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "oceanBlock", "Lnet/minecraft/block/Block;"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted gravel into 'oceanBlock'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "grassColor", "I"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted -1 into 'grassColor'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "foliageColor", "I"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted -1 into 'foliageColor'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "waterColor", "I"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted -1 into 'waterColor'");
				fixed++;
			}
		if(fixed < 12)
			ModBiomeTweakerCore.logger.error("Failed to completely patch "+transformedName+"! Only "+fixed+" patches were processed. Ye who continue now abandon all hope.");
		else if(fixed > 12)
			ModBiomeTweakerCore.logger.warn("Sucessfully patched "+transformedName+", but "+fixed+" patches were applied when we were expecting 12"
					+ ". Is something else also patching this class?");
		else
			ModBiomeTweakerCore.logger.info("Sucessfully patched "+transformedName+"! "+fixed+" patches were applied.");
		return ASMHelper.writeClassToBytes(cNode, ClassWriter.COMPUTE_MAXS);
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {ASMNameHelper.class_biomeGenBase.get()};
	}

	@Override
	public String getModuleName() {
		return "moduleTransformBiomeGenBase";
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
