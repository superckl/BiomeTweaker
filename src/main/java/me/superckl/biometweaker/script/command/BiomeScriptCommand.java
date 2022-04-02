package me.superckl.biometweaker.script.command;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Getter;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.command.ScriptCommand;

public abstract class BiomeScriptCommand extends ScriptCommand{

	public BIOME_REQUIREMENT requiredStage() {
		return BIOME_REQUIREMENT.NONE;
	}

	public enum BIOME_REQUIREMENT{
		NONE(ApplicationStage.CONSTRUCTION, ApplicationStage.SETUP, ApplicationStage.LOAD_COMPLETE),
		EARLY(ApplicationStage.CONSTRUCTION),
		LATE(ApplicationStage.SETUP, ApplicationStage.LOAD_COMPLETE);

		@Getter
		private final Set<ApplicationStage> validStages;

		BIOME_REQUIREMENT(final ApplicationStage stage, final ApplicationStage... stages) {
			this.validStages = Sets.immutableEnumSet(stage, stages);
		}

		public boolean isValid(final ApplicationStage stage) {
			return this.validStages.contains(stage);
		}
	}

}
