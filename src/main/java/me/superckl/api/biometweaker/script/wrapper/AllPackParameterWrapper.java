package me.superckl.api.biometweaker.script.wrapper;

import me.superckl.api.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.util.ParameterWrapper;

import org.apache.commons.lang3.tuple.Pair;

public class AllPackParameterWrapper extends ParameterWrapper{

	public AllPackParameterWrapper() {
		super(BTParameterTypes.ALL_BIOMES_PACKAGE, 1, 1, false);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		return Pair.of(new Object[] {new AllBiomesPackage()}, args);
	}

}
