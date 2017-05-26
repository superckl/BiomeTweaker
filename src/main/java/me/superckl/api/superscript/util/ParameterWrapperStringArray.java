package me.superckl.api.superscript.util;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.superscript.ScriptHandler;

public class ParameterWrapperStringArray extends ParameterWrapper{

	protected ParameterWrapperStringArray() {
		super(ParameterTypes.STRING_ARRAY, 0, Integer.MAX_VALUE, true);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		final List<String> parsed = Lists.newArrayList();
		for (final String arg : args) {
			final String parse = (String) ParameterTypes.STRING.tryParse(arg, handler);
			if(parse == null)
				break;
			parsed.add(parse);
		}
		final String[] toReturn = new String[args.length-parsed.size()];
		System.arraycopy(args, parsed.size(), toReturn, 0, toReturn.length);
		return Pair.of(new Object[] {parsed.toArray(new String[parsed.size()])}, toReturn);
	}

}
