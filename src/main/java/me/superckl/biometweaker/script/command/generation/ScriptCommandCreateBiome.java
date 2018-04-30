package me.superckl.biometweaker.script.command.generation;

import java.lang.reflect.Constructor;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Primitives;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.property.PropertyField;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.handler.RegistryEventHandler;
import me.superckl.biometweaker.common.reference.ModData;
import me.superckl.biometweaker.common.world.biome.BiomeTweakerBiome;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.util.LogHelper;
import me.superckl.biometweaker.util.ObfNameHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;

@AutoRegister(classes = TweakerScriptObject.class, name = "createBiome")
@RequiredArgsConstructor
public class ScriptCommandCreateBiome extends ScriptCommand{

	public static final Map<Class<? extends Biome>, List<? extends Property<?>>> extraParameters = new IdentityHashMap<>();

	static {
		ScriptCommandCreateBiome.extraParameters.put(BiomeForest.class, ImmutableList.of(new PropertyField<>(BiomeForest.class, ObfNameHelper.Fields.BIOMEFOREST_TYPE.getName(), BiomeForest.Type.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomeTaiga.class, ImmutableList.of(new PropertyField<>(BiomeTaiga.class, ObfNameHelper.Fields.BIOMETAIGA_TYPE.getName(), BiomeTaiga.Type.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomeHills.class, ImmutableList.of(new PropertyField<>(BiomeHills.class, ObfNameHelper.Fields.BIOMEHILLS_TYPE.getName(), BiomeHills.Type.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomeSnow.class, ImmutableList.of(new PropertyField<>(BiomeSnow.class, ObfNameHelper.Fields.SUPERICY.getName(), Boolean.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomePlains.class, ImmutableList.of(new PropertyField<>(BiomePlains.class, ObfNameHelper.Fields.SUNFLOWERS.getName(), Boolean.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomeMesa.class, ImmutableList.of(new PropertyField<>(BiomeMesa.class, ObfNameHelper.Fields.BRYCEPILLARS.getName(), Boolean.class),
				new PropertyField<>(BiomeMesa.class, ObfNameHelper.Fields.HASFOREST.getName(), Boolean.class)));
		ScriptCommandCreateBiome.extraParameters.put(BiomeJungle.class, ImmutableList.of(new PropertyField<>(BiomeJungle.class, ObfNameHelper.Fields.ISEDGE.getName(), Boolean.class)));
	}

	private final String rLoc;
	private final BiomePackage toCopy;

	public ScriptCommandCreateBiome(final String rLoc) {
		this(rLoc, null);
	}

	@Override
	public void perform() throws Exception {
		if(RegistryEventHandler.registry == null)
			throw new IllegalStateException("No biome registry avilable! Make sure you're using the biome registry script stage!");
		if(this.toCopy == null){
			final BiomeTweakerBiome biome = new BiomeTweakerBiome(new BiomeProperties("BiomeTweaker Biome").setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.8F).setRainfall(0.4F));
			if(!MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Create(this, biome))){
				biome.setRegistryName(ModData.MOD_ID, this.rLoc.toLowerCase());
				RegistryEventHandler.registry.register(biome);
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
				//catches all vanilla biomes
				if(ScriptCommandCreateBiome.extraParameters.containsKey(toCopy.getBiomeClass())) {
					final List<? extends Property<?>> props = ScriptCommandCreateBiome.extraParameters.get(toCopy.getBiomeClass());
					final Class<?>[] types = new Class<?>[props.size()+1];
					for(int i = 0; i < props.size(); i++)
						types[i] = Primitives.unwrap(props.get(i).getTypeClass());
					types[types.length-1] = BiomeProperties.class;
					construct = toCopy.getBiomeClass().getConstructor(types);
				}else
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
			} else
				switch(construct.getParameterCount()) {
				case 0:
					biome = construct.newInstance();
					break;
				case 1:
					biome = construct.newInstance(new BiomeProperties(BiomePropertyManager.NAME.get(toCopy)));
					break;
				default:
					final List<? extends Property<?>> props = ScriptCommandCreateBiome.extraParameters.get(toCopy.getBiomeClass());
					final Object[] objs = new Object[props.size()+1];
					for(int i = 0; i < props.size(); i++)
						objs[i] = props.get(i).get(toCopy);
					objs[objs.length-1] = new BiomeProperties(BiomePropertyManager.NAME.get(toCopy));
					biome = construct.newInstance(objs);
					break;
				}
			if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.Create(this, biome)))
				return;
			biome.setRegistryName(ModData.MOD_ID, this.rLoc.toLowerCase());
			RegistryEventHandler.registry.register(biome);
			//Copy props
			for(final Property<?> prop:BiomePropertyManager.getAllProperties())
				if(prop.isCopyable())
					prop.copy(toCopy, biome);
			//Copy dict types
			if(BiomeDictionary.hasAnyType(toCopy))
				BiomeDictionary.addTypes(biome, BiomeDictionary.getTypes(toCopy).toArray(new BiomeDictionary.Type[0]));
			//Copy spawns
			for(final EnumCreatureType type:EnumCreatureType.values()) {
				final List<SpawnListEntry> entries = biome.getSpawnableList(type);
				entries.clear();
				entries.addAll(toCopy.getSpawnableList(type));
			}
			BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(biome));
		}
	}

}
