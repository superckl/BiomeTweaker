package me.superckl.biometweaker.integration;

import com.google.gson.JsonObject;

import net.minecraft.world.biome.Biome;

public abstract class IntegrationModule {

	public abstract void preInit();
	public abstract void init();
	public abstract void postInit();
	public abstract String getName();
	public abstract void addBiomeInfo(Biome biome, JsonObject obj);

}
