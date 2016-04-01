package me.superckl.biometweaker.core;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

@SortingIndex(1001)
@MCVersion("1.8")
@Name("BiomeTweakerCore")
@TransformerExclusions({"me.superckl.biometweaker.core", "me.superckl.biometweaker.util", "me.superckl.biometweaker.config", "squeek.asmhelper.me.superckl.biometweaker", "me.superckl.api.superscript"})
public class BiomeTweakerCore implements IFMLLoadingPlugin{

	public static File mcLocation;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {BiomeTweakerASMTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return ModBiomeTweakerCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return BiomeTweakerCallHook.class.getName();
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		BiomeTweakerCore.mcLocation = (File) data.get("mcLocation");
		ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
		ObfHelper.setRunsAfterDeobfRemapper(true);
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
