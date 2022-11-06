package me.superckl.biometweaker.script.command.effects;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.BiomeLookup;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.superscript.AutoRegister;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.BiomeModificationManager.MobEffectModification;
import me.superckl.biometweaker.script.command.StagedScriptCommand;
import me.superckl.biometweaker.script.object.BiomesScriptObject;
import me.superckl.biometweaker.script.object.TweakerScriptObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

@RequiredArgsConstructor
@AutoRegister(classes = {BiomesScriptObject.class, TweakerScriptObject.class}, name = "addMobEffect")
public class ScriptCommandAddPotionEffect extends StagedScriptCommand{

	private final BiomePackage pack;
	private final ResourceLocation entityLocation;
	private final MobEffectModification.Builder effect;

	@Override
	public void perform() throws Exception {
		if(!ForgeRegistries.ENTITY_TYPES.containsKey(this.entityLocation))
			throw new IllegalArgumentException(String.format("No entity type %s found", this.entityLocation));
		final EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(this.entityLocation);
		this.pack.locIterator(BiomeLookup.fromForge()).forEachRemaining(loc -> BiomeModificationManager.forBiome(loc).addMobEffect(type, this.effect.build()));
	}

	@Override
	public StageRequirement requiredStage() {
		return StageRequirement.LATE;
	}

}
