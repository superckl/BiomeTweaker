package me.superckl.biometweaker.core.module;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.core.ObfNameHelper.Classes;
import me.superckl.biometweaker.core.ObfNameHelper.Fields;
import me.superckl.biometweaker.core.ObfNameHelper.Methods;
import squeek.asmhelper.me.superckl.biometweaker.ASMHelper;

public class ModuleBiomeGenBase implements IClassTransformerModule{

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		final ClassNode cNode = ASMHelper.readClassFromBytes(bytes);
		BiomeTweakerCore.logger.info("Attempting to patch class "+transformedName+"...");
		cNode.fields.add(Fields.ACTUALFILLERBLOCKS.toNode(Opcodes.ACC_PUBLIC, null));
		BiomeTweakerCore.logger.debug("Successfully inserted 'actualFillerBlocks' field into "+transformedName);
		cNode.fields.add(Fields.OCEANTOPBLOCK.toNode(Opcodes.ACC_PUBLIC, null));
		BiomeTweakerCore.logger.debug("Successfully inserted 'oceanTopBlock' field into "+transformedName);
		cNode.fields.add(Fields.OCEANFILLERBLOCK.toNode(Opcodes.ACC_PUBLIC, null));
		BiomeTweakerCore.logger.debug("Successfully inserted 'oceanFillerBlock' field into "+transformedName);
		cNode.fields.add(Fields.GRASSCOLOR.toNode(Opcodes.ACC_PUBLIC, -1));
		BiomeTweakerCore.logger.debug("Successfully inserted 'grassColor' field into "+transformedName);
		cNode.fields.add(Fields.FOLIAGECOLOR.toNode(Opcodes.ACC_PUBLIC, -1));
		BiomeTweakerCore.logger.debug("Successfully inserted 'foliageColor' field into "+transformedName);
		cNode.fields.add(Fields.SKYCOLOR.toNode(Opcodes.ACC_PUBLIC, -1));
		BiomeTweakerCore.logger.debug("Successfully inserted 'skyColor' field into "+transformedName);
		int fixed = 6;
		boolean sky = false;
		for(final MethodNode node:cNode.methods)
			if(Methods.GENBIOMETERRAIN.matches(node)){
				InsnList toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 17));
				toFind.add(Methods.GETBLOCK.toInsnNode(Opcodes.INVOKEINTERFACE));
				toFind.add(Fields.STONE.toInsnNode(Opcodes.GETSTATIC));
				final AbstractInsnNode found = ASMHelper.find(node.instructions, toFind);
				if(found != null){
					final AbstractInsnNode someNode = found.getNext().getNext().getNext();
					if((someNode instanceof JumpInsnNode) && (someNode.getOpcode() == Opcodes.IF_ACMPNE)){
						((JumpInsnNode)someNode).setOpcode(Opcodes.IFEQ);
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(Fields.ACTUALFILLERBLOCKS.toInsnNode(Opcodes.GETFIELD));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 17));
						toInsert.add(Methods.CONTAINS.toInsnNode(Opcodes.INVOKESTATIC));
						if(ASMHelper.findAndReplace(node.instructions, toFind, toInsert) != null){
							BiomeTweakerCore.logger.debug("Successfully redirected 'Stone' check to 'contains' method.");
							fixed++;
						}
					}
				}

				toFind = new InsnList();
				toFind.add(new VarInsnNode(Opcodes.ALOAD, 3));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 14));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 16));
				toFind.add(new VarInsnNode(Opcodes.ILOAD, 13));
				toFind.add(Fields.BIOMEGENBASE_GRAVEL.toInsnNode(Opcodes.GETSTATIC));
				toFind.add(Methods.SETBLOCKSTATE.toInsnNode(Opcodes.INVOKEVIRTUAL));
				AbstractInsnNode aNode = ASMHelper.find(node.instructions, toFind);
				if(aNode != null){
					final AbstractInsnNode aaNode = ASMHelper.findNextInstructionWithOpcode(aNode, Opcodes.GETSTATIC);
					if(aaNode != null){
						final InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(Fields.OCEANTOPBLOCK.toInsnNode(Opcodes.GETFIELD));
						node.instructions.insert(aaNode, toInsert);
						node.instructions.remove(aaNode);
						BiomeTweakerCore.logger.debug("Successfully inserted 'oceanTopBlock' instructions.");
						fixed++;

						aNode = ASMHelper.findPreviousInstructionWithOpcode(aNode, Opcodes.GETSTATIC);
						toFind = new InsnList();
						toFind.add(Fields.BIOMEGENBASE_STONE.toInsnNode(Opcodes.GETSTATIC));
						toFind.add(new VarInsnNode(Opcodes.ASTORE, 10));
						aNode = ASMHelper.find(aNode, toFind);
						if(aNode != null){
							final InsnList toInsert1 = new InsnList();
							toInsert1.add(new VarInsnNode(Opcodes.ALOAD, 0));
							toInsert1.add(Fields.OCEANFILLERBLOCK.toInsnNode(Opcodes.GETFIELD));
							node.instructions.insert(aNode, toInsert1);
							node.instructions.remove(aNode);
							BiomeTweakerCore.logger.debug("Successfully inserted 'oceanFillerBlock' instructions.");
							fixed++;
						}
					}
				}
			}else if(Methods.GETSKYCOLORBYTEMP.matches(node)){
				final InsnList list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(Fields.SKYCOLOR.toInsnNode(Opcodes.GETFIELD));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				final LabelNode label = new LabelNode();
				list.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, label));
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(Fields.SKYCOLOR.toInsnNode(Opcodes.GETFIELD));
				list.add(new InsnNode(Opcodes.IRETURN));
				list.add(label);
				node.instructions.insertBefore(node.instructions.getFirst(), list);
				BiomeTweakerCore.logger.debug("Successfully inserted sky color instructions.");
				sky = true;
			}else if(Methods.BIOME_CONSTRUCTOR.matches(node)){
				InsnList list = new InsnList();
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_1));
				list.add(new TypeInsnNode(Opcodes.ANEWARRAY, Classes.IBLOCKSTATE.getInternalName()));
				list.add(new InsnNode(Opcodes.DUP));
				list.add(new InsnNode(Opcodes.ICONST_0));
				list.add(Fields.STONE.toInsnNode(Opcodes.GETSTATIC));
				list.add(Methods.GETDEFAULTSTATE.toInsnNode(Opcodes.INVOKEVIRTUAL));
				list.add(new InsnNode(Opcodes.AASTORE));
				list.add(Fields.ACTUALFILLERBLOCKS.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted empty array into 'actualFillerBlocks'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(Fields.BIOMEGENBASE_STONE.toInsnNode(Opcodes.GETSTATIC));
				list.add(Fields.OCEANFILLERBLOCK.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted stone into 'oceanFillerBlock'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(Fields.BIOMEGENBASE_GRAVEL.toInsnNode(Opcodes.GETSTATIC));
				list.add(Fields.OCEANTOPBLOCK.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted gravel into 'oceanTopBlock'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(Fields.GRASSCOLOR.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted -1 into 'grassColor'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(Fields.FOLIAGECOLOR.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted -1 into 'foliageColor'");
				fixed++;
				list = new InsnList();
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new InsnNode(Opcodes.ICONST_M1));
				list.add(Fields.SKYCOLOR.toInsnNode(Opcodes.PUTFIELD));
				node.instructions.insert(list);
				BiomeTweakerCore.logger.debug("Successfully inserted -1 into 'skyColor'");
				fixed++;
			}
		if(!sky)
			BiomeTweakerCore.logger.warn("Failed to insert sky color instructions. If this is a server, don't worry. If if this a client, worry. A lot.");

		if(fixed < 15){
			BiomeTweakerCore.logger.error("Failed to completely patch "+transformedName+"! Only "+fixed+" patches were processed. Ye who continue now abandon all hope.");
			BiomeTweakerCore.logger.error("Seriously, this is really bad. Things are probably going to break.");
		}
		else if(fixed > 15)
			BiomeTweakerCore.logger.warn("Sucessfully patched "+transformedName+", but "+fixed+" patches were applied when we were expecting 15"
					+ ". Is something else also patching this class?");
		else{
			BiomeTweakerCore.logger.info("Sucessfully patched "+transformedName+"! "+fixed+" patches were applied.");
			BiomeTweakerCore.modifySuccess = true;
		}
		return ASMHelper.writeClassToBytes(cNode);
	}

	@Override
	public String[] getClassesToTransform() {
		return new String[] {Classes.BIOME.getName()};
	}

	@Override
	public String getModuleName() {
		return "moduleTransformBiome";
	}

	@Override
	public boolean isRequired() {
		return true;
	}

}
