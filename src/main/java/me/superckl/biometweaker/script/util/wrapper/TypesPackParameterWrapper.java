package me.superckl.biometweaker.script.util.wrapper;

import java.util.List;

import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.pack.TypeBiomesPackage;
import me.superckl.biometweaker.script.util.ParameterType;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public class TypesPackParameterWrapper extends ParameterWrapper{

	public TypesPackParameterWrapper() {
		super(ParameterType.TYPE_BIOMES_PACKAGE, 1, 1, false);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		final List<String> parsed = Lists.newArrayList();
		String[] toReturn = new String[0];
		for(int i = 0; i < args.length; i++){
			final String obj = (String) ParameterType.STRING.tryParse(args[i], handler);
			if(obj == null){
				toReturn = new String[args.length-i];
				System.arraycopy(args, i, toReturn, 0, toReturn.length);
				break;
			}
			parsed.add(obj);
		}
		return Pair.of(new Object[] {new TypeBiomesPackage(parsed.toArray(new String[parsed.size()]))}, toReturn);
	}

}
