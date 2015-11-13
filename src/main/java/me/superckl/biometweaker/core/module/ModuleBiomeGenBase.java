package me.superckl.biometweaker.core.module;

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
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'fillerBlockMeta' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "grassColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'grassColor' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "foliageColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'foliageColor' field into "+transformedName);
		cNode.visitField(Opcodes.ACC_PUBLIC, "waterColor", "I", "I", -1);
		ModBiomeTweakerCore.logger.debug("Successfully inserted 'waterColor' field into "+transformedName);
		int fixed = 4;
		for(final MethodNode node:cNode.methods)
			if(node.name.equals(ASMNameHelper.method_genBiomeTerrain.get()) && node.desc.equals(ASMNameHelper.desc_genBiomeTerrain.get())){
				final InsnList toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 20));
				toFind.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_stone.get(), "Lnet/minecraft/block/Block;"));
				final AbstractInsnNode found = ASMHelper.find(node.instructions, toFind);
				if(found != null){
					final AbstractInsnNode someNode = found.getNext().getNext();
					if((someNode instanceof JumpInsnNode) && (someNode.getOpcode() == Opcodes.IF_ACMPNE)){
						((JumpInsnNode)someNode).setOpcode(Opcodes.IFEQ);
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlocks", "[Lnet/minecraft/block/Block;"));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 20));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/BiomeHooks", "contains", "([Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;)Z", false));
						//toInsert.add(new JumpInsnNode(Opcodes.IFEQ, ln));
						if(ASMHelper.findAndReplace(node.instructions, toFind, toInsert) != null){
							ModBiomeTweakerCore.logger.debug("Successfully redirected 'Stone' check to 'contains' method.");
							fixed++;
						}
					}
				}

				/*toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 3));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 19));
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 12));
				toFind.add(new InsnNode(Opcodes.AASTORE));
				AbstractInsnNode aNode = ASMHelper.find(node.instructions, toFind);
				if(aNode != null){
					AbstractInsnNode aaNode = ASMHelper.findNextInstructionWithOpcode(aNode, Opcodes.AASTORE);
					if(aaNode != null){
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 19));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "fillerBlockMeta", "B"));
						toInsert.add(new InsnNode(Opcodes.BASTORE));
						node.instructions.insert(aaNode, toInsert);
						ModBiomeTweakerCore.logger.debug("Successfully inserted 'fillerBlockMeta' instructions.");
						fixed++;
					}

					aNode = ASMHelper.find(aNode.getNext(), toFind);
					if(aNode != null){
						aaNode = ASMHelper.findNextInstructionWithOpcode(aNode, Opcodes.AASTORE);
						if(aaNode != null){
							final InsnList toInsert = new InsnList();
							toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
							toInsert.add(new VarInsnNode(Opcodes.ILOAD, 19));
							toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
							toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "fillerBlockMeta", "B"));
							toInsert.add(new InsnNode(Opcodes.BASTORE));
							node.instructions.insert(aaNode, toInsert);
							ModBiomeTweakerCore.logger.debug("Successfully inserted 'fillerBlockMeta' instructions.");
							fixed++;
						}
					}
				}*/

			}else if(node.name.equals("<init>") && node.desc.equals("(IZ)V")){
				InsnList list = new InsnList();
				/*list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_stone.get(), "Lnet/minecraft/block/Block;"));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlock", "Lnet/minecraft/block/Block;"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted Stone into 'actualFillerBlock'");
				fixed++;*/
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
				/*list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_water.get(), "Lnet/minecraft/block/Block;"));
				list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "liquidFillerBlock", "Lnet/minecraft/block/Block;"));
				node.instructions.insert(list);
				ModBiomeTweakerCore.logger.debug("Successfully inserted Water into 'liquidFillerBlock'");
				fixed++;*/
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
		if(fixed < 8)
			ModBiomeTweakerCore.logger.error("Failed to completely patch "+transformedName+"! Only "+fixed+" patches were processed. Ye who continue now abandon all hope.");
		else if(fixed > 8)
			ModBiomeTweakerCore.logger.warn("Sucessfully patched "+transformedName+", but "+fixed+" patches were applied when we were expecting 8"
					+ ". Is something else also patching this class?");
		else
			ModBiomeTweakerCore.logger.info("Sucessfully patched "+transformedName+"! "+fixed+" patches were applied.");
		return ASMHelper.writeClassToBytesNoDeobf(cNode);
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
