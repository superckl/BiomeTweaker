package me.superckl.api.superscript;

import me.superckl.api.superscript.command.IScriptCommand;

public interface IScriptCommandManager {

	public boolean addCommand(final IScriptCommand command, ApplicationStage stage);

	public void applyCommandsFor(final ApplicationStage stage);

	public void reset();

}
