package me.superckl.biometweaker.integration;

import com.google.gson.JsonObject;

import net.minecraft.world.biome.Biome;

public interface IIntegrationModule {

	public void preInit();
	public void init();
	public void postInit();
	public String getName();
	public void addBiomeInfo(Biome biome, JsonObject obj);

}
