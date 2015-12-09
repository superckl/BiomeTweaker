package me.superckl.api.biometweaker.event;

import com.google.gson.JsonElement;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public abstract class BiomeTweakEvent extends Event{

	private final IScriptCommand command;
	private final BiomeGenBase biome;

	public BiomeTweakEvent(final IScriptCommand command, final BiomeGenBase biome) {
		this.command = command;
		this.biome = biome;
	}

	public IScriptCommand getCommand() {
		return this.command;
	}

	public BiomeGenBase getBiome() {
		return this.biome;
	}

	@Cancelable
	public static class Create extends BiomeTweakEvent{

		public Create(final IScriptCommand command, final BiomeGenBase biome) {
			super(command, biome);
		}

	}

	@Cancelable
	public static class Remove extends BiomeTweakEvent{

		private final BiomeEntry entry;

		public Remove(final IScriptCommand command, final BiomeGenBase biome, final BiomeEntry entry) {
			super(command, biome);
			this.entry = entry;
		}

		public BiomeEntry getEntry() {
			return this.entry;
		}

	}

	/**
	 * Note: The corresponding BiomeGenBase may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	public static class RemoveDecoration extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveDecoration(final IScriptCommand command, final BiomeGenBase biome, final int biomeID, final String type) {
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
	 * Note: The corresponding BiomeGenBase may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	public static class RemoveFeature extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveFeature(final IScriptCommand command, final BiomeGenBase biome, final int biomeID, final String type) {
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

		public AddDictionaryType(final IScriptCommand command, final BiomeGenBase biome, final BiomeDictionary.Type type) {
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

		public RemoveDictionaryType(final IScriptCommand command, final BiomeGenBase biome, final BiomeDictionary.Type type) {
			super(command, biome);
			this.type = type;
		}

		public BiomeDictionary.Type getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class RemoveAllDictionaryTypes extends BiomeTweakEvent{

		public RemoveAllDictionaryTypes(final IScriptCommand command, final BiomeGenBase biome) {
			super(command, biome);
		}

	}

	@Cancelable
	public static class RemoveSpawn extends BiomeTweakEvent{

		private final SpawnListType type;
		private final Class<?> entityClass;

		public RemoveSpawn(final IScriptCommand command, final BiomeGenBase biome, final SpawnListType type, final Class<?> entityClass) {
			super(command, biome);
			this.type = type;
			this.entityClass = entityClass;
		}

		public SpawnListType getType() {
			return this.type;
		}

		public Class<?> getEntityClass() {
			return this.entityClass;
		}

	}

	@Cancelable
	public static class RemoveAllSpawns extends BiomeTweakEvent{

		private final SpawnListType type;

		public RemoveAllSpawns(final IScriptCommand command, final BiomeGenBase biome, final SpawnListType type) {
			super(command, biome);
			this.type = type;
		}

		public SpawnListType getType() {
			return this.type;
		}

	}

	@Cancelable
	public static class AddSpawn extends BiomeTweakEvent{

		private final SpawnListEntry spawnEntry;

		public AddSpawn(final IScriptCommand command, final BiomeGenBase biome, final SpawnListEntry spawnEntry) {
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

		public SetProperty(final IScriptCommand command, final BiomeGenBase biome, final String property, final JsonElement value) {
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

		private final Block block;
		private final int metdata;
		private final int weight;

		public AddFlower(final IScriptCommand command, final BiomeGenBase biome, final Block block, final int meta, final int weight) {
			super(command, biome);
			this.block = block;
			this.metdata = meta;
			this.weight = weight;
		}

		public Block getBlock() {
			return this.block;
		}

		public int getMetdata() {
			return this.metdata;
		}

		public int getWeight() {
			return this.weight;
		}

	}

	@Cancelable
	public static class RemoveFlower extends BiomeTweakEvent{

		private final Block block;
		private final int metdata;

		public RemoveFlower(final IScriptCommand command, final BiomeGenBase biome, final Block block, final int meta) {
			super(command, biome);
			this.block = block;
			this.metdata = meta;
		}

		public Block getBlock() {
			return this.block;
		}

		public int getMetdata() {
			return this.metdata;
		}

	}

	@Cancelable
	public static class RegisterGenBlockReplacement extends BiomeTweakEvent{

		private final int weight;
		private final Block toReplace;
		private final Integer toReplaceMeta;
		private final Block replaceWith;
		private final Integer replaceWithMeta;

		public RegisterGenBlockReplacement(final IScriptCommand command, final int weight, final BiomeGenBase biome, final Block toReplace, final Integer toReplaceMeta, final Block replaceWith, final Integer replaceWithMeta) {
			super(command, biome);
			this.weight = weight;
			this.toReplace = toReplace;
			this.toReplaceMeta = toReplaceMeta;
			this.replaceWith = replaceWith;
			this.replaceWithMeta = replaceWithMeta;
		}

		public int getWeight() {
			return this.weight;
		}

		public Block getToReplace() {
			return this.toReplace;
		}

		public Integer getToReplaceMeta() {
			return this.toReplaceMeta;
		}

		public Block getReplaceWith() {
			return this.replaceWith;
		}

		public Integer getReplaceWithMeta() {
			return this.replaceWithMeta;
		}

	}

}
