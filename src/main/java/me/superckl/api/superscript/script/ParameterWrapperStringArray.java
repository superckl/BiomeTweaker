package me.superckl.api.superscript.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class ParameterWrapperStringArray extends ParameterWrapper<String[]>{

	protected ParameterWrapperStringArray() {
		super(ParameterTypes.STRING_ARRAY, 0, Integer.MAX_VALUE, true);
	}

	@Override
	public Pair<String[][], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		final List<String> parsed = new ArrayList<>();
		for (final String arg : args) {
			final String parse = ParameterTypes.STRING.tryParse(arg, handler);
			if(parse == null)
				break;
			parsed.add(parse);
		}
		final String[] toReturn = new String[args.length-parsed.size()];
		System.arraycopy(args, parsed.size(), toReturn, 0, toReturn.length);
		return Pair.of(new String[][] {parsed.toArray(new String[parsed.size()])}, toReturn);
	}

}
