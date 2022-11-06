package me.superckl.api.biometweaker.property;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import me.superckl.api.biometweaker.BiomeLookup;
import net.minecraft.resources.ResourceLocation;

public class EarlyBiomeProperty<K> extends Property<K, ResourceLocation, BiomeLookup>{

	private final BiFunction<? super ResourceLocation, ? super BiomeLookup, ? extends K> getter;
	private final BiConsumer<? super ResourceLocation, ? super K> setter;

	public EarlyBiomeProperty(final Class<K> type, final BiFunction<? super ResourceLocation, ? super BiomeLookup, ? extends K> getter, final BiConsumer<? super ResourceLocation, ? super K> setter) {
		super(type);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void set(final ResourceLocation obj, final K val) {
		this.setter.accept(obj, val);
	}

	@Override
	public K get(final ResourceLocation obj, final Optional<BiomeLookup> lookup) {
		return this.getter.apply(obj, lookup.get());
	}

	@Override
	public boolean isReadable() {
		return this.getter != null;
	}

	@Override
	public boolean isSettable() {
		return this.setter != null;
	}

	@Override
	public Class<ResourceLocation> getTargetClass() {
		return ResourceLocation.class;
	}

}
