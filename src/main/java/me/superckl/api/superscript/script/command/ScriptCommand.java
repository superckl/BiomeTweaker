package me.superckl.api.superscript.script.command;

import me.superckl.api.superscript.script.ScriptHandler;

public abstract class ScriptCommand {

	protected ScriptHandler scriptHandler;

	public abstract void perform() throws Exception;

	public void setScriptHandler(final ScriptHandler handler) {
		this.scriptHandler = handler;
	}


}
