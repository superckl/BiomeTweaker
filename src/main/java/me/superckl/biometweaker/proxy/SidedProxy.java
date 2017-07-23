package me.superckl.biometweaker.proxy;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

public abstract class SidedProxy {

	public abstract void registerHandlers();
	public abstract void initProperties();
	public abstract void setupScripts(ASMDataTable dataTable);

}
