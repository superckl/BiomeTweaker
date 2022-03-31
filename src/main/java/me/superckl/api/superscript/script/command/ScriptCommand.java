package me.superckl.api.superscript.script.command;

import lombok.Getter;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.ScriptParser.ScriptContext;

public abstract class ScriptCommand {

	protected ScriptHandler scriptHandler;

	@Getter
	private ScriptContext context;

	public abstract void perform() throws Exception;

	public boolean performInst() {
		return false;
	}

	public void setScriptHandler(final ScriptHandler handler) {
		this.scriptHandler = handler;
	}

	@Override
	public String toString() {
		String name;
		final Class<? extends ScriptCommand> clazz = this.getClass();
		if(clazz.getAnnotation(AutoRegister.class) != null)
			name = clazz.getAnnotation(AutoRegister.class).name();
		else
			name = this.getClass().getSimpleName();
		if(this.context != null)
			return name+" @ "+this.context.toString();
		return name;
	}

	public void setContext(final ScriptContext context) {
		if(this.context != null)
			throw new IllegalStateException("Context has already been set!");
		this.context = context;
	}

}
