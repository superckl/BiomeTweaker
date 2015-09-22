package me.superckl.biometweaker.core.module;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.superckl.biometweaker.core.ASMNameHelper;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public class ModuleBiomeGenBase implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
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
		else if(fixed > 14)
			ModBiomeTweakerCore.logger.warn("Sucessfully patched "+transformedName+", but "+fixed+" patches were applied when we were expecting 14. Is something else also patching this class?");
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

	private InsnList createGenBaseBlockFieldAccess(final String fieldName){
		final InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", fieldName, "Lnet/minecraft/block/Block;"));
		return list;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
