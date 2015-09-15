package me.superckl.api.superscript;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraftforge.common.util.EnumHelper;

import com.google.common.collect.Maps;

public class ScriptCommandManager {

	public static enum ApplicationStage{
		PRE_INIT, INIT, POST_INIT, FINISHED_LOAD, SERVER_STARTING, SERVER_STARTED;
		
		public static ApplicationStage newStage(String name){
			return EnumHelper.addEnum(ApplicationStage.class, name);
		}
		
	}

	private static Map<String, ScriptCommandManager> instances = Maps.newHashMap();
	
	protected ScriptCommandManager() {}

	private ApplicationStage currentStage = ApplicationStage.FINISHED_LOAD;

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
		this.currentStage = ApplicationStage.FINISHED_LOAD;
	}
	
	public static ScriptCommandManager newInstance(String owner){
		ScriptCommandManager manager = new ScriptCommandManager();
		instances.put(owner, manager);
		return manager;
	}
	
	public static ScriptCommandManager getManagerFor(String owner){
		return instances.get(owner);
	}

	public ApplicationStage getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(ApplicationStage currentStage) {
		this.currentStage = currentStage;
	}

	public Map<ApplicationStage, List<IScriptCommand>> getCommands() {
		return commands;
	}

	public Set<ApplicationStage> getAppliedStages() {
		return appliedStages;
	}

}
