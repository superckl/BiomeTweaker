package me.superckl.api.biometweaker.property;

import com.google.gson.JsonPrimitive;

import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.script.ParameterType;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.world.level.block.state.BlockState;

public class PropertyHelper {

	public static <K, V> void setProperty(final Object obj, final Property<K, V> property, final JsonPrimitive value, final ScriptHandler handler) throws Exception{
		if(!property.isSettable())
			throw new UnsupportedOperationException("Property is not settable!");
		if(!property.getTargetClass().isAssignableFrom(obj.getClass()))
			throw new IllegalArgumentException("Property is not applicable to object with type "+obj.getClass().getSimpleName());
		final V target = property.getTargetClass().cast(obj);
		final Class<K> type = property.getTypeClass();
		try {
			if(type.isAssignableFrom(BlockState.class)) {
				WarningHelper.<Property<? super BlockState, V>>uncheckedCast(property).set(target, BTParameterTypes.BLOCKSTATE_BUILDER.tryParse(value.getAsString(), handler).build());
				return;
			}
			final ParameterType<? extends K> pType = ParameterTypes.getDefaultType(type);
			if(pType == null)
				throw new IllegalStateException("No paramter type found for required type "+type.getCanonicalName());
			final K parsed = pType.tryParse(value.getAsString(), handler);
			if(parsed == null)
				throw new IllegalArgumentException("Failed to parse command argument "+value.getAsString());
			property.set(target, parsed);
		} catch (final Exception e) {
			throw e;
		}
	}

}
