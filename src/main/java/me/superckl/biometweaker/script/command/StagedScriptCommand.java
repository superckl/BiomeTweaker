package me.superckl.biometweaker.script.command;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Getter;
import me.superckl.api.superscript.ApplicationStage;
import me.superckl.api.superscript.script.command.ScriptCommand;

public abstract class StagedScriptCommand extends ScriptCommand{

	public StageRequirement requiredStage() {
		return StageRequirement.NONE;
	}

	public enum StageRequirement{
		NONE(ApplicationStage.CONSTRUCTION, ApplicationStage.SETUP, ApplicationStage.LOAD_COMPLETE),
		EARLY(ApplicationStage.CONSTRUCTION),
		LATE(ApplicationStage.SETUP, ApplicationStage.LOAD_COMPLETE);

		@Getter
		private final Set<ApplicationStage> validStages;

		StageRequirement(final ApplicationStage stage, final ApplicationStage... stages) {
			this.validStages = Sets.immutableEnumSet(stage, stages);
		}

		public boolean isValid(final ApplicationStage stage) {
			return this.validStages.contains(stage);
		}
	}

}
