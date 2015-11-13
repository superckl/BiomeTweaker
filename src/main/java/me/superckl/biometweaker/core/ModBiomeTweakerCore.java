package me.superckl.biometweaker.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import me.superckl.biometweaker.common.reference.ModData;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class ModBiomeTweakerCore extends DummyModContainer{

	public static final Logger logger = LogManager.getLogger("BiomeTweakerCore");

	public ModBiomeTweakerCore() {
		super(new ModMetadata());
		final ModMetadata meta = this.getMetadata();
		meta.modId = "BiomeTweakerCore";
		meta.name = "BiomeTweaker Core";
		meta.parent = ModData.MOD_ID;
		meta.version = ModData.VERSION;
		meta.authorList = Lists.newArrayList("superckl");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

}
