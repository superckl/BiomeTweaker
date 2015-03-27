package me.superckl.biometweaker.script;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public enum ParameterType {

	STRING, INTEGER, NON_NEG_INTEGER, FLOAT;
	
	public Object tryParse(String parameter){
		switch(this){
		case FLOAT:{
			return Floats.tryParse(parameter);
		}
		case INTEGER:{
			return Ints.tryParse(parameter);
		}
		case NON_NEG_INTEGER:{
			Integer i = Ints.tryParse(parameter);
			if(i != null && i.intValue() >= 0)
				return i;
			break;
		}
		case STRING:{
			if(parameter.startsWith("\"") && parameter.endsWith("\""))
				return parameter.substring(1, parameter.length()-1);
			break;
		}
		default:
			break;
		}
		return null;
	}
	
}
