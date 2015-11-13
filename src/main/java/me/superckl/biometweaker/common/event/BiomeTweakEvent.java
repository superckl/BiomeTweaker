package me.superckl.biometweaker.common.event;

import com.google.gson.JsonElement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.command.IScriptCommand;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@RequiredArgsConstructor
@Getter
public abstract class BiomeTweakEvent extends Event{

	private final IScriptCommand command;
	private final BiomeGenBase biome;

	@Cancelable
	public static class Create extends BiomeTweakEvent{

		public Create(final IScriptCommand command, final BiomeGenBase biome) {
			super(command, biome);
		}

	}

	@Cancelable
	@Getter
	public static class Remove extends BiomeTweakEvent{

		private final BiomeEntry entry;

		public Remove(final IScriptCommand command, final BiomeGenBase biome, final BiomeEntry entry) {
			super(command, biome);
			this.entry = entry;
		}

	}

	/**
	 * Note: The corresponding BiomeGenBase may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	@Getter
	public static class RemoveDecoration extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveDecoration(final IScriptCommand command, final BiomeGenBase biome, final int biomeID, final String type) {
			super(command, biome);
			this.biomeID = biomeID;
			this.type = type;
		}

	}

	/**
	 * Note: The corresponding BiomeGenBase may be null for this event! The biomeID is given for that case.
	 */
	@Cancelable
	@Getter
	public static class RemoveFeature extends BiomeTweakEvent{

		private final int biomeID;
		private final String type;

		public RemoveFeature(final IScriptCommand command, final BiomeGenBase biome, final int biomeID, final String type) {
			super(command, biome);
			this.biomeID = biomeID;
			this.type = type;
		}

	}

	@Cancelable
	@Getter
	public static class AddDictionaryType extends BiomeTweakEvent{

		private final BiomeDictionary.Type type;

		public AddDictionaryType(final IScriptCommand command, final BiomeGenBase biome, final BiomeDictionary.Type type) {
			super(command, biome);
			this.type = type;
		}

	}

	@Cancelable
	@Getter
	public static class RemoveDictionaryType extends BiomeTweakEvent{

		private final BiomeDictionary.Type type;

		public RemoveDictionaryType(final IScriptCommand command, final BiomeGenBase biome, final BiomeDictionary.Type type) {
			super(command, biome);
			this.type = type;
		}

	}

	@Cancelable
	public static class RemoveAllDictionaryTypes extends BiomeTweakEvent{

		public RemoveAllDictionaryTypes(final IScriptCommand command, final BiomeGenBase biome) {
			super(command, biome);
		}

	}

	@Cancelable
	@Getter
	public static class RemoveSpawn extends BiomeTweakEvent{

		private final SpawnListType type;
		private final Class<?> entityClass;

		public RemoveSpawn(final IScriptCommand command, final BiomeGenBase biome, final SpawnListType type, final Class<?> entityClass) {
			super(command, biome);
			this.type = type;
			this.entityClass = entityClass;
		}

	}

	@Cancelable
	@Getter
	public static class RemoveAllSpawns extends BiomeTweakEvent{

		private final SpawnListType type;

		public RemoveAllSpawns(final IScriptCommand command, final BiomeGenBase biome, final SpawnListType type) {
			super(command, biome);
			this.type = type;
		}

	}

	@Cancelable
	@Getter
	public static class AddSpawn extends BiomeTweakEvent{

		private final SpawnListEntry spawnEntry;

		public AddSpawn(final IScriptCommand command, final BiomeGenBase biome, final SpawnListEntry spawnEntry) {
			super(command, biome);
			this.spawnEntry = spawnEntry;
		}

	}

	@Cancelable
	@Getter
	public static class SetProperty extends BiomeTweakEvent{

		private final String property;
		private final JsonElement value;

		public SetProperty(final IScriptCommand command, final BiomeGenBase biome, final String property, final JsonElement value) {
			super(command, biome);
			this.property = property;
			this.value = value;
		}

	}

	@Cancelable
	@Getter
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

	}

	@Cancelable
	@Getter
	public static class RemoveFlower extends BiomeTweakEvent{

		private final Block block;
		private final int metdata;

		public RemoveFlower(final IScriptCommand command, final BiomeGenBase biome, final Block block, final int meta) {
			super(command, biome);
			this.block = block;
			this.metdata = meta;
		}

	}

	@Cancelable
	@Getter
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

	}

}
