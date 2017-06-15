package me.superckl.biometweaker.script.command.misc;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.property.BiomePropertyManager;
import me.superckl.api.biometweaker.property.Property;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.biome.Biome;

@RequiredArgsConstructor
public class ScriptCommandInheritProperties implements IScriptCommand{

	private final IBiomePackage pack;
	private final IBiomePackage toInheritFrom;
	private final String[] properties;

	public ScriptCommandInheritProperties(final IBiomePackage pack, final IBiomePackage toInheritFrom) {
		this(pack, toInheritFrom, null);
	}

	@Override
	public void perform() throws Exception {
		final Iterator<Biome> it = this.pack.getIterator();
		while(it.hasNext()){
			final Biome gen = it.next();
			String[] props;
			if(this.properties == null)
				props = BiomePropertyManager.propertyMap.keySet().toArray(new String[] {});
			else
				props = this.properties;
			for(final String property:props){
				final Property<?> prop = BiomePropertyManager.propertyMap.get(property);
				if(prop == null){
					LogHelper.error("No property found for "+property);
					continue;
				}
				if(!prop.isCopyable()){
					if(this.properties != null)
						LogHelper.error("Property "+property + " is not copyable!");
					continue;
				}
				final Iterator<Biome> inheritFrom = this.toInheritFrom.getIterator();
				while(inheritFrom.hasNext())
					try {
						prop.copy(inheritFrom.next(), gen);
					} catch (final Exception e) {
						LogHelper.debug("Failed to inherit property "+property+". This may be because it hasn't been set in the parent biome, or the property is not readable.");
					}
			}
		}
	}

}
