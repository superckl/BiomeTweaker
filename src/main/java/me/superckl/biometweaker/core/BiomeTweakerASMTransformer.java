package me.superckl.biometweaker.core;

import me.superckl.biometweaker.util.CollectionHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BiomeTweakerASMTransformer implements IClassTransformer{

	@Override
	public byte[] transform(final String name, final String transformedName,
			final byte[] basicClass) {
		int index = -1;
		if((index = CollectionHelper.find(name, BiomeTweakerASMTransformer.class_biomeGenBase)) != -1){
			final ClassReader reader = new ClassReader(basicClass);
			final ClassNode cNode = new ClassNode();
			reader.accept(cNode, 0);
			cNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "actualFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null));
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlock' field into "+name);
			cNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "liquidFillerBlock", "Lnet/minecraft/block/Block;", "Lnet/minecraft/block/Block;", null));
			ModBiomeTweakerCore.logger.debug("Successfully inserted 'liquidFillerBlock' field into "+name);
			int fixed = 2;
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
				}
			if(fixed < 8)
				ModBiomeTweakerCore.logger.error("Failed to completely patch "+name+"! Only "+fixed+" patches were processed. Ye who continue now abandon all hope.");
			else
				ModBiomeTweakerCore.logger.info("Sucessfully patched BiomeGenBase! "+fixed+" patches were applied.");
			final ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			cNode.accept(cWriter);
			return cWriter.toByteArray();
		}
		return basicClass;
	}

	private InsnList createGenBaseBlockFieldAccess(String fieldName){
		final InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", fieldName, "Lnet/minecraft/block/Block;"));
		return list;
	}
	
	public static final String[] class_biomeGenBase = {"net.minecraft.world.biome.BiomeGenBase", "ahu"};

	public static final String[] method_genBiomeTerrain = {"genBiomeTerrain", "func_150560_b"};

	public static final String[] field_stone = {"stone", "field_150348_b"};
	public static final String[] field_water = {"water", "field_150355_j"};

	public static final String[] desc_genBiomeTerrain = {"(Lnet/minecraft/world/World;Ljava/util/Random;[Lnet/minecraft/block/Block;[BIID)V", "(Lahb;Ljava/util/Random;[Laji;[BIID)V"};


}
