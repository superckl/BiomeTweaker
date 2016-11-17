package me.superckl.biometweaker.proxy;

import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import me.superckl.biometweaker.common.handler.WorldEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		final BiomeEventHandler handler = new BiomeEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		MinecraftForge.TERRAIN_GEN_BUS.register(handler);
		final EntityEventHandler eHandler = new EntityEventHandler();
		MinecraftForge.EVENT_BUS.register(eHandler);
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
	}

}
