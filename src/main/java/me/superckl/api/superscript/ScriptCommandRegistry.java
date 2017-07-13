package me.superckl.api.superscript;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.object.ScriptObject;

public class ScriptCommandRegistry {

	public static final ScriptCommandRegistry INSTANCE = new ScriptCommandRegistry();

	private final Map<String, IScriptCommandManager> managers = Maps.newHashMap();

	private final Map<Class<? extends ScriptObject>, Map<String, ScriptCommandListing>> commands = Maps.newHashMap();

	public Map<Class<? extends ScriptObject>, Map<String, ScriptCommandListing>> getCommandsMap(){
		return Maps.newHashMap(this.commands);
	}

	/**
	 * Registers a new listing for the given command and for the given ScriptObject classes. This will override any existing listing for the passed name.
	 * In most cases, you should use {@link #getListing(Class, String) getListing} instead of this method, so that you don't override anything you don't mean to.
	 * @param command The command that should be recognized by the script parser.
	 * @param listing The command listing that should be used to parse the command.
	 * @param clazz The ScriptObject class this command should be defined for.
	 */
	public void registerListing(final String command, final ScriptCommandListing listing, final Class<? extends ScriptObject> clazz){
		if(!this.commands.containsKey(clazz))
			this.commands.put(clazz, new LinkedHashMap<String, ScriptCommandListing>());
		this.commands.get(clazz).put(command, listing);
	}

	/**
	 * Registers a new Class listing. This will override any existing listing for the Class.
	 * @param clazz The ScriptObject class this listing should be for.
	 * @param listing The listing to register.
	 */
	public void registerClassListing(final Class<? extends ScriptObject> clazz, final Map<String, ScriptCommandListing> listing){
		this.commands.put(clazz, listing);
	}

	/**
	 * Attempts to retrieve any existing command listings for the given class, creating one if none are found.
	 * @param clazz The class to lookup listings for.
	 * @return The map of command listings for the given class. This will never be null.
	 */
	public Map<String, ScriptCommandListing> getListings(final Class<? extends ScriptObject> clazz){
		Map<String, ScriptCommandListing> listings = this.commands.get(clazz);
		if(listings == null){
			listings = Maps.newLinkedHashMap();
			this.commands.put(clazz, listings);
		}
		return Maps.newLinkedHashMap(listings);
	}

	/**
	 * Attempts to retrieve the command listing for the given class and command, creating one if one does not already exist.
	 * This method should be used instead of {@link #registerListing(String, ScriptCommandListing, Class...) registerListing} to register new command listings,
	 * unless you want to override existing listings.
	 * @param clazz The class to lookup listings for.
	 * @param command The command to lookup a listing for.
	 * @return The command listing. This will never be null.
	 */
	public ScriptCommandListing getListing(final Class<? extends ScriptObject> clazz, final String command){
		ScriptCommandListing listing = this.getListings(clazz).get(command);
		if(listing == null){
			listing = new ScriptCommandListing();
			this.commands.get(clazz).put(command, listing);
		}
		return listing;
	}

	public void registerScriptCommandManager(final String key, final IScriptCommandManager manager){
		this.managers.put(key, manager);
	}

	public IScriptCommandManager getManagerFor(final String owner){
		return this.managers.get(owner);
	}

}
