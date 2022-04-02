package me.superckl.api.biometweaker.property;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class EarlyBiomeProperty<K> extends Property<K, ResourceLocation>{

	private final Function<? super Biome, ? extends K> getter;
	private final BiConsumer<? super ResourceLocation, ? super K> setter;

	public EarlyBiomeProperty(final Class<K> type, final Function<? super Biome, ? extends K> getter, final BiConsumer<? super ResourceLocation, ? super K> setter) {
		super(type);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public void set(final ResourceLocation obj, final K val) {
		this.setter.accept(obj, val);
	}

	@Override
	public K get(final ResourceLocation obj) {
		return this.getter.apply(ForgeRegistries.BIOMES.getValue(obj));
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
