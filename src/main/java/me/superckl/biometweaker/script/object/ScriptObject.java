package me.superckl.biometweaker.script.object;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.command.ScriptCommandListing;

public abstract class  ScriptObject {

	@Getter
	protected final Map<String, ScriptCommandListing> validCommands = new HashMap<String, ScriptCommandListing>();

	public ScriptObject() {
		try {
			this.populateCommands();
		} catch (final Exception e) {
			ModBiomeTweakerCore.logger.error("Failed to populate command listings! Some tweaks may not be applied.");
			e.printStackTrace();
		}
	}

	public abstract void handleCall(final String call, final ScriptHandler handler) throws Exception;
	public abstract void populateCommands() throws Exception;

}
