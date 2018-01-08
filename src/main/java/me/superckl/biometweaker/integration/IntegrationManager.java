package me.superckl.biometweaker.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import lombok.Getter;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

public class IntegrationManager extends IntegrationModule{

	public static final IntegrationManager INSTANCE = new IntegrationManager();
	private static final Map<List<String>, String> modules = new HashMap<>();

	static{
		IntegrationManager.modules.put(Arrays.asList(new String[] {"biomesoplenty"}), "me.superckl.biometweaker.integration.bop.BOPIntegrationModule");
	}

	@Getter
	private final List<IntegrationModule> activeModules = new ArrayList<>();

	private IntegrationManager() {}

	@Override
	public void preInit(){
		boolean noGo = false;
		for(final Entry<List<String>, String> entry:IntegrationManager.modules.entrySet()){
			for(final String mod:entry.getKey())
				if(!Loader.isModLoaded(mod)){
					noGo = true;
					break;
				}
			if(!noGo)
				try {
					final IntegrationModule module = (IntegrationModule) Class.forName(entry.getValue()).newInstance();
					this.activeModules.add(module);
					LogHelper.info("Enabled " + module.getName() + " module.");
				} catch (final Exception e) {
					LogHelper.error("Failed to instantiate integration module "+entry.getValue());
					e.printStackTrace();
				}
		}
		for(final IntegrationModule module:this.activeModules)
			module.preInit();
	}


	@Override
	public void init() {
		for(final IntegrationModule module:this.activeModules)
			module.init();
	}

	@Override
	public void postInit(){
		for(final IntegrationModule module:this.activeModules)
			module.postInit();
	}

	@Override
	public String getName() {
		return "BiomeTweaker Integration Manager";
	}

	@Override
	public void addBiomeInfo(final Biome biome, final JsonObject obj) {
		for(final IntegrationModule module:this.activeModules)
			module.addBiomeInfo(biome, obj);
	}

}
