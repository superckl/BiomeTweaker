package me.superckl.biometweaker.util;

public record IntRange(int min, int max) implements Comparable<IntRange>{

	public boolean contains(final int value) {
		return this.min <= value && this.max >= value;
	}

	@Override
	public int compareTo(final IntRange o) {
		return Integer.compare(this.min, o.min) == 0 ? 0 : Integer.compare(this.max, o.max);
	}

}
