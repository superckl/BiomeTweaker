package me.superckl.api.superscript.script;

public interface IStringParser<T> {

	/**
	 * Attempts to parse the given string argument into a type given by this ParameterType instance.
	 * This should not be used for biome packs. Use their special wrapper instead.
	 */
	public T tryParse(final String parameter, final ScriptHandler handler) throws Exception;

}
