package me.superckl.biometweaker.script.command.world.gen;

import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.BiomeTweakerAPI;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

@RequiredArgsConstructor
@AutoRegister(classes = TweakerScriptObject.class, name = "registerCarver")
public class ScriptCommandRegisterCarver extends StagedScriptCommand{

	private final String name;

	@Override
	public void perform() throws Exception {
		final File jsonFile = new File(BiomeTweaker.getINSTANCE().getCarverDir(), this.name+".json");
		if(!jsonFile.isFile())
			throw new IllegalArgumentException("Failed to find carver file "+jsonFile);
		try (FileReader reader = new FileReader(jsonFile)){
			final JsonElement el = JsonParser.parseReader(reader);
			final DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());

			final ConfiguredWorldCarver<?> feature = ScriptCommandRegisterFeature.decode(ops, el, ConfiguredWorldCarver.DIRECT_CODEC);
			final ResourceLocation key = new ResourceLocation(BiomeTweakerAPI.MOD_ID, this.name);
			BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, key, feature);
		} catch (final Exception e) {
			BiomeTweaker.LOG.error("Failed to register carver "+this.name, e);
		}
	}

	@Override
	public StageRequirement requiredStage() {
		return StageRequirement.LATE;
	}

}
