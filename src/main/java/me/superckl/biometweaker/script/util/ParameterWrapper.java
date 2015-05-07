package me.superckl.biometweaker.script.util;

import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.pack.AllBiomesPackage;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.Pair;

@Getter
@Builder
public class ParameterWrapper {

	private final ParameterType type;
	private final int minNum;
	private final int maxNum;
	private final boolean varArgs;

	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, String ... args) throws Exception{
		if(this.type == ParameterType.ALL_BIOMES_PACKAGE)
			return Pair.of(new Object[]{new AllBiomesPackage()}, new String[0]);
		final List<Object> parsed = Lists.newArrayList();
		for(int i = 0; ; i++){
			Object obj;
			if(!this.shouldCont(i, args.length) || ((obj = this.type.tryParse(args[i], handler)) == null)){
				final String[] newArgs = new String[args.length-i];
				System.arraycopy(args, i, newArgs, 0, newArgs.length);
				args = newArgs;
				break;
			}
			if(obj instanceof Collection)
				parsed.addAll((Collection<? extends Object>) obj);
			else
				parsed.add(obj);
		}
		return Pair.of(parsed.toArray(), args);
	}

	private boolean shouldCont(final int index, final int argsLength){
		if(!this.varArgs)
			if(index < this.minNum)
				return true;
			else if(index >= this.maxNum)
				return false;
		return index < argsLength;
	}

}
