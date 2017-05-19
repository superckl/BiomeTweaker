package me.superckl.biometweaker.common.world;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

public class TweakWorldManager {

	@Nullable
	@Getter
	private static final Integer defaultWorld = null;
	@Nullable
	@Getter
	@Setter
	private static Integer currentWorld = TweakWorldManager.defaultWorld;

	private TweakWorldManager(){

	}

}
