package me.superckl.api.superscript.script;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.superscript.util.WarningHelper;

public class ParameterWrapper<T> {

	private final ParameterType<T> type;
	private final int minNum;
	private final int maxNum;
	private final boolean varArgs;

	protected ParameterWrapper(final ParameterType<T> type, final int minNum, final int maxNum, final boolean varArgs) {
		this.type = type;
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.varArgs = varArgs;
	}

	public Pair<T[], String[]> parseArgs(final ScriptHandler handler, String ... args) throws Exception{
		final List<T> parsed = Lists.newArrayList();
		for(int i = 0; ; i++){
			T obj;
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
		return Pair.of(parsed.toArray(WarningHelper.uncheckedCast(Array.newInstance(this.type.getTypeClass(), parsed.size()))), args);
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

	public ParameterType<T> getType() {
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

	public static <T> Builder<T> builder(){
		return new Builder<T>();
	}

	public static class Builder<T>{

		private ParameterType<T> type;
		private int minNum;
		private int maxNum;
		private boolean varArgs;

		private Builder(){}

		public ParameterType<?> type() {
			return this.type;
		}

		public Builder<T> type(final ParameterType<T> type) {
			this.type = type;
			return this;
		}

		public int minNum() {
			return this.minNum;
		}

		public Builder<T> minNum(final int minNum) {
			this.minNum = minNum;
			return this;
		}

		public int maxNum() {
			return this.maxNum;
		}

		public Builder<T> maxNum(final int maxNum) {
			this.maxNum = maxNum;
			return this;
		}

		public boolean varArgs() {
			return this.varArgs;
		}

		public Builder<T> varArgs(final boolean varArgs) {
			this.varArgs = varArgs;
			return this;
		}

		public ParameterWrapper<T> build(){
			return new ParameterWrapper<T>(this.type, this.minNum, this.maxNum, this.varArgs);
		}

	}

}
