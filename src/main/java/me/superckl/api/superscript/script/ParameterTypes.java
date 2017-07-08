package me.superckl.api.superscript.script;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import me.superckl.api.superscript.util.CollectionHelper;
import me.superckl.api.superscript.util.WarningHelper;

public final class ParameterTypes {

	public static final ParameterType<JsonElement> JSON_ELEMENT = new ParameterType<JsonElement>(JsonElement.class) {

		@Override
		public JsonElement tryParse(final String parameter, final ScriptHandler handler) {
			return new JsonPrimitive(parameter);
		}
	};
	public static final ParameterType<Byte> NON_NEG_BYTE = new ParameterType<Byte>(Byte.class) {

		@Override
		public Byte tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final Integer i = ParameterTypes.NON_NEG_INTEGER.tryParse(parameter, handler);
			return (i == null) || (i > Byte.MAX_VALUE) || (i < Byte.MIN_VALUE) ? null:i.byteValue();
		}
	};
	public static final ParameterType<Byte> BYTE = new ParameterType<Byte>(Byte.class) {

		@Override
		public Byte tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final Integer i = ParameterTypes.INTEGER.tryParse(parameter, handler);
			return (i == null) || (i > Byte.MAX_VALUE) || (i < Byte.MIN_VALUE) ? null:i.byteValue();
		}
	};
	public static final ParameterType<Float> FLOAT = new ParameterType<Float>(Float.class) {

		@Override
		public Float tryParse(final String parameter, final ScriptHandler handler) {
			return Floats.tryParse(parameter);
		}
	};
	public static final ParameterType<int[]> NON_NEG_INTEGERS = new ParameterType<int[]>(int[].class) {

		@Override
		public int[] tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final List<Integer> ints = WarningHelper.uncheckedCast(ParameterTypes.INTEGERS.tryParse(parameter, handler));
			final Iterator<Integer> it = ints.iterator();
			while(it.hasNext())
				if(it.next() < 0)
					it.remove();
			return ints.stream().mapToInt(i -> i).toArray();
		}
	};
	public static final ParameterType<Integer> NON_NEG_INTEGER = new ParameterType<Integer>(Integer.class) {

		@Override
		public Integer tryParse(final String parameter, final ScriptHandler handler) {
			final Integer i = Ints.tryParse(parameter);
			if((i != null) && (i.intValue() >= 0))
				return i;
			return null;
		}
	};
	public static final ParameterType<int[]> INTEGERS = new ParameterType<int[]>(int[].class) {

		@Override
		public int[] tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final List<Integer> ints = Lists.newArrayList();
			final Object i = ParameterTypes.INTEGER.tryParse(parameter, handler);
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
			return ints.stream().mapToInt(j -> j).toArray();
		}
	};
	public static final ParameterType<Integer> INTEGER = new ParameterType<Integer>(Integer.class) {

		@Override
		public Integer tryParse(final String parameter, final ScriptHandler handler) {
			return Ints.tryParse(parameter);
		}
	};
	public static final ParameterType<String> STRING = new ParameterType<String>(String.class){

		@Override
		public String tryParse(final String parameter, final ScriptHandler handler) {
			if(parameter.startsWith("\"") && parameter.endsWith("\""))
				return parameter.substring(1, parameter.length()-1);
			return null;
		}

	};

	public static final ParameterType<String[]> STRING_ARRAY = new ParameterType<String[]>(String[].class, new ParameterWrapperStringArray()) {

		@Override
		public String[] tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final String parsed = ParameterTypes.STRING.tryParse(parameter, handler);
			if(parsed == null)
				return null;
			return new String[] {parsed};
		}
	};

	public static final ParameterType<Boolean> BOOLEAN = new ParameterType<Boolean>(Boolean.class) {

		@Override
		public Boolean tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			if(parameter.equals("true"))
				return true;
			else if(parameter.equals("false"))
				return false;
			return null;
		}
	};

	public static final ParameterType<Object> BLANK = new ParameterType<Object>(Object.class) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return null;
		}
	};

}
