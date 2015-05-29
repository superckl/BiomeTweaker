package me.superckl.biometweaker.core.module;

import net.minecraft.launchwrapper.IClassTransformer;

public interface IClassTransformerModule extends IClassTransformer{

	public String[] getClassesToTransform();
	public String getModuleName();
	public boolean isRequired();
	
}
