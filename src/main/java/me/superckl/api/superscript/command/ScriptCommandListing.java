package me.superckl.api.superscript.command;

import lombok.Getter;
import lombok.Setter;
import me.superckl.api.superscript.util.ConstructorListing;

/**
 * A subclass of ConstructorListing that allows tracking of multiple possible parameter lists for the same command name.
 */
public class ScriptCommandListing extends ConstructorListing<IScriptCommand>{

	@Getter
	@Setter
	private boolean performInst;

}
