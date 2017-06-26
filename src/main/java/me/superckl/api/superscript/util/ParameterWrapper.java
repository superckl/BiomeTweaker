package me.superckl.api.superscript.util;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.superscript.ScriptHandler;

public class ParameterWrapper {

	private final ParameterType type;
	private final int minNum;
	private final int maxNum;
	private final boolean varArgs;

	protected ParameterWrapper(final ParameterType type, final int minNum, final int maxNum, final boolean varArgs) {
		this.type = type;
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.varArgs = varArgs;
	}

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
				parsed.addAll(WarningHelper.uncheckedCast(obj));
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

	public ParameterType getType() {
		return this.type;
	}

	public int getMinNum() {
		return this.minNum;
	}

	public int getMaxNum() {
		return this.maxNum;
	}

	public boolean isVarArgs() {
		return this.varArgs;
	}

	public static Builder builder(){
		return new Builder();
	}

	public static class Builder{

		private ParameterType type;
		private int minNum;
		private int maxNum;
		private boolean varArgs;

		private Builder(){}

		public ParameterType type() {
			return this.type;
		}

		public Builder type(final ParameterType type) {
			this.type = type;
			return this;
		}

		public int minNum() {
			return this.minNum;
		}

		public Builder minNum(final int minNum) {
			this.minNum = minNum;
			return this;
		}

		public int maxNum() {
			return this.maxNum;
		}

		public Builder maxNum(final int maxNum) {
			this.maxNum = maxNum;
			return this;
		}

		public boolean varArgs() {
			return this.varArgs;
		}

		public Builder varArgs(final boolean varArgs) {
			this.varArgs = varArgs;
			return this;
		}

		public ParameterWrapper build(){
			return new ParameterWrapper(this.type, this.minNum, this.maxNum, this.varArgs);
		}

	}

}
