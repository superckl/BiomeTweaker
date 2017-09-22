package me.superckl.biometweaker.util;

import com.google.common.base.Predicate;

public class PredicateHelper {

	public static final Predicate<String> EMPTY_STRING = input -> input.isEmpty();

	public static final Predicate<String> NONNEG_INT = input -> {
		try {
			Integer.parseUnsignedInt(input);
			return true;
		}catch (final Exception e) {
			return false;
		}
	};

	public static final Predicate<String> INT = input -> {
		try {
			Integer.parseInt(input);
			return true;
		}catch (final Exception e) {
			return false;
		}
	};

}
