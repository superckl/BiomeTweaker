package me.superckl.biometweaker.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import lombok.NonNull;

public class LogHelper {

	private static Logger log;

	public static void log(final Level logLevel, final Object object)
	{
		if(LogHelper.log == null)
			throw new IllegalStateException("Logger has not been set!");
		LogHelper.log.log(logLevel, String.valueOf(object));
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

	public static void setLogger(@NonNull final Logger log){
		if(LogHelper.log != null)
			throw new IllegalStateException("Logger has already been set!");
		LogHelper.log = log;
	}

}
