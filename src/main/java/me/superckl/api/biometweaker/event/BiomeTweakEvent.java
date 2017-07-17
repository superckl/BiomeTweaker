package me.superckl.api.biometweaker.event;

import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BiomeTweakEvent extends Event{

	private final IScriptCommand command;
	private final Biome biome;

	public BiomeTweakEvent(final IScriptCommand command, final Biome biome) {
		this.command = command;
		this.biome = biome;
	}

	public IScriptCommand getCommand() {
		return this.command;
	}

	public Biome getBiome() {
		return this.biome;
	}

	@Cancelable
	public static class Create extends BiomeTweakEvent{

		public Create(final IScriptCommand command, final Biome biome) {
			super(command, biome);
		}

	}

	@Cancelable
	public static class AddToGeneration extends BiomeTweakEvent{

		private final BiomeEntry entry;

		public AddToGeneration(final IScriptCommand command, final Biome biome, final BiomeEntry entry) {
			super(command, biome);
			this.entry = entry;
		}

		public BiomeEntry getEntry() {
			return this.entry;
		}
	}

	@Cancelable
	public static class Remove extends BiomeTweakEvent{

		private final BiomeEntry entry;

		public Remove(final IScriptCommand command, final Biome biome, final BiomeEntry entry) {
			super(command, biome);
			this.entry = entry;
		}

		public BiomeEntry getEntry() {
			return this.entry;
		}

	}

	/**
	 * Note: The corresponding Biome may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	public static class RemoveDecoration extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveDecoration(final IScriptCommand command, final Biome biome, final int biomeID, final String type) {
			super(command, biome);
			this.biomeID = biomeID;
			this.type = type;
		}

		public int getBiomeID() {
			return this.biomeID;
		}

		public String getType() {
			return this.type;
		}

	}

	/**
	 * Note: The corresponding Biome may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	public static class RemoveFeature extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveFeature(final IScriptCommand command, final Biome biome, final int biomeID, final String type) {
			super(command, biome);
			this.biomeID = biomeID;
			this.type = type;
		}

		public int getBiomeID() {
			return this.biomeID;
		}

		public String getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class AddDictionaryType extends BiomeTweakEvent{

		private final BiomeDictionary.Type type;

		public AddDictionaryType(final IScriptCommand command, final Biome biome, final BiomeDictionary.Type type) {
			super(command, biome);
			this.type = type;
		}

		public BiomeDictionary.Type getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class RemoveDictionaryType extends BiomeTweakEvent{

		private final BiomeDictionary.Type type;

		public RemoveDictionaryType(final IScriptCommand command, final Biome biome, final BiomeDictionary.Type type) {
			super(command, biome);
			this.type = type;
		}

		public BiomeDictionary.Type getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class RemoveAllDictionaryTypes extends BiomeTweakEvent{

		public RemoveAllDictionaryTypes(final IScriptCommand command, final Biome biome) {
			super(command, biome);
		}

	}

	@Cancelable
	public static class RemoveSpawn extends BiomeTweakEvent{

		private final EnumCreatureType type;
		private final Class<?> entityClass;

		public RemoveSpawn(final IScriptCommand command, final Biome biome, final EnumCreatureType type2, final Class<?> entityClass) {
			super(command, biome);
			this.type = type2;
			this.entityClass = entityClass;
		}

		public EnumCreatureType getType() {
			return this.type;
		}

		public Class<?> getEntityClass() {
			return this.entityClass;
		}

	}

	@Cancelable
	public static class RemoveAllSpawns extends BiomeTweakEvent{

		private final EnumCreatureType type;

		public RemoveAllSpawns(final IScriptCommand command, final Biome biome, final EnumCreatureType type) {
			super(command, biome);
			this.type = type;
		}

		public EnumCreatureType getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class AddSpawn extends BiomeTweakEvent{

		private final SpawnListEntry spawnEntry;

		public AddSpawn(final IScriptCommand command, final Biome biome, final SpawnListEntry spawnEntry) {
			super(command, biome);
			this.spawnEntry = spawnEntry;
		}

		public SpawnListEntry getSpawnEntry() {
			return this.spawnEntry;
		}

	}

	@Cancelable
	public static class SetProperty extends BiomeTweakEvent{

		private final String property;
		private final JsonElement value;

		public SetProperty(final IScriptCommand command, final Biome biome, final String property, final JsonElement value) {
			super(command, biome);
			this.property = property;
			this.value = value;
		}

		public String getProperty() {
			return this.property;
		}

		public JsonElement getValue() {
			return this.value;
		}

	}

	@Cancelable
	public static class AddFlower extends BiomeTweakEvent{

		private final BlockStateBuilder<?> block;
		private final int weight;

		public AddFlower(final IScriptCommand command, final Biome biome, final BlockStateBuilder<?> block, final int weight) {
			super(command, biome);
			this.block = block;
			this.weight = weight;
		}

		public BlockStateBuilder<?> getBlock() {
			return this.block;
		}

		public int getWeight() {
			return this.weight;
		}

	}

	@Cancelable
	public static class RemoveFlower extends BiomeTweakEvent{

		private final BlockStateBuilder<?> block;

		public RemoveFlower(final IScriptCommand command, final Biome biome, final BlockStateBuilder<?> block) {
			super(command, biome);
			this.block = block;
		}

		public BlockStateBuilder<?> getBlock() {
			return this.block;
		}

	}

	@Cancelable
	public static class RegisterGenBlockReplacement extends BiomeTweakEvent{

		private final int weight;
		private final BlockStateBuilder<?> toReplace;
		private final BlockStateBuilder<?> replaceWith;

		public RegisterGenBlockReplacement(final IScriptCommand command, final int weight, final Biome biome, final BlockStateBuilder<?> toReplace, final BlockStateBuilder<?> replaceWith) {
			super(command, biome);
			this.weight = weight;
			this.toReplace = toReplace;
			this.replaceWith = replaceWith;
		}

		public int getWeight() {
			return this.weight;
		}

		public BlockStateBuilder<?> getToReplace() {
			return this.toReplace;
		}

		public BlockStateBuilder<?> getReplaceWith() {
			return this.replaceWith;
		}

	}

}
