package me.superckl.biometweaker.proxy;

import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		final BiomeEventHandler handler = new BiomeEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.TERRAIN_GEN_BUS.register(handler);
	}

}
