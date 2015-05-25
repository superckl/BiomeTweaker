package me.superckl.biometweaker.script.util;

import lombok.Getter;
import lombok.Setter;
import me.superckl.biometweaker.script.command.IScriptCommand;

/**
 * A subclass of ConstructorListing that allows tracking of multiple possible parameter lists for the same command name.
 */
public class ScriptCommandListing extends ConstructorListing<IScriptCommand>{

	@Getter
	@Setter
	private boolean performInst;

}
