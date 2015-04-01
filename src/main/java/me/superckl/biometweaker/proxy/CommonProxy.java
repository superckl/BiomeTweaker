package me.superckl.biometweaker.proxy;

import me.superckl.biometweaker.common.handler.GenHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		MinecraftForge.EVENT_BUS.register(new GenHandler());
	}

}
