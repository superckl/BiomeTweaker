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
		int index = -1;
		final ClassReader reader = new ClassReader(basicClass);
		final ClassNode cNode = new ClassNode();
		reader.accept(cNode, 0);
		if((index = CollectionHelper.find(name, BiomeTweakerASMTransformer.class_biomeGenBase)) != -1){
			ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
			cNode.visitField(Opcodes.ACC_PUBLIC, "actualFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlock' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "liquidFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'liquidFillerBlock' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "grassColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'grassColor' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "foliageColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'foliageColor' field into "+transformedName);
			cNode.visitField(Opcodes.ACC_PUBLIC, "waterColor", "I", "I", -1);
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'waterColor' field into "+transformedName);
			int fixed = 5;
			for(final MethodNode node:cNode.methods)
				if((CollectionHelper.find(node.name, BiomeTweakerASMTransformer.method_genBiomeTerrain) != -1) && (CollectionHelper.find(node.desc, BiomeTweakerASMTransformer.desc_genBiomeTerrain) != -1)){
					for(AbstractInsnNode aNode:node.instructions.toArray())
						if(aNode instanceof FieldInsnNode){
							final FieldInsnNode vNode = (FieldInsnNode) aNode;
							if(CollectionHelper.find(vNode.name, BiomeTweakerASMTransformer.field_stone) != -1){
								aNode = vNode.getNext();
								if((aNode instanceof VarInsnNode) && (((VarInsnNode)aNode).var == 12)){
									final InsnList list = this.createGenBaseBlockFieldAccess("actualFillerBlock");
									node.instructions.insertBefore(vNode, list);
									node.instructions.remove(vNode);
									fixed++;
									ModBiomeTweakerCore.logger.debug("Successfully redirected 'block1' to 'actualFillerBlock'");
								}
							}else if(CollectionHelper.find(vNode.name, BiomeTweakerASMTransformer.field_water) != -1){
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
								if((aNode instanceof FieldInsnNode) && (CollectionHelper.find(((FieldInsnNode)aNode).name, BiomeTweakerASMTransformer.field_stone) != -1)){
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
					list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", BiomeTweakerASMTransformer.field_stone[index], "Lnet/minecraft/block/Block;"));
					list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/world/biome/BiomeGenBase", "actualFillerBlock", "Lnet/minecraft/block/Block;"));
					node.instructions.insert(list);
					ModBiomeTweakerCore.logger.debug("Successfully inserted Stone into 'actualFillerBlock'");
					fixed++;
					list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 0));
					list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Blocks", BiomeTweakerASMTransformer.field_water[index], "Lnet/minecraft/block/Block;"));
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
		}else if(!Config.INSTANCE.isLightASM() && ((index = CollectionHelper.find(name, BiomeTweakerASMTransformer.class_blockOldLeaf)) != -1)){
			ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
			boolean fixed = false;
			for(final MethodNode mNode:cNode.methods)
				if(CollectionHelper.find(mNode.name, BiomeTweakerASMTransformer.method_colorMultiplier) != -1){
					final InsnList list = new InsnList();
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new VarInsnNode(Opcodes.ILOAD, 2));
					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
					list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", BiomeTweakerASMTransformer.method_getBiomeGenForCoords[index], "(II)Lnet/minecraft/world/biome/BiomeGenBase;", true));
					list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "foliageColor", "I"));
					list.add(new InsnNode(Opcodes.ICONST_M1));
					final LabelNode label = new LabelNode(new Label());
					list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
					list.add(new VarInsnNode(Opcodes.ALOAD, 1));
					list.add(new VarInsnNode(Opcodes.ILOAD, 2));
					list.add(new VarInsnNode(Opcodes.ILOAD, 4));
					list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", BiomeTweakerASMTransformer.method_getBiomeGenForCoords[index], "(II)Lnet/minecraft/world/biome/BiomeGenBase;", true));
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
				if(((index = CollectionHelper.find(mNode.name, BiomeTweakerASMTransformer.method_getBiomeGrassColor)) != -1) && mNode.desc.equals("(III)I")){
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
				}else if(((index = CollectionHelper.find(mNode.name, BiomeTweakerASMTransformer.method_getBiomeFoliageColor)) != -1) && mNode.desc.equals("(III)I")){
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
				}
			//TODO force water color
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

	public static final String[] class_biomeGenBase = {"net.minecraft.world.biome.BiomeGenBase", "ahu"};
	public static final String[] class_blockOldLeaf = {"net.minecraft.block.BlockOldLeaf", "aml"};

	public static final String[] method_genBiomeTerrain = {"genBiomeTerrain", "func_150560_b"};
	public static final String[] method_getBiomeGrassColor = {"getBiomeGrassColor", "func_150558_b"};
	public static final String[] method_getBiomeFoliageColor = {"getBiomeFoliageColor", "func_150571_c"};
	public static final String[] method_colorMultiplier = {"colorMultiplier", "func_149720_d"};
	public static final String[] method_getBiomeGenForCoords = {"getBiomeGenForCoords","func_72807_a"};

	public static final String[] field_stone = {"stone", "field_150348_b"};
	public static final String[] field_water = {"water", "field_150355_j"};

	public static final String[] desc_genBiomeTerrain = {"(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V", "(Lahb;Ljava/util/Random;[Laji;[BIID)V"};


}
