package me.superckl.biometweaker.util;

import me.superckl.biometweaker.common.reference.ModData;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {

	public static void log(final Level logLevel, final Object object)
	{
		FMLLog.log(ModData.MOD_NAME, logLevel, String.valueOf(object));
	}

	public static void all(final Object object)
	{
		LogHelper.log(Level.ALL, object);
	}

	public static void debug(final Object object)
	{
		LogHelper.log(Level.DEBUG, object);
	}

	public static void error(final Object object)
	{
		LogHelper.log(Level.ERROR, object);
	}

	public static void fatal(final Object object)
	{
		LogHelper.log(Level.FATAL, object);
	}

	public static void info(final Object object)
	{
		LogHelper.log(Level.INFO, object);
	}

	public static void off(final Object object)
	{
		LogHelper.log(Level.OFF, object);
	}

	public static void trace(final Object object)
	{
		LogHelper.log(Level.TRACE, object);
	}

	public static void warn(final Object object)
	{
		LogHelper.log(Level.WARN, object);
	}

}
