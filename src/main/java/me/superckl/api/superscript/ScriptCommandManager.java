package me.superckl.api.superscript;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraftforge.common.util.EnumHelper;

public class ScriptCommandManager {

	public static enum ApplicationStage{
		PRE_INIT, INIT, POST_INIT, FINISHED_LOAD, SERVER_STARTING, SERVER_STARTED;

		public static ApplicationStage newStage(final String name){
			return EnumHelper.addEnum(ApplicationStage.class, name);
		}

	}

	private static Map<String, ScriptCommandManager> instances = Maps.newHashMap();
	private static ApplicationStage defaultStage = ApplicationStage.FINISHED_LOAD;

	protected ScriptCommandManager() {}

	private ApplicationStage currentStage = ScriptCommandManager.defaultStage;

	private final Map<ApplicationStage, List<IScriptCommand>> commands = Maps.newEnumMap(ApplicationStage.class);
	private final Set<ApplicationStage> appliedStages = EnumSet.noneOf(ApplicationStage.class);

	public boolean addCommand(final IScriptCommand command){
		if(!this.commands.containsKey(this.currentStage))
			this.commands.put(this.currentStage, new ArrayList<IScriptCommand>());
		if(this.appliedStages.contains(this.currentStage))
			try {
				command.perform();
			} catch (final Exception e) {
				APIInfo.log.error("Failed to execute script command: "+command);
				e.printStackTrace();
			}
		return this.commands.get(this.currentStage).add(command);
	}

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

	public void reset(){
		this.commands.clear();
		this.currentStage = ScriptCommandManager.defaultStage;
	}

	public static ScriptCommandManager newInstance(final String owner){
		final ScriptCommandManager manager = new ScriptCommandManager();
		ScriptCommandManager.instances.put(owner, manager);
		return manager;
	}

	public static ScriptCommandManager getManagerFor(final String owner){
		return ScriptCommandManager.instances.get(owner);
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
		return ScriptCommandManager.defaultStage;
	}

	public static void setDefaultStage(final ApplicationStage defaultStage) {
		ScriptCommandManager.defaultStage = defaultStage;
	}

}
