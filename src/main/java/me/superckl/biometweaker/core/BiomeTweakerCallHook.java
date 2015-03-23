package me.superckl.biometweaker.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import lombok.Cleanup;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.config.Config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.relauncher.IFMLCallHook;

public class BiomeTweakerCallHook implements IFMLCallHook{

	@Override
	public Void call() throws Exception {
		Logger log = ModBiomeTweakerCore.logger;
		log.info("Beginning early configuration parsing...");
		File operateIn = new File(BiomeTweakerCore.mcLocation, "config/BiomeTweaker/");
		log.debug("We are operating in "+operateIn.getAbsolutePath());
		File mainConfig = new File(operateIn, "BiomeTweaker.cfg");
		operateIn.mkdirs();
		if(!mainConfig.exists()){
			mainConfig.createNewFile();
			int readBytes;
            byte[] buffer = new byte[4096];
            @Cleanup
			InputStream stream = BiomeTweaker.class.getResourceAsStream("/BiomeTweaker.cfg");
			@Cleanup
			OutputStream outStream = new FileOutputStream(mainConfig);
			while ((readBytes = stream.read(buffer)) > 0) {
                outStream.write(buffer, 0, readBytes);
            }
			//TODO create example tweak file.
		}
		@Cleanup
		BufferedReader reader = new BufferedReader(new FileReader(mainConfig));
		JsonObject obj = (JsonObject) new JsonParser().parse(reader);
		if(obj.entrySet().isEmpty()){
			log.warn("The configuration file read as empty! BiomeTweaker isn't going to do anything.");
		}
		Config.INSTANCE.init(operateIn, obj);
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}

}
