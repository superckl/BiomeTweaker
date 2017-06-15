package me.superckl.api.superscript.util;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.gson.JsonPrimitive;

import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;

public final class ParameterTypes {

	public static final ParameterType JSON_ELEMENT = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			return new JsonPrimitive(parameter);
		}
	};
	public static final ParameterType NON_NEG_BYTE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final Integer i = (Integer) ParameterTypes.NON_NEG_INTEGER.tryParse(parameter, handler);
			return (i == null) || (i > Byte.MAX_VALUE) || (i < Byte.MIN_VALUE) ? null:i.byteValue();
		}
	};
	public static final ParameterType BYTE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final Integer i = (Integer) ParameterTypes.INTEGER.tryParse(parameter, handler);
			return (i == null) || (i > Byte.MAX_VALUE) || (i < Byte.MIN_VALUE) ? null:i.byteValue();
		}
	};
	public static final ParameterType FLOAT = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			return Floats.tryParse(parameter);
		}
	};
	public static final ParameterType NON_NEG_INTEGERS = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final List<Integer> ints = (List<Integer>) ParameterTypes.INTEGERS.tryParse(parameter, handler);
			final Iterator<Integer> it = ints.iterator();
			while(it.hasNext())
				if(it.next() < 0)
					it.remove();
			return ints;
		}
	};
	public static final ParameterType NON_NEG_INTEGER = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			final Integer i = Ints.tryParse(parameter);
			if((i != null) && (i.intValue() >= 0))
				return i;
			return null;
		}
	};
	public static final ParameterType INTEGERS = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
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
			return ints;
		}
	};
	public static final ParameterType INTEGER = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			return Ints.tryParse(parameter);
		}
	};
	public static final ParameterType STRING = new ParameterType(){

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			if(parameter.startsWith("\"") && parameter.endsWith("\""))
				return parameter.substring(1, parameter.length()-1);
			return null;
		}

	};

	public static final ParameterType STRING_ARRAY = new ParameterType(new ParameterWrapperStringArray()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final String parsed = (String) ParameterTypes.STRING.tryParse(parameter, handler);
			if(parsed == null)
				return null;
			return new String[] {parsed};
		}
	};

	public static final ParameterType BLANK = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return null;
		}
	};

}
