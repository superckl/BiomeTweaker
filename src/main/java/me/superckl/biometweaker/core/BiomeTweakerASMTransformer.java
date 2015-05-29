package me.superckl.biometweaker.core;

import java.util.Set;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.module.IClassTransformerModule;
import me.superckl.biometweaker.core.module.ModuleBiomeGenBase;
import me.superckl.biometweaker.core.module.ModuleBiomeGenBaseSubclass;
import me.superckl.biometweaker.core.module.ModuleBlockOldLeaf;
import me.superckl.biometweaker.util.CollectionHelper;
import net.minecraft.launchwrapper.IClassTransformer;

import com.google.common.collect.Sets;

public class BiomeTweakerASMTransformer implements IClassTransformer{

	private final Set<IClassTransformerModule> modules = Sets.newIdentityHashSet();
	
	public BiomeTweakerASMTransformer() {
		this.registerModule(new ModuleBiomeGenBase());
		this.registerModule(new ModuleBlockOldLeaf());
		this.registerModule(new ModuleBiomeGenBaseSubclass());
	}
	
	public void registerModule(IClassTransformerModule module){
		this.modules.add(module);
	}
	
	@Override
	public byte[] transform(final String name, final String transformedName, byte[] basicClass) {
		if((basicClass == null) || (CollectionHelper.find(transformedName, Config.INSTANCE.getAsmBlacklist()) != -1))
			return basicClass;
		boolean lightASM = Config.INSTANCE.isLightASM();
		for(IClassTransformerModule module:this.modules){
			if(lightASM && !module.isRequired())
				continue;
			for(String clazz:module.getClassesToTransform())
				if(clazz.equals("*") || clazz.equals(transformedName))
					try{
						byte[] newBytes = module.transform(name, transformedName, basicClass);
						basicClass = newBytes;
					}catch(Exception e){
						ModBiomeTweakerCore.logger.error("Caught an exception from module "+module.getModuleName());
						e.printStackTrace();
					}

		}
		return basicClass;
	}

}
