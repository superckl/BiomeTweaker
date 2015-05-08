package me.superckl.biometweaker.script.util;

import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.ScriptObject;
import me.superckl.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.biometweaker.script.pack.AllButBiomesPackage;
import me.superckl.biometweaker.script.pack.BasicBiomesPackage;
import me.superckl.biometweaker.script.pack.IBiomePackage;
import me.superckl.biometweaker.script.pack.TypeBiomesPackage;
import me.superckl.biometweaker.util.CollectionHelper;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.gson.JsonPrimitive;

@Getter
public enum ParameterType {

	STRING, INTEGER, INTEGERS, NON_NEG_INTEGER, NON_NEG_INTEGERS, FLOAT, SPAWN_TYPE, JSON_ELEMENT, BASIC_BIOMES_PACKAGE, TYPE_BIOMES_PACKAGE(new TypesPackParameterWrapper()),
	ALL_BIOMES_PACKAGE(new AllPackParameterWrapper()), ALL_BUT_BIOMES_PACKAGE(new AllButPackParameterWrapper());

	private final ParameterWrapper simpleWrapper;
	private final ParameterWrapper varArgsWrapper;
	private ParameterWrapper specialWrapper = null;

	private ParameterType() {
		this.simpleWrapper = ParameterWrapper.builder().type(this).minNum(1).maxNum(1).build();
		this.varArgsWrapper = ParameterWrapper.builder().type(this).varArgs(true).build();
	}

	private ParameterType(final ParameterWrapper wrapper){
		this();
		this.specialWrapper = wrapper;
	}

	public Object tryParse(final String parameter) throws Exception{
		return this.tryParse(parameter, null);
	}

	public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception{
		switch(this){
		case FLOAT:{
			return Floats.tryParse(parameter);
		}
		case INTEGER:{
			return Ints.tryParse(parameter);
		}
		case INTEGERS:{
			final List<Integer> ints = Lists.newArrayList();
			final List<IBiomePackage> toMerge = Lists.newArrayList();
			final Object i = ParameterType.INTEGER.tryParse(parameter, handler);
			if(i == null){
				if(parameter.contains("-")){
					final String[] split = parameter.split("-");
					if((split.length == 2) && ScriptParser.isPositiveInteger(split[0]) && ScriptParser.isPositiveInteger(split[1])){
						final int start = Integer.parseInt(split[0]);
						final int end = Integer.parseInt(split[1]);
						final int[] values = CollectionHelper.range(start, end);
						CollectionHelper.addAll(ints, values);
					}
				}
			}else
				ints.add((Integer) i);
			return ints;
		}
		case NON_NEG_INTEGER:{
			final Integer i = Ints.tryParse(parameter);
			if((i != null) && (i.intValue() >= 0))
				return i;
			break;
		}
		case NON_NEG_INTEGERS:{
			final List<Integer> ints = (List<Integer>) ParameterType.INTEGERS.tryParse(parameter, handler);
			final Iterator<Integer> it = ints.iterator();
			while(it.hasNext())
				if(it.next() < 0)
					it.remove();
			return ints;
		}
		case STRING:{
			if(parameter.startsWith("\"") && parameter.endsWith("\""))
				return parameter.substring(1, parameter.length()-1);
			break;
		}
		case SPAWN_TYPE: {
			final String extracted = (String) ParameterType.STRING.tryParse(parameter, handler);
			if(extracted != null)
				return Type.valueOf(extracted);
			break;
		}
		case JSON_ELEMENT: {
			return new JsonPrimitive(parameter);
		}
		case BASIC_BIOMES_PACKAGE: {
			final List<Integer> ints = (List<Integer>) ParameterType.NON_NEG_INTEGERS.tryParse(parameter, handler);
			if(ints.isEmpty()){
				final ScriptObject obj = handler.getObjects().get(parameter);
				if((obj == null) || !(obj instanceof BiomesScriptObject))
					return null;
				return ((BiomesScriptObject)obj).getPack();
			}
			final int[] array = new int[ints.size()];
			for(int i = 0; i < array.length; i++)
				array[i] = ints.get(i);
			return new BasicBiomesPackage(array);
		}case TYPE_BIOMES_PACKAGE: {
			return new TypeBiomesPackage((String) ParameterType.STRING.tryParse(parameter, handler));
		}case ALL_BUT_BIOMES_PACKAGE: {
			return new AllButBiomesPackage((IBiomePackage) ParameterType.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}case ALL_BIOMES_PACKAGE: {
			return new AllBiomesPackage();
		}
		default:
			break;
		}
		return null;
	}

	public boolean hasSpecialWrapper(){
		return this.specialWrapper != null;
	}

}
