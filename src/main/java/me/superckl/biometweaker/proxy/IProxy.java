package me.superckl.biometweaker.proxy;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

public interface IProxy {

	public void registerHandlers();
	public void initProperties();
	public void setupScripts(ASMDataTable dataTable);

}
