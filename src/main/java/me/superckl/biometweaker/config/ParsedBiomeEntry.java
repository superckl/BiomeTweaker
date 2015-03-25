package me.superckl.biometweaker.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;

import com.google.gson.JsonElement;

@Getter
public class ParsedBiomeEntry {

	private final int biomeID;
	private final Map<String, JsonElement> mappings = new HashMap<String, JsonElement>();
	/*private boolean hasBiomeName;
	private String biomeName;
	private boolean hasFoliageColor;
	private int foliageColor;
	private boolean hasRootHeight;
	private float rootHeight;
	private float heightVariation;
	private boolean hasHeightVariation;
	private String topBlock;
	private boolean hasTopBlock;
	private String fillerBlock;
	private boolean hasFillerBlock;
	private float temperature;
	private boolean hasTemperature;
	private float humidity;
	private boolean hasHumidity;
	private int waterTint;
	private boolean hasWaterTint;
	private boolean enableRain;
	private boolean hasEnableRain;
	private boolean enableSnow;
	private boolean hasEnableSnow;
	private List<BiomeSpawnEntry> spawnableCreatures = new ArrayList<BiomeSpawnEntry>();
	private boolean hasSpawnableCreatures;
	private List<BiomeSpawnEntry> spawnableMonsters = new ArrayList<BiomeSpawnEntry>();
	private boolean hasSpawnableMonsters;
	private List<BiomeSpawnEntry> spawnableWaterCreatures = new ArrayList<BiomeSpawnEntry>();
	private boolean hasSpawnableWaterCreatures;
	private List<BiomeSpawnEntry> spawnableCaveCreatures = new ArrayList<BiomeSpawnEntry>();
	private boolean hasSpawnableCaveCreatures;*/

	public ParsedBiomeEntry(final int biomeID) {
		this.biomeID = biomeID;
	}

	public void overwriteWith(final ParsedBiomeEntry entry){
		if(entry.biomeID != this.biomeID)
			return;
		for(final Entry<String, JsonElement> ent:entry.getMappings().entrySet())
			this.addMapping(ent.getKey(), ent.getValue());
	}

	public void addMapping(final String key, final JsonElement element){
		if(this.mappings.containsKey(key))
			ModBiomeTweakerCore.logger.warn("Biome ID "+this.biomeID+": Duplicate mapping detected for key: "+key+", with element: "+element.toString());
		this.mappings.put(key, element);
	}

	public void removeMapping(final String key){
		this.mappings.remove(key);
	}

	/*public void setBiomeName(String biomeName) {
		this.biomeName = biomeName;
		this.hasBiomeName = true;
	}

	public void setFoliageColor(int foliageColor) {
		this.foliageColor = foliageColor;
		this.hasFoliageColor = true;
	}

	public void setRootHeight(float rootHeight) {
		this.rootHeight = rootHeight;
		this.hasRootHeight = true;
	}

	public void setHeighTVariation(float heightVariation) {
		this.heightVariation = heightVariation;
		this.hasHeightVariation = true;
	}

	public void setTopBlock(String topBlock) {
		this.topBlock = topBlock;
		this.hasTopBlock = true;
	}

	public void setFillerBlock(String fillerBlock) {
		this.fillerBlock = fillerBlock;
		this.hasFillerBlock = true;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
		this.hasTemperature = true;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
		this.hasHumidity = true;
	}

	public void setWaterTint(int waterTint) {
		this.waterTint = waterTint;
		this.hasWaterTint = true;
	}

	public void setEnableRain(boolean enableRain) {
		this.enableRain = enableRain;
		this.hasEnableRain = true;
	}

	public void setEnableSnow(boolean enableSnow) {
		this.enableSnow = enableSnow;
		this.hasEnableSnow = true;
	}

	public void setSpawnableCreatures(List<BiomeSpawnEntry> spawnableCreatures) {
		this.spawnableCreatures = spawnableCreatures;
		this.hasSpawnableCreatures = true;
	}

	public void addSpawnableCreaturesEntry(BiomeSpawnEntry entry){
		this.spawnableCreatures.add(entry);
	}

	public void setSpawnableMonsters(List<BiomeSpawnEntry> spawnableMonsters) {
		this.spawnableMonsters = spawnableMonsters;
		this.hasSpawnableMonsters = true;
	}

	public void addSpawnableMonstersEntry(BiomeSpawnEntry entry){
		this.spawnableMonsters.add(entry);
	}

	public void setSpawnableWaterCreatures(
			List<BiomeSpawnEntry> spawnableWaterCreatures) {
		this.spawnableWaterCreatures = spawnableWaterCreatures;
		this.hasSpawnableWaterCreatures = true;
	}

	public void addSpawnableWaterCreaturesEntry(BiomeSpawnEntry entry){
		this.spawnableWaterCreatures.add(entry);
	}

	public void setSpawnableCaveCreatures(
			List<BiomeSpawnEntry> spawnableCaveCreatures) {
		this.spawnableCaveCreatures = spawnableCaveCreatures;
		this.hasSpawnableCaveCreatures = true;
	}

	public void addSpawnableCaveCreaturesEntry(BiomeSpawnEntry entry){
		this.spawnableCaveCreatures.add(entry);
	}*/

	@AllArgsConstructor
	@Getter
	public static class BiomeSpawnEntry{

		private final String entityClass;
		private final int weight;
		private final int minGroupSize;
		private final int maxGroupsize;
		private final boolean add;

	}

}
