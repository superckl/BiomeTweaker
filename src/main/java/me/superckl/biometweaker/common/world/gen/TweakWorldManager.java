package me.superckl.biometweaker.common.world.gen;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

public class TweakWorldManager {

	@Nullable
	@Getter
	private static final ResourceLocation defaultWorld = null;
	@Nullable
	@Getter
	@Setter
	private static ResourceLocation currentWorld = TweakWorldManager.defaultWorld;

	private TweakWorldManager(){

	}

}
