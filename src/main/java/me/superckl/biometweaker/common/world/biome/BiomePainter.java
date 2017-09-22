package me.superckl.biometweaker.common.world.biome;

import java.io.File;
import java.util.Arrays;

import ar.com.hjg.pngj.IImageLineFactory;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import lombok.RequiredArgsConstructor;
import me.superckl.biometweaker.client.gui.GuiGeneratingBiomeLayoutImage;
import me.superckl.biometweaker.common.world.DummyWorld;
import me.superckl.biometweaker.util.BiomeColorMappings;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.storage.WorldInfo;

@RequiredArgsConstructor
public class BiomePainter {

	private final String worldName;
	private final WorldSettings worldSettings;

	private BiomeProvider bProvider;

	private final Biome[] tempArray = new Biome[1];
	private final int[] colorArray = new int[256];

	public void paintImage(final int radius, final ChunkPos center, final File outputFile, final GuiGeneratingBiomeLayoutImage gui) {
		Arrays.fill(this.colorArray, -1);
		final ImageInfo iInfo = new ImageInfo(radius*2, radius*2, 8, false);
		final IImageLineFactory<ImageLineInt> factory = ImageLineInt.getFactory(iInfo);
		final PngWriter writer = new PngWriter(outputFile, iInfo);
		final int[] colors = new int[iInfo.cols];
		final int currentX = center.x - iInfo.cols/2;
		final int currentY = center.z + iInfo.rows/2;
		for(int i = 0; i < iInfo.rows; i++) {
			gui.setCurrentX(currentX + i);
			for(int j = 0; j < colors.length; j++) {
				gui.setCurrentY(currentY + j);
				colors[j] = this.getColor(currentX + i, currentY + j);
			}
			final ImageLineInt line = factory.createImageLine(iInfo);
			ImageLineHelper.setPixelsRGB8(line, colors);
			writer.writeRow(line);
			gui.setProgress(((double) i)/iInfo.rows);
		}
		writer.end();
		gui.setFinished();
	}

	private int getColor(final int chunkX, final int chunkZ) {
		if(this.bProvider == null) {
			final DummyWorld world = new DummyWorld(new WorldInfo(this.worldSettings, this.worldName));
			world.provider.setWorld(world);
			this.bProvider = world.getBiomeProvider();
		}
		this.tempArray[0] = null;
		final int bId = Biome.getIdForBiome(this.bProvider.getBiomes(this.tempArray, (chunkX << 4) + 8, (chunkZ << 4) + 8, 1, 1, false)[0]);
		if(this.colorArray[bId] == -1) {
			LogHelper.info(bId);
			LogHelper.info(Biome.getBiome(bId));
			LogHelper.info(Biome.REGISTRY.getNameForObject(Biome.getBiome(bId)));;
			this.colorArray[bId] = BiomeColorMappings.getColorForBiome(Biome.REGISTRY.getNameForObject(Biome.getBiome(bId)).toString());
		}
		return this.colorArray[bId];
	}

}
