package me.superckl.biometweaker;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import me.superckl.api.biometweaker.property.PropertyField;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.AutoRegister.RegisterExempt;
import me.superckl.api.superscript.AutoRegisters;
import me.superckl.api.superscript.script.ParameterType;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ParameterWrapper;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.api.superscript.script.command.ScriptCommandListing;
import me.superckl.api.superscript.script.command.ScriptCommandRegistry;
import me.superckl.api.superscript.script.object.ScriptObject;
import me.superckl.api.superscript.util.ConstructorListing;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.common.world.gen.ReplacementPropertyManager;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import me.superckl.biometweaker.script.object.block.BasicBlockStateScriptObject;
import me.superckl.biometweaker.script.object.block.BlockReplacementScriptObject;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

public class ScriptSetup {

	public static void setupScripts(final Collection<ModFileScanData> scanData) {

		//Ensure BTParamterTypes registers its defaults
		BTParameterTypes.BLOCKSTATE_BUILDER.getTypeClass();
		LogHelper.debug("Discovering @AutoRegister script commands...");
		final Map<Class<? extends ScriptObject>, Map<String, ScriptCommandListing>> listings = new HashMap<>();

		final Type autoReg = Type.getType(AutoRegister.class);
		final Type autoRegs = Type.getType(AutoRegisters.class);

		final Set<AnnotationData> datas = Sets.newIdentityHashSet();

		scanData.forEach(modData -> modData.getAnnotations().stream().filter(annotData -> annotData.annotationType().equals(autoReg)).forEach(datas::add));
		scanData.forEach(modData -> modData.getAnnotations().stream().filter(annotData -> annotData.annotationType().equals(autoRegs)).map(ScriptSetup::ungroupAutoRegs).forEach(datas::addAll));

		final Set<Type> examinedClasses = new HashSet<>();
		for(final AnnotationData data : datas)
			try {
				if(examinedClasses.contains(data.clazz()))
					continue;
				final Class<?> asmClass = Class.forName(data.clazz().getClassName());
				final Class<? extends ScriptCommand> scriptClass = asmClass.asSubclass(ScriptCommand.class);
				final AutoRegister[] classAnns = scriptClass.getAnnotationsByType(AutoRegister.class);
				final Constructor<?>[] cons = scriptClass.getConstructors();
				for(final Constructor<?> con:cons){
					if(con.isAnnotationPresent(RegisterExempt.class))
						continue;
					final AutoRegister[] methodAnns = con.getAnnotationsByType(AutoRegister.class);
					if(classAnns.length == 0 && methodAnns.length == 0)
						continue;
					AutoRegister[] annsToUse;
					if(methodAnns.length != 0)
						annsToUse = methodAnns;
					else
						annsToUse = classAnns;
					final Class<?>[] cTypes = con.getParameterTypes();
					final ParameterWrapper<?>[] pTypes = new ParameterWrapper[cTypes.length];
					for(final ParameterOverride override:con.getAnnotationsByType(ParameterOverride.class))
						pTypes[override.parameterIndex()] = ParameterTypes.getExceptionWrapper(override.exceptionKey());
					for(int i = 0; i < cTypes.length; i++){
						if(pTypes[i] != null)
							continue;
						final ParameterType<?> type = ParameterTypes.getDefaultType(cTypes[i]);
						if(type == null)
							throw new IllegalStateException("No parameter type found for parameter "+cTypes[i].getCanonicalName());
						pTypes[i] = type.hasSpecialWrapper() ? type.getSpecialWrapper():type.getSimpleWrapper();
					}
					for(final AutoRegister ann:annsToUse)
						for(final Class<? extends ScriptObject> clazz:ann.classes()){
							if(!listings.containsKey(clazz))
								listings.put(clazz, new HashMap<>());
							final Map<String, ScriptCommandListing> map = listings.get(clazz);
							if(!map.containsKey(ann.name()))
								map.put(ann.name(), new ScriptCommandListing());
							final ScriptCommandListing listing = map.get(ann.name());
							listing.addEntry(Lists.newArrayList(pTypes), WarningHelper.uncheckedCast(con));
						}
				}
				examinedClasses.add(data.clazz());
			} catch (final Exception e) {
				LogHelper.error("Failed to auto-register a script command "+data.toString());
				e.printStackTrace();
			}
		for(final Entry<Class<? extends ScriptObject>, Map<String, ScriptCommandListing>> entry:listings.entrySet()){
			LogHelper.debug("Registering "+entry.getValue().size()+" commands to "+entry.getKey().getSimpleName());
			ScriptCommandRegistry.INSTANCE.registerClassListing(entry.getKey(), entry.getValue());
		}

		ScriptHandler.registerStaticObject("Tweaker", TweakerScriptObject.class);

		try {
			ConstructorListing<ScriptObject> listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getVarArgsWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomes", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.TYPE_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomesOfTypes", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.ALL_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forAllBiomes", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.ALL_BUT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forAllBiomesExcept", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.INTERSECT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("intersectionOf", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.SUBTRACT_BIOMES_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("subtractFrom", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.PROPERTY_RANGE_PACKAGE.getSpecialWrapper()), BiomesScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBiomesWithPropertyRange", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()), BasicBlockStateScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("forBlock", listing);

			listing = new ConstructorListing<>();
			listing.addEntry(new ArrayList<>(), BlockReplacementScriptObject.class.getDeclaredConstructor());
			ScriptParser.registerValidObjectInst("newBlockReplacement", listing);

		} catch (final Exception e2) {
			LogHelper.error("Failed to populate object listings! Some tweaks may not be applied.");
			e2.printStackTrace();
		}
	}

	public static void initProperties() {

		//Replacement Properties
		ReplacementPropertyManager.BLOCK = new PropertyField<>(ReplacementConstraints.class, "block", BlockState.class);
		ReplacementPropertyManager.MIN_Y = new PropertyField<>(ReplacementConstraints.class, "minY", Integer.class);
		ReplacementPropertyManager.MAX_Y = new PropertyField<>(ReplacementConstraints.class, "maxY", Integer.class);
		ReplacementPropertyManager.MIN_X = new PropertyField<>(ReplacementConstraints.class, "minX", Integer.class);
		ReplacementPropertyManager.MAX_X = new PropertyField<>(ReplacementConstraints.class, "maxX", Integer.class);
		ReplacementPropertyManager.MIN_Z = new PropertyField<>(ReplacementConstraints.class, "minZ", Integer.class);
		ReplacementPropertyManager.MAX_Z = new PropertyField<>(ReplacementConstraints.class, "maxZ", Integer.class);
		ReplacementPropertyManager.MIN_CHUNK_X = new PropertyField<>(ReplacementConstraints.class, "minChunkX", Integer.class);
		ReplacementPropertyManager.MAX_CHUNK_X = new PropertyField<>(ReplacementConstraints.class, "maxChunkX", Integer.class);
		ReplacementPropertyManager.MIN_CHUNK_Z = new PropertyField<>(ReplacementConstraints.class, "minChunkZ", Integer.class);
		ReplacementPropertyManager.MAX_CHUNK_Z = new PropertyField<>(ReplacementConstraints.class, "maxChunkZ", Integer.class);
		ReplacementPropertyManager.IGNORE_META = new PropertyField<>(ReplacementConstraints.class, "ignoreMeta", Boolean.class);
		ReplacementPropertyManager.CONTIGUOUS = new PropertyField<>(ReplacementConstraints.class, "contiguous", Boolean.class);

		ReplacementPropertyManager.populatePropertyMap();
	}

	public static Set<AnnotationData> ungroupAutoRegs(final AnnotationData autoRegs){
		final Type autoReg = Type.getType(AutoRegister.class);
		return autoRegs.annotationData().entrySet().stream()
				.map(entry -> new AnnotationData(autoReg, autoRegs.targetType(), autoRegs.clazz(), autoRegs.memberName(), Collections.singletonMap(entry.getKey(), entry.getValue())))
				.collect(Collectors.toSet());
	}

}
