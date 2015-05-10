package me.superckl.biometweaker.script.util.wrapper;

import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.biometweaker.script.util.ParameterType;

import org.apache.commons.lang3.tuple.Pair;

public class AllPackParameterWrapper extends ParameterWrapper{

	public AllPackParameterWrapper() {
		super(ParameterType.ALL_BIOMES_PACKAGE, 1, 1, false);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		return Pair.of(new Object[] {new AllBiomesPackage()}, args);
	}

}
