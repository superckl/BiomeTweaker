package me.superckl.api.biometweaker.script.wrapper;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.util.ParameterType;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;

public class NoArgsParameterWrapper<K> extends ParameterWrapper{

	private final Class<K> clazz;

	public NoArgsParameterWrapper(final Class<K> clazz) {
		this(ParameterTypes.BLANK, clazz);
	}

	public NoArgsParameterWrapper(final ParameterType type, final Class<K> clazz) {
		super(type, 1, 1, false);
		this.clazz = clazz;
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		return Pair.of(new Object[] {this.clazz.newInstance()}, args);
	}

}
