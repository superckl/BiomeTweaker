package me.superckl.api.biometweaker.property;

import com.google.gson.JsonPrimitive;

import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.script.ParameterType;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.block.state.IBlockState;

public class PropertyHelper {

	public static void setProperty(final Object obj, final Property<?> property, final JsonPrimitive value, final ScriptHandler handler) throws Exception{
		if(!property.isSettable())
			throw new UnsupportedOperationException("Property is not settable!");
		if(!property.getTargetClass().isAssignableFrom(obj.getClass()))
			throw new IllegalArgumentException("Property is not applicable to object with type "+obj.getClass().getSimpleName());
		final Class<?> type = property.getTypeClass();
		try {
			if(type.isAssignableFrom(IBlockState.class)) {
				WarningHelper.<Property<? super IBlockState>>uncheckedCast(property).set(obj, BTParameterTypes.BLOCKSTATE_BUILDER.tryParse(value.getAsString(), handler).build());
				return;
			}
			final ParameterType<?> pType = ParameterTypes.getDefaultType(type);
			if(pType != null)
				PropertyHelper.typeSafeSet(property, obj, pType.tryParse(pType == ParameterTypes.STRING ? value.getAsString():ParameterTypes.STRING.tryParse(value.getAsString(), handler), handler));
		} catch (final Exception e) {
			throw e;
		}
	}

	public static <K> void typeSafeSet(final Property<K> prop, final Object obj, final Object val){
		prop.set(obj, WarningHelper.uncheckedCast(val));
	}

}
