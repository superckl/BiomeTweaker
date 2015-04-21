package me.superckl.biometweaker.script.util;

import lombok.Getter;
import lombok.Setter;
import me.superckl.biometweaker.script.command.IScriptCommand;

public class ScriptCommandListing extends ScriptListing<IScriptCommand>{

	@Getter
	@Setter
	private boolean performInst;

}
