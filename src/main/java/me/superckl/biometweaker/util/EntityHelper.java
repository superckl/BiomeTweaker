package me.superckl.biometweaker.util;

import com.google.gson.JsonObject;

import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class EntityHelper {

	public static Class<? extends Entity> getEntityClass(final String identifier){
		Class<? extends Entity> clazz = null;
		try{
			clazz = WarningHelper.uncheckedCast(Class.forName(identifier));
			return clazz;
		}catch(final Exception e){
			return EntityList.getClass(new ResourceLocation(identifier));
		}

	}

	public static JsonObject populateObject(final EntityEntry entry){
		final JsonObject obj = new JsonObject();
		obj.addProperty("Name", entry.getName());
		obj.addProperty("Registry ID", entry.getRegistryName().toString());
		obj.addProperty("Class", entry.getEntityClass().getCanonicalName());
		return obj;
	}

}
