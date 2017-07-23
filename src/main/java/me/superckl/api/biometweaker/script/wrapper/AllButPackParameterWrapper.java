package me.superckl.api.biometweaker.script.wrapper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.script.pack.AllButBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.api.superscript.script.ParameterWrapper;
import me.superckl.api.superscript.script.ScriptHandler;

public class AllButPackParameterWrapper extends ParameterWrapper<AllButBiomesPackage>{

	public AllButPackParameterWrapper() {
		super(BTParameterTypes.ALL_BUT_BIOMES_PACKAGE, 1, 1, false);
	}

	@Override
	public Pair<AllButBiomesPackage[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		final List<BiomePackage> parsed = Lists.newArrayList();
		String[] toReturn = new String[0];
		for(int i = 0; i < args.length; i++){
			final BiomePackage obj = BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(args[i], handler);
			if(obj == null){
				toReturn = new String[args.length-i];
				System.arraycopy(args, i, toReturn, 0, toReturn.length);
				break;
			}
			parsed.add(obj);
		}
		return Pair.of(new AllButBiomesPackage[] {new AllButBiomesPackage(new MergedBiomesPackage(parsed.toArray(new BiomePackage[parsed.size()])))}, toReturn);
	}

}
