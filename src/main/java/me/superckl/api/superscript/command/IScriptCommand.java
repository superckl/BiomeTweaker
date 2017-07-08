package me.superckl.api.superscript.command;

import me.superckl.api.superscript.script.ScriptHandler;

public interface IScriptCommand {

	public void perform() throws Exception;
	default void setScriptHandler(final ScriptHandler handler) {}

}
