package me.superckl.api.superscript.script.command;

import me.superckl.api.superscript.script.ScriptHandler;

public abstract class ScriptCommand {

	protected ScriptHandler scriptHandler;

	public abstract void perform() throws Exception;
	
	public boolean performInst() {
		return false;
	}

	public void setScriptHandler(final ScriptHandler handler) {
		this.scriptHandler = handler;
	}


}
