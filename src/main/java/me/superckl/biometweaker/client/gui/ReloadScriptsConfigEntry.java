package me.superckl.biometweaker.client.gui;

import com.google.common.collect.Lists;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ButtonEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ReloadScriptsConfigEntry extends ButtonEntry{

	private boolean youSure;
	private int pressedCounter;

	public ReloadScriptsConfigEntry(final GuiConfig owningScreen, final GuiConfigEntries owningEntryList, final IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
		this.btnValue.displayString = "Reload Scripts";
	}

	@Override
	public void updateValueButtonText() {
		if(this.youSure)
			this.btnValue.displayString = "Are you sure?";
		else
			this.btnValue.displayString = "Reload Scripts";
	}


	@Override
	public void drawToolTip(final int mouseX, final int mouseY) {
		if(this.pressedCounter-- > 0)
			this.owningScreen.drawToolTip(Lists.newArrayList(TextFormatting.AQUA+"Scripts reloaded.", TextFormatting.RED+"Don't spam the button!"), mouseX, mouseY);
		else
			super.drawToolTip(mouseX, mouseY);
	}

	@Override
	public void valueButtonPressed(final int slotIndex) {
		if(this.pressedCounter > 0)
			return;
		if(this.youSure)
			try {
				BiomeTweaker.getInstance().getConfig().loadValues();
				BiomeTweaker.getInstance().getCommandManager().reset();
				BiomeTweaker.getInstance().parseScripts();
				this.pressedCounter = 200;
			} catch (final Exception e) {
				LogHelper.error("Failed to reload scripts!");
				e.printStackTrace();
			}
		this.youSure = !this.youSure;
	}

	@Override
	public boolean isDefault() {
		return !this.youSure;
	}

	@Override
	public void setToDefault() {
		this.youSure = false;
		this.btnValue.displayString = "Reload Scripts";
	}

	@Override
	public boolean isChanged() {
		return this.youSure;
	}

	@Override
	public void undoChanges() {
		this.youSure = false;
		this.btnValue.displayString = "Reload Scripts";
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
	public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {}

}
