package me.superckl.biometweaker.core.module;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
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
import me.superckl.biometweaker.core.ASMNameHelper;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;

public class ModuleBlockOldLeaf implements IClassTransformerModule{

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
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
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {ASMNameHelper.class_blockOldLeaf.get()};
	}

	@Override
	public String getModuleName() {
		return "moduleTransformBlockOldLeaf";
	}

	@Override
	public boolean isRequired() {
		return false;
	}

}
