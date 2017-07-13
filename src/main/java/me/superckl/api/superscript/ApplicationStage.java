package me.superckl.api.superscript;

import net.minecraftforge.common.util.EnumHelper;

public enum ApplicationStage{
	PRE_INIT, INIT, POST_INIT, FINISHED_LOAD, SERVER_STARTING, SERVER_STARTED;

	public static ApplicationStage newStage(final String name){
		return EnumHelper.addEnum(ApplicationStage.class, name, new Class[] {});
	}

}