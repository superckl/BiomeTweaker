package me.superckl.api.biometweaker.script.wrapper;

import org.apache.commons.lang3.tuple.Pair;

import me.superckl.api.biometweaker.script.pack.PropertyRangeBiomePackage;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.ParameterWrapper;

public class PropertyRangePackParameterWrapper extends ParameterWrapper{

	protected PropertyRangePackParameterWrapper() {
		super(BTParameterTypes.PROPERTY_RANGE_PACKAGE, 3, 3, false);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		if(args.length < 3)
			throw new IllegalArgumentException("Property range requires three parameters!");
		final String prop = (String) ParameterTypes.STRING.tryParse(args[0], handler);
		if(prop == null)
			throw new IllegalArgumentException("Property range requires a property name as the first parameter!");
		final Float lowBound = (Float) ParameterTypes.FLOAT.tryParse(args[1], handler);
		if(lowBound == null)
			throw new IllegalArgumentException("Property range requires a numerical value as the second parameter!");
		final Float highBound = (Float) ParameterTypes.FLOAT.tryParse(args[2], handler);
		if(highBound == null)
			throw new IllegalArgumentException("Property range requires a numerical value as the third parameter!");
		final String[] toReturn = new String[args.length-3];
		System.arraycopy(args, 3, toReturn, 0, toReturn.length);
		return Pair.of(new Object[] {new PropertyRangeBiomePackage(prop, lowBound, highBound)}, toReturn);
	}

}
