package me.superckl.biometweaker.core.module;

import me.superckl.biometweaker.core.ObfNameHelper;

public class ModuleBlockOldLeaf implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		/*final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		ModBiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
		boolean fixed = false;
		for(final MethodNode mNode:cNode.methods)
			if(mNode.name.equals(ASMNameHelper.method_colorMultiplier.get())){
				final InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/world/biome/Biome;", true));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/Biome", "foliageColor", "I"));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				final LabelNode label = new LabelNode(new Label());
				list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new VarInsnNode(Opcodes.ALOAD, 2));
				list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ASMNameHelper.method_getBiomeGenForCoords.get(), "(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/world/biome/Biome;", true));
				list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/Biome", "foliageColor", "I"));
				list.add(new InsnNode(Opcodes.IRETURN));
				list.add(label);
				mNode.instructions.insert(list);
				ModBiomeTweakerCore.logger.info("Successfully patched "+mNode.name+" in "+transformedName);
				fixed = true;
			}
		if(!fixed)
			ModBiomeTweakerCore.logger.error("Failed to patch "+transformedName+"!  If this is a server, you're fine. Otherwise ye who continue now abandon all hope.");
		return ASMHelper.writeClassToBytes(cNode);*/
		//TODO Apparently the override is gone?
		return bytes;
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {ObfNameHelper.Classes.blockOldLead.getName()};
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
