package me.superckl.api.superscript;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import me.superckl.api.superscript.command.IScriptCommand;

public class BasicScriptCommandManager implements IScriptCommandManager{

	private static ApplicationStage defaultStage = ApplicationStage.FINISHED_LOAD;

	private ApplicationStage currentStage = BasicScriptCommandManager.defaultStage;

	private final Map<ApplicationStage, List<IScriptCommand>> commands = Maps.newEnumMap(ApplicationStage.class);
	private final Set<ApplicationStage> appliedStages = EnumSet.noneOf(ApplicationStage.class);

	public boolean addCommand(final IScriptCommand command){
		return this.addCommand(command, this.getCurrentStage());
	}

	@Override
	public boolean addCommand(final IScriptCommand command, final ApplicationStage stage) {
		if(!this.commands.containsKey(stage))
			this.commands.put(stage, new ArrayList<IScriptCommand>());
		if(this.appliedStages.contains(stage))
			try {
				command.perform();
			} catch (final Exception e) {
				APIInfo.log.error("Failed to execute script command: "+command);
				e.printStackTrace();
			}
		return this.commands.get(stage).add(command);
	}

	@Override
	public void applyCommandsFor(final ApplicationStage stage){
		if(!this.commands.containsKey(stage))
			return;
		this.appliedStages.add(stage);
		final List<IScriptCommand> commands = this.commands.get(stage);
		APIInfo.log.info("Found "+commands.size()+" tweak"+(commands.size() > 1 ? "s":"")+" to apply for stage "+stage.toString()+". Applying...");
		for(final IScriptCommand command:commands)
			try{
				command.perform();
			}catch(final Exception e){
				APIInfo.log.error("Failed to execute script command: "+command);
				e.printStackTrace();
			}
	}

	@Override
	public void reset(){
		this.commands.clear();
		this.currentStage = BasicScriptCommandManager.defaultStage;
	}

	public ApplicationStage getCurrentStage() {
		return this.currentStage;
	}

	public void setCurrentStage(final ApplicationStage currentStage) {
		this.currentStage = currentStage;
	}

	public Map<ApplicationStage, List<IScriptCommand>> getCommands() {
		return this.commands;
	}

	public Set<ApplicationStage> getAppliedStages() {
		return this.appliedStages;
	}

	public static ApplicationStage getDefaultStage() {
		return BasicScriptCommandManager.defaultStage;
	}

	public static void setDefaultStage(final ApplicationStage defaultStage) {
		BasicScriptCommandManager.defaultStage = defaultStage;
	}

}
