package me.superckl.biometweaker.client.gui;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ButtonEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class BiomeLayoutConfigEntry extends ButtonEntry{

	public BiomeLayoutConfigEntry(final GuiConfig owningScreen, final GuiConfigEntries owningEntryList, final IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
		this.btnValue.displayString = "Open Biome Layout Image GUI";
	}

	@Override
	public void valueButtonPressed(final int slotIndex) {
		this.mc.displayGuiScreen(new GuiBiomeLayoutImage(this.owningScreen));
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public void setToDefault() {}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public boolean saveConfigElement() {
		return false;
	}

	@Override
	public Object getCurrentValue() {
		return null;
	}

	@Override
	public Object[] getCurrentValues() {
		return null;
	}

	@Override
	public void updateValueButtonText() {}

	@Override
	public void undoChanges() {}

}
