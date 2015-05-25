package me.superckl.biometweaker.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import me.superckl.biometweaker.common.reference.ModData;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@SortingIndex(1001)
@MCVersion("1.7.10")
@Name("BiomeTweakerCore")
@TransformerExclusions({"me.superckl.biometweaker.core", "me.superckl.biometweaker.util", "me.superckl.biometweaker.config", "squeek.asmhelper.me.superckl.biometweaker", "me.superckl.biometweaker.script"})
public class BiomeTweakerCore extends AccessTransformer implements IFMLLoadingPlugin{

	public static File mcLocation;

	public BiomeTweakerCore() throws IOException {
		super(ModData.MOD_ID.toLowerCase()+"_at.cfg");
	}

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
		ModBiomeTweakerCore.logger.info("Attempting to discover integration callbacks in coremods...");
		final List coremods = (List) data.get("coremodList");
		Field coreModInstance = null;
		for(final Object obj:coremods)
			try {
				if(coreModInstance == null){
					coreModInstance = obj.getClass().getDeclaredField("coreModInstance");
					coreModInstance.setAccessible(true);
				}
				final IFMLLoadingPlugin plugin = (IFMLLoadingPlugin) coreModInstance.get(obj);
				if(plugin == null)
					continue;
				final Class<?> clazz = plugin.getClass();
				final Method method = clazz.getDeclaredMethod("btCallback");
				ModBiomeTweakerCore.logger.info("Found integration callback in coremod "+obj.toString());
				if((method.getModifiers() & java.lang.reflect.Modifier.STATIC) == java.lang.reflect.Modifier.STATIC)
					method.invoke(null);
				else
					method.invoke(plugin);
			}catch(final NoSuchMethodException nm){
				//Swallow it, this means they have no callback method.
			}catch (final Exception e) {
				ModBiomeTweakerCore.logger.error("Failed to discover callbacks for "+obj.toString());
				e.printStackTrace();
				continue;
			}
	}

	@Override
	public String getAccessTransformerClass() {
		return this.getClass().getName();
	}

}
