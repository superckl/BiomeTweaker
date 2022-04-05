package me.superckl.biometweaker.script.command.misc;

import lombok.RequiredArgsConstructor;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.TweakWorldManager;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;

@AutoRegister(classes = TweakerScriptObject.class, name = "setWorld")
@RequiredArgsConstructor
public class ScriptCommandSetWorld extends ScriptCommand{

	private final ResourceLocation dim;

	public ScriptCommandSetWorld() {
		this(null);
	}

	@Override
	public void perform() throws Exception {
		TweakWorldManager.setCurrentWorld(this.dim == null ? null : this.dim);
	}

}
