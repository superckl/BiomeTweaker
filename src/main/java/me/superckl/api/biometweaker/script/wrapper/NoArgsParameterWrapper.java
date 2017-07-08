package me.superckl.api.biometweaker.script.wrapper;

import java.lang.reflect.Array;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.superscript.script.ParameterWrapper;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.WarningHelper;

public class NoArgsParameterWrapper<T> extends ParameterWrapper<T>{

	private final Class<T> clazz;

	public NoArgsParameterWrapper(final Class<T> clazz) {
		super(null, 1, 1, false);
		this.clazz = clazz;
	}

	@Override
	public Pair<T[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		final T[] array = WarningHelper.uncheckedCast(Array.newInstance(this.clazz, 1));
		Array.set(array, 0, this.clazz.newInstance());
		return Pair.of(array, args);
	}

}
