package me.superckl.biometweaker.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Cleanup;
import me.superckl.biometweaker.config.Config;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

public class BiomeTweakerCallHook implements IFMLCallHook{

	@Override
	public Void call() throws Exception {
		BiomeTweakerCore.logger.info("Beginning early config parsing...");
		final File operateIn = new File(BiomeTweakerCore.mcLocation, "config/BiomeTweaker/");
		BiomeTweakerCore.logger.debug("We are operating in "+operateIn.getAbsolutePath());
		final File mainConfig = new File(operateIn, "BiomeTweaker.cfg");
		final File scripts = new File(operateIn, "scripts/");
		scripts.mkdirs();
		if(!mainConfig.exists()){
			mainConfig.createNewFile();
			{
				int readBytes;
				final byte[] buffer = new byte[4096];
				@Cleanup
				final InputStream stream = this.getClass().getResourceAsStream("/BiomeTweaker.cfg");
				@Cleanup
				final OutputStream outStream = new FileOutputStream(mainConfig);
				while ((readBytes = stream.read(buffer)) > 0)
					outStream.write(buffer, 0, readBytes);
			}
			final File exampleTweaks = new File(operateIn, "ExampleTweaks.cfg");
			if(!exampleTweaks.exists()){
				exampleTweaks.createNewFile();
				int readBytes;
				final byte[] buffer = new byte[4096];
				@Cleanup
				final InputStream stream = this.getClass().getResourceAsStream("/ExampleTweaks.cfg");
				@Cleanup
				final OutputStream outStream = new FileOutputStream(exampleTweaks);
				while ((readBytes = stream.read(buffer)) > 0)
					outStream.write(buffer, 0, readBytes);
			}
		}
		@Cleanup
		final
		BufferedReader reader = new BufferedReader(new FileReader(mainConfig));
		final JsonObject obj = (JsonObject) new JsonParser().parse(reader);
		if(obj.entrySet().isEmpty())
			BiomeTweakerCore.logger.warn("The configuration file read as empty! BiomeTweaker isn't going to do anything.");
		Config.INSTANCE.init(operateIn, obj);
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {}

}
