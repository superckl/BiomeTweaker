package me.superckl.biometweaker.core;

import java.util.ArrayList;
import java.util.List;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.CollectionHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

public class BiomeTweakerASMTransformer implements IClassTransformer{

	@Override
	public byte[] transform(final String name, final String transformedName,
			final byte[] basicClass) {
		if((basicClass == null) || (CollectionHelper.find(transformedName, Config.INSTANCE.getAsmBlacklist()) != -1))
			return basicClass;
		final ClassReader reader = new ClassReader(basicClass);
		final ClassNode cNode = new ClassNode();
		reader.accept(cNode, 0);
		if(name.equals(ASMNameHelper.class_biomeGenBase.get())){
			ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
			cNode.visitField(Opcodes.ACC_PUBLIC, "actualFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlock' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "liquidFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'liquidFillerBlock' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "actualFillerBlockMeta", "I", "I", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlockMeta' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "liquidFillerBlockMeta", "I", "I", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'liquidFillerBlockMeta' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "grassColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'grassColor' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "foliageColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'foliageColor' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "waterColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'waterColor' field into "+transformedName);
			int fixed = 5;
			for(final MethodNode node:cNode.methods)
				if(node.name.equals(ASMNameHelper.method_genBiomeTerrain.get()) && node.desc.equals(ASMNameHelper.desc_genBiomeTerrain.get())){
					for(AbstractInsnNode aNode:node.instructions.toArray())
						if(aNode instanceof FieldInsnNode){
							final FieldInsnNode vNode = (FieldInsnNode) aNode;
							if(vNode.name.equals(ASMNameHelper.field_stone.get())){
								aNode = vNode.getNext();
								if((aNode instanceof VarInsnNode) && (((VarInsnNode)aNode).var == 12)){
									final InsnList list = this.createGenBaseBlockFieldAccess("actualFillerBlock");
									node.instructions.insertBefore(vNode, list);
									node.instructions.remove(vNode);
									fixed++;
									ModBiomeTweakerCore.logger.debug("Successfully redirected 'block1' to 'actualFillerBlock'");
								}
							}else if(vNode.name.equals(ASMNameHelper.field_water.get())){
								aNode = vNode.getNext();
								if((aNode instanceof VarInsnNode) && (((VarInsnNode)aNode).var == 10)){
									final InsnList list = this.createGenBaseBlockFieldAccess("liquidFillerBlock");
									node.instructions.insertBefore(vNode, list);
									node.instructions.remove(vNode);
									fixed++;
									ModBiomeTweakerCore.logger.debug("Successfully redirected 'block' to 'liquidFillerBlock'");
								}
							}
						}else if(aNode instanceof VarInsnNode){
							final VarInsnNode vNode = (VarInsnNode) aNode;
							if((vNode.var == 20) && (vNode.getOpcode() == Opcodes.ALOAD)){
								aNode = vNode.getNext();
								if((aNode instanceof FieldInsnNode) && ((FieldInsnNode)aNode).name.equals(ASMNameHelper.field_stone.get())){
									final InsnList list = this.createGenBaseBlockFieldAccess("actualFillerBlock");
									node.instructions.insertBefore(aNode, list);
									node.instructions.remove(aNode);
									fixed++;
									ModBiomeTweakerCore.logger.debug("Successfully redirected 'block2' to 'actualFillerBlock'");
								}
							}
						}
				}else if(node.name.equals("<init>") && node.desc.equals("(IZ)V")){
					InsnList list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_stone.get(), "Lnet/minecraft/block/Block;"));
					list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlock", "Lnet/minecraft/block/Block;"));
					node.instructions.insert(list);
					ModBiomeTweakerCore.logger.debug("Successfully inserted Stone into 'actualFillerBlock'");
					fixed++;
					list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", ASMNameHelper.field_water.get(), "Lnet/minecraft/block/Block;"));
					list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "liquidFillerBlock", "Lnet/minecraft/block/Block;"));
					node.instructions.insert(list);
					ModBiomeTweakerCore.logger.debug("Successfully inserted Water into 'liquidFillerBlock'");
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
			if(fixed < 14)
				ModBiomeTweakerCore.logger.error("Failed to completely patch "+transformedName+"! Only "+fixed+" patches were processed. Ye who continue now abandon all hope.");
			else
				ModBiomeTweakerCore.logger.info("Sucessfully patched "+transformedName+"! "+fixed+" patches were applied.");
			return ASMHelper.writeClassToBytesNoDeobf(cNode);
		}else if(!Config.INSTANCE.isLightASM() && name.equals(ASMNameHelper.class_blockOldLeaf.get())){
			ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
			boolean fixed = false;
			for(final MethodNode mNode:cNode.methods)
				if(mNode.name.equals(ASMNameHelper.method_colorMultiplier.get())){
					final InsnList list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new VarInsnNode(Opcodes.ILOAD, 2));
					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
					list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(II)Lnet/minecraft/world/biome/BiomeGenBase;", true));
					list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "foliageColor", "I"));
					list.add(new InsnNode(Opcodes.ICONST_M1));
					final LabelNode label = new LabelNode(new Label());
					list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new VarInsnNode(Opcodes.ILOAD, 2));
					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
					list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(II)Lnet/minecraft/world/biome/BiomeGenBase;", true));
					list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "foliageColor", "I"));
					list.add(new InsnNode(Opcodes.IRETURN));
					list.add(label);
					mNode.instructions.insert(list);
					ModBiomeTweakerCore.logger.info("Successfully patched "+mNode.name+" in "+transformedName);
					fixed = true;
				}
			if(!fixed)
				ModBiomeTweakerCore.logger.error("Failed to patch "+transformedName+"!  If this is a server, you're fine. Otherwise ye who continue now abandon all hope.");
			return ASMHelper.writeClassToBytesNoDeobf(cNode);
		}else if(!Config.INSTANCE.isLightASM() && ASMHelper.doesClassExtend(reader, ObfHelper.isObfuscated() ? "ahu":"net/minecraft/world/biome/BiomeGenBase") && !transformedName.equals("net.minecraft.world.biome.BiomeGenMutated")){
			for(final MethodNode mNode:cNode.methods)
				if(mNode.name.equals(ASMNameHelper.method_getBiomeGrassColor.get()) && mNode.desc.equals("(III)I")){
					boolean shouldCont = false;
					final AbstractInsnNode aNode = mNode.instructions.get(mNode.instructions.size()-2);
					if(aNode instanceof MethodInsnNode){
						final MethodInsnNode methNode = (MethodInsnNode) aNode;
						if(methNode.name.equals("getModdedBiomeGrassColor") && methNode.desc.equals("(I)I") && methNode.owner.equals("net/minecraft/world/biome/BiomeGenBase")){
							shouldCont = true;
							break;
						}
					}
					if(shouldCont)
						continue;
					ModBiomeTweakerCore.logger.info("Found Biome subclass "+transformedName+" with overriden grass color method and no event call. Attempting to force modded color event call...");
					for(final AbstractInsnNode aINode:this.findReturnNodes(mNode.instructions)){
						final InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/util/BiomeHelper", "callGrassColorEvent", "(ILnet/minecraft/world/biome/BiomeGenBase;)I", false));
						mNode.instructions.insertBefore(aINode, list);
					}
				}else if(mNode.name.equals(ASMNameHelper.method_getBiomeFoliageColor.get()) && mNode.desc.equals("(III)I")){
					boolean shouldCont = false;
					final AbstractInsnNode aNode = mNode.instructions.get(mNode.instructions.size()-2);
					if(aNode instanceof MethodInsnNode){
						final MethodInsnNode methNode = (MethodInsnNode) aNode;
						if(methNode.name.equals("getModdedBiomeFoliageColor") && methNode.desc.equals("(I)I") && methNode.owner.equals("net/minecraft/world/biome/BiomeGenBase")){
							shouldCont = true;
							break;
						}
					}
					if(shouldCont)
						continue;
					ModBiomeTweakerCore.logger.info("Found Biome subclass "+transformedName+" with overriden foliage color method and no event call. Attempting to force modded color event call...");
					for(final AbstractInsnNode aINode:this.findReturnNodes(mNode.instructions)){
						final InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/util/BiomeHelper", "callFoliageColorEvent", "(ILnet/minecraft/world/biome/BiomeGenBase;)I", false));
						mNode.instructions.insertBefore(aINode, list);
					}
				}else if(mNode.name.equals("getWaterColorMultiplier") && mNode.desc.equals("()I")){
					ModBiomeTweakerCore.logger.info("Found Biome subclass "+transformedName+" with overriden water color method. Attempting to force modded color event call...");
					for(final AbstractInsnNode aINode:this.findReturnNodes(mNode.instructions)){
						final InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/util/BiomeHelper", "callFoliageColorEvent", "(ILnet/minecraft/world/biome/BiomeGenBase;)I", false));
						mNode.instructions.insertBefore(aINode, list);
					}
				}else if(Config.INSTANCE.isRemoveLateAssignments() && mNode.name.equals(ASMNameHelper.method_genTerrainBlocks.get()) && mNode.desc.equals(ASMNameHelper.desc_genTerrainBlocks.get())){
					AbstractInsnNode node = ASMHelper.findFirstInstruction(mNode);
					AbstractInsnNode nextNode = node;
					int removed = 0;
					do{
						nextNode = node.getNext();
						if((node instanceof FieldInsnNode) == false)
							continue;
						final FieldInsnNode fNode = (FieldInsnNode) node;
						if((fNode.name.equals(ASMNameHelper.field_topBlock.get()) || fNode.name.equals(ASMNameHelper.field_fillerBlock.get())) && fNode.desc.equals("Lnet/minecraft/block/Block;")){
							AbstractInsnNode prevNode = ASMHelper.findPreviousInstruction(fNode);
							if((prevNode != null) && (prevNode instanceof FieldInsnNode) && (prevNode.getOpcode() == Opcodes.GETSTATIC)){
								final FieldInsnNode prevFNode = (FieldInsnNode) prevNode;
								if(prevFNode.owner.equals("net/minecraft/init/Blocks")){
									prevNode = ASMHelper.findPreviousInstruction(prevFNode);
									if((prevNode != null) && (prevNode instanceof VarInsnNode) && (prevNode.getOpcode() == Opcodes.ALOAD) && (((VarInsnNode)prevNode).var == 0)){
										ASMHelper.removeFromInsnListUntil(mNode.instructions, prevNode, nextNode);
										removed++;
									}
								}
							}
						}
					}while((node = ASMHelper.findNextInstructionWithOpcode(nextNode, Opcodes.PUTFIELD)) != null);
					if(removed > 0){
						ModBiomeTweakerCore.logger.warn("Found Biome subclass "+transformedName+" that was setting topBlock or fillerBlock in genTerrainBlocks! This is bad practice and breaks functionality in BiomeTweaker! "+removed+" items were removed. If this is not a vanilla biome, please let me (superckl) know.");
						ModBiomeTweakerCore.logger.info("If you feel the removal of this is causing issues with a modded biome, add this class to the ASM blacklist in the config and let me know. I apologize for the wall of text, but this is important.");
					}
				}
			return ASMHelper.writeClassToBytesNoDeobf(cNode);
		}
		return basicClass;
	}

	private InsnList createGenBaseBlockFieldAccess(final String fieldName){
		final InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", fieldName, "Lnet/minecraft/block/Block;"));
		return list;
	}

	private List<AbstractInsnNode> findReturnNodes(final InsnList instructions){
		final List<AbstractInsnNode> list = new ArrayList<AbstractInsnNode>();
		for(int i = instructions.size()-1; i >= 0; i--)
			if(instructions.get(i).getOpcode() == Opcodes.IRETURN)
				list.add(instructions.get(i));
		return list;
	}


}
