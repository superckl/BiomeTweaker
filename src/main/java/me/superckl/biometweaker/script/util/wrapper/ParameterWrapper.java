package me.superckl.biometweaker.script.util.wrapper;

import java.util.Collection;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.util.ParameterType;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

@Getter
@Builder
public class ParameterWrapper {

	private final ParameterType type;
	private final int minNum;
	private final int maxNum;
	private final boolean varArgs;

	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, String ... args) throws Exception{
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
			else if(obj != null)
				parsed.add(obj);
		}
		return Pair.of(parsed.toArray(), args);
	}

	private boolean shouldCont(final int index, final int argsLength){
		if(!this.varArgs && (index < argsLength))
			if(index < this.minNum)
				return true;
			else if(index >= this.maxNum)
				return false;
		return index < argsLength;
	}

	public boolean canReturnNothing(){
		return this.minNum <= 0;
	}

}
