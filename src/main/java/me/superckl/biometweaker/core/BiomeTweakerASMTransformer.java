package me.superckl.biometweaker.core;

import java.util.Set;

import com.google.common.collect.Sets;

import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.module.IClassTransformerModule;
import me.superckl.biometweaker.core.module.ModuleBiomeGenBase;
import me.superckl.biometweaker.core.module.ModuleBiomeGenBaseSubclass;
import net.minecraft.launchwrapper.IClassTransformer;

public class BiomeTweakerASMTransformer implements IClassTransformer{

	private final Set<IClassTransformerModule> modules = Sets.newIdentityHashSet();

	public BiomeTweakerASMTransformer() {
		this.registerModule(new ModuleBiomeGenBase());
		this.registerModule(new ModuleBiomeGenBaseSubclass());
	}

	public void registerModule(final IClassTransformerModule module){
		this.modules.add(module);
	}

	@Override
	public byte[] transform(final String name, final String transformedName, byte[] basicClass) {
		if((basicClass == null) || (CollectionHelper.find(transformedName, Config.INSTANCE.getAsmBlacklist()) != -1))
			return basicClass;
		final boolean lightASM = Config.INSTANCE.isLightASM();
		for(final IClassTransformerModule module:this.modules){
			if(lightASM && !module.isRequired())
				continue;
			for(final String clazz:module.getClassesToTransform())
				if(clazz.equals("*") || clazz.equals(transformedName))
					try{
						final byte[] newBytes = module.transform(name, transformedName, basicClass);
						basicClass = newBytes;
					}catch(final Exception e){
						BiomeTweakerCore.logger.error("Caught an exception from module "+module.getModuleName());
						e.printStackTrace();
					}

		}
		return basicClass;
	}

}
