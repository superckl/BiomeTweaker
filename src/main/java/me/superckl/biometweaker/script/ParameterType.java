package me.superckl.biometweaker.script;

import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.gson.JsonPrimitive;

public enum ParameterType {

	STRING, INTEGER, NON_NEG_INTEGER, FLOAT, SPAWN_TYPE, JSON_ELEMENT;

	public Object tryParse(final String parameter){
		switch(this){
		case FLOAT:{
			return Floats.tryParse(parameter);
		}
		case INTEGER:{
			return Ints.tryParse(parameter);
		}
		case NON_NEG_INTEGER:{
			final Integer i = Ints.tryParse(parameter);
			if((i != null) && (i.intValue() >= 0))
				return i;
			break;
		}
		case STRING:{
			if(parameter.startsWith("\"") && parameter.endsWith("\""))
				return parameter.substring(1, parameter.length()-1);
			break;
		}
		case SPAWN_TYPE: {
			final String extracted = (String) ParameterType.STRING.tryParse(parameter);
			if(extracted != null)
				return Type.valueOf(extracted);
			break;
		}
		case JSON_ELEMENT: {
			return new JsonPrimitive(parameter);
		}
		default:
			break;
		}
		return null;
	}

}
