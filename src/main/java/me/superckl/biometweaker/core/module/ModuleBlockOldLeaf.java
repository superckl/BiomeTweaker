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

import me.superckl.biometweaker.core.ASMNameHelper;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public class ModuleBlockOldLeaf implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
		boolean fixed = false;
		for(final MethodNode mNode:cNode.methods)
			if(mNode.name.equals(ASMNameHelper.method_colorMultiplier.get())){
				final InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/world/biome/BiomeGenBase;", true));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/BiomeGenBase", "foliageColor", "I"));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				final LabelNode label = new LabelNode(new Label());
				list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/world/biome/BiomeGenBase;", true));
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
