package me.superckl.biometweaker.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeColorMappings {

	private static Map<String, Integer> defaultColors;

	/* These are the biome colors used by default in the Amidst program. */
	static
	{
		BiomeColorMappings.defaultColors = new HashMap<>();

		BiomeColorMappings.defaultColors.put("minecraft:ocean",                            0x000070);
		BiomeColorMappings.defaultColors.put("minecraft:plains",                           0x8DB360);
		BiomeColorMappings.defaultColors.put("minecraft:desert",                           0xFA9418);
		BiomeColorMappings.defaultColors.put("minecraft:extreme_hills",                    0x606060);
		BiomeColorMappings.defaultColors.put("minecraft:forest",                           0x056621);
		BiomeColorMappings.defaultColors.put("minecraft:taiga",                            0x0B6659);
		BiomeColorMappings.defaultColors.put("minecraft:swampland",                        0x07F9B2);
		BiomeColorMappings.defaultColors.put("minecraft:river",                            0x0000FF);
		BiomeColorMappings.defaultColors.put("minecraft:hell",                             0xFF0000);
		BiomeColorMappings.defaultColors.put("minecraft:sky",                              0x8080FF);
		BiomeColorMappings.defaultColors.put("minecraft:frozen_ocean",                     0x9090A0);
		BiomeColorMappings.defaultColors.put("minecraft:frozen_river",                     0xA0A0FF);
		BiomeColorMappings.defaultColors.put("minecraft:ice_flats",                        0xFFFFFF);
		BiomeColorMappings.defaultColors.put("minecraft:ice_mountains",                    0xA0A0A0);
		BiomeColorMappings.defaultColors.put("minecraft:mushroom_island",                  0xFF00FF);
		BiomeColorMappings.defaultColors.put("minecraft:mushroom_island_shore",            0xA000FF);
		BiomeColorMappings.defaultColors.put("minecraft:beaches",                          0xFADE55);
		BiomeColorMappings.defaultColors.put("minecraft:desert_hills",                     0xD25F12);
		BiomeColorMappings.defaultColors.put("minecraft:forest_hills",                     0x22551C);
		BiomeColorMappings.defaultColors.put("minecraft:taiga_hills",                      0x163933);
		BiomeColorMappings.defaultColors.put("minecraft:smaller_extreme_hills",            0x72789A);
		BiomeColorMappings.defaultColors.put("minecraft:jungle",                           0x537B09);
		BiomeColorMappings.defaultColors.put("minecraft:jungle_hills",                     0x2C4205);
		BiomeColorMappings.defaultColors.put("minecraft:jungle_edge",                      0x628B17);
		BiomeColorMappings.defaultColors.put("minecraft:deep_ocean",                       0x000030);
		BiomeColorMappings.defaultColors.put("minecraft:stone_beach",                      0xA2A284);
		BiomeColorMappings.defaultColors.put("minecraft:cold_beach",                       0xFAF0C0);
		BiomeColorMappings.defaultColors.put("minecraft:birch_forest",                     0x307444);
		BiomeColorMappings.defaultColors.put("minecraft:birch_forest_hills",               0x1F5F32);
		BiomeColorMappings.defaultColors.put("minecraft:roofed_forest",                    0x40511A);
		BiomeColorMappings.defaultColors.put("minecraft:taiga_cold",                       0x31554A);
		BiomeColorMappings.defaultColors.put("minecraft:taiga_cold_hills",                 0x243F36);
		BiomeColorMappings.defaultColors.put("minecraft:redwood_taiga",                    0x596651);
		BiomeColorMappings.defaultColors.put("minecraft:redwood_taiga_hills",              0x454F3E);
		BiomeColorMappings.defaultColors.put("minecraft:extreme_hills_with_trees",         0x507050);
		BiomeColorMappings.defaultColors.put("minecraft:savanna",                          0xBDB25F);
		BiomeColorMappings.defaultColors.put("minecraft:savanna_rock",                     0xA79D64);
		BiomeColorMappings.defaultColors.put("minecraft:mesa",                             0xD94515);
		BiomeColorMappings.defaultColors.put("minecraft:mesa_rock",                        0xB09765);
		BiomeColorMappings.defaultColors.put("minecraft:mesa_clear_rock",                  0xCA8C65);
		BiomeColorMappings.defaultColors.put("minecraft:void",                             0x000000); // Not in Amidst as of v4.3-beta1
		BiomeColorMappings.defaultColors.put("minecraft:mutated_plains",                   0xB5DB88);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_desert",                   0xFFBC40);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_extreme_hills",            0x888888);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_forest",                   0x2D8E49);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_taiga",                    0x338E81);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_swampland",                0x2FFFDA);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_ice_flats",                0xB4DCDC);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_jungle",                   0x7BA331);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_jungle_edge",              0x8AB33F);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_birch_forest",             0x589C6C);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_birch_forest_hills",       0x47875A);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_roofed_forest",            0x687942);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_taiga_cold",               0x597D72);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_redwood_taiga",            0x818E79);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_redwood_taiga_hills",      0x6D7766);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_extreme_hills_with_trees", 0x789878);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_savanna",                  0xE5DA87);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_savanna_rock",             0xCFC58C);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_mesa",                     0xFF6D3D);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_mesa_rock",                0xD8BF8D);
		BiomeColorMappings.defaultColors.put("minecraft:mutated_mesa_clear_rock",          0xF2B48D);
	}

	public static Integer getColorForBiome(final String biomeName)
	{
		return BiomeColorMappings.defaultColors.getOrDefault(biomeName, (Biome.getIdForBiome(Biome.REGISTRY.getObject(new ResourceLocation(biomeName))) & 0xFF) << 16);
	}

}
