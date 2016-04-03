package me.superckl.biometweaker.core.module;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ASMNameHelper;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

public class ModuleBiomeGenBaseSubclass implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
		final ClassReader reader = new ClassReader(basicClass);
		if(ASMHelper.doesClassExtend(reader, ObfHelper.isObfuscated() ? "aig":"net/minecraft/world/biome/BiomeGenBase") && !transformedName.equals("net.minecraft.world.biome.BiomeGenMutated")){
			final ClassNode cNode = new ClassNode();
			reader.accept(cNode, 0);
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
					ModBiomeTweakerCore.logger.debug("Found Biome subclass "+transformedName+" with overriden grass color method and no event call. Attempting to force modded color event call...");
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
					ModBiomeTweakerCore.logger.debug("Found Biome subclass "+transformedName+" with overriden foliage color method and no event call. Attempting to force modded color event call...");
					for(final AbstractInsnNode aINode:this.findReturnNodes(mNode.instructions)){
						final InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/util/BiomeHelper", "callFoliageColorEvent", "(ILnet/minecraft/world/biome/BiomeGenBase;)I", false));
						mNode.instructions.insertBefore(aINode, list);
					}
				}else if(mNode.name.equals("getWaterColorMultiplier") && mNode.desc.equals("()I")){
					ModBiomeTweakerCore.logger.debug("Found Biome subclass "+transformedName+" with overriden water color method. Attempting to force modded color event call...");
					for(final AbstractInsnNode aINode:this.findReturnNodes(mNode.instructions)){
						final InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "me/superckl/biometweaker/util/BiomeHelper", "callFoliageColorEvent", "(ILnet/minecraft/world/biome/BiomeGenBase;)I", false));
						mNode.instructions.insertBefore(aINode, list);
					}
				}else if(Config.INSTANCE.isRemoveLateAssignments() && mNode.name.equals(ASMNameHelper.method_genTerrainBlocks.get()) && mNode.desc.equals(ASMNameHelper.desc_genTerrainBlocks.get())){
					//TODO broken
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
			return ASMHelper.writeClassToBytes(cNode);
		}
		return basicClass;
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {"*"};
	}

	@Override
	public String getModuleName() {
		return "moduleTransformBiomeGenBaseSubclass";
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	private List<AbstractInsnNode> findReturnNodes(final InsnList instructions){
		final List<AbstractInsnNode> list = new ArrayList<AbstractInsnNode>();
		for(int i = instructions.size()-1; i >= 0; i--)
			if(instructions.get(i).getOpcode() == Opcodes.IRETURN)
				list.add(instructions.get(i));
		return list;
	}

}
