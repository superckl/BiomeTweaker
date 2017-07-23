package me.superckl.api.superscript.script.command;

import me.superckl.api.superscript.ApplicationStage;

public abstract class ScriptCommandManager {

	public abstract boolean addCommand(final ScriptCommand command, ApplicationStage stage);

	public abstract void applyCommandsFor(final ApplicationStage stage);

	public abstract void reset();

}
