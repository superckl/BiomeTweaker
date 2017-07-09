package me.superckl.biometweaker.script.command.generation;

import java.lang.reflect.Constructor;
import java.util.Iterator;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.BiomeHelper;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = TweakerScriptObject.class, name = "createBiome")
@RequiredArgsConstructor
public class ScriptCommandAddBiome implements IScriptCommand{

	private final String rLoc;
	private final IBiomePackage toCopy;

	public ScriptCommandAddBiome(final String rLoc) {
		this(rLoc, null);
	}

	@Override
	public void perform() throws Exception {
		final int id = BiomeHelper.getNextFreeBiomeId();
		if(this.toCopy == null){
			final BiomeTweakerBiome biome = new BiomeTweakerBiome(new BiomeProperties("BiomeTweaker Biome").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F));
			if(!MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Create(this, biome))){
				Biome.registerBiome(id, ModData.MOD_ID+":"+this.rLoc.toLowerCase(), biome);
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(biome));
			}
		}else{
			final Iterator<Biome> it = this.toCopy.getIterator();
			if(!it.hasNext())
				throw new IllegalStateException("No biome found to copy!");
			final Biome toCopy = it.next();
			if(it.hasNext())
				LogHelper.warn("More than one biome found to copy! Only the first one will be copied.");
			Constructor<? extends Biome> construct = null;
			try{
				//catches most vanilla biomes, a few exceptions
				construct = toCopy.getBiomeClass().getConstructor(BiomeProperties.class);
			} catch(final Exception e){
				try{
					//Catches most BOP biomes
					construct = toCopy.getBiomeClass().getConstructor();
				} catch(final Exception e1){}
			}
			Biome biome;
			if(construct == null){
				LogHelper.warn("Unable to copy biome class "+toCopy.getBiomeClass().getCanonicalName()+"! Some functionality may not be copied!");
				biome = new BiomeTweakerBiome(new BiomeProperties("BiomeTweaker Biome").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F));
			}else{
				final boolean noArgs = construct.getParameterCount() == 0;
				biome = noArgs ? construct.newInstance():construct.newInstance(new BiomeProperties(toCopy.getBiomeName()));
			}
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Create(this, biome)))
				return;
			Biome.registerBiome(id, ModData.MOD_ID+":"+this.rLoc.toLowerCase(), biome);
			//Copy props
			for(final Property<?> prop:BiomePropertyManager.propertyMap.values())
				if(prop.isCopyable())
					prop.copy(toCopy, biome);
			//Copy dict types
			if(BiomeDictionary.hasAnyType(toCopy))
				BiomeDictionary.addTypes(biome, BiomeDictionary.getTypes(toCopy).toArray(new BiomeDictionary.Type[0]));
			//Copy spawns
			biome.spawnableCaveCreatureList = Lists.newArrayList(toCopy.spawnableCaveCreatureList);
			biome.spawnableCreatureList = Lists.newArrayList(toCopy.spawnableCreatureList);
			biome.spawnableMonsterList = Lists.newArrayList(toCopy.spawnableMonsterList);
			biome.spawnableWaterCreatureList = Lists.newArrayList(toCopy.spawnableWaterCreatureList);
			//TODO modSpawnables? Forge adds it so it'd have to be reflection
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(biome));
		}
	}

}
