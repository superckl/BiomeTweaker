package me.superckl.biometweaker.core;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class ModBiomeTweakerCore extends DummyModContainer{
	
	public static final Logger logger = LogManager.getLogger("BiomeTweakerCore");
	
	public ModBiomeTweakerCore() {
		super(new ModMetadata());
		final ModMetadata meta = this.getMetadata();
		meta.modId = "BiomeTweakerCore";
		meta.name = "BiomeTweaker Core";
		meta.parent = "BiomeTweaker";
		meta.version = "0.1";
		meta.authorList = Arrays.asList("superckl");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
}
