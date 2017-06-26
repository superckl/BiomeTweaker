package me.superckl.biometweaker.client.gui;

import com.google.common.collect.Lists;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ButtonEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class RegenerateOutputConfigEntry extends ButtonEntry{

	private boolean youSure;
	private int pressedCounter;

	public RegenerateOutputConfigEntry(final GuiConfig owningScreen, final GuiConfigEntries owningEntryList, final IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
		this.btnValue.displayString = "Regenerate Output Files";
	}

	@Override
	public void updateValueButtonText() {
		if(this.youSure)
			this.btnValue.displayString = "Are you sure?";
		else
			this.btnValue.displayString = "Regenerate Output Files";
	}


	@Override
	public void drawToolTip(final int mouseX, final int mouseY) {
		if(this.pressedCounter-- > 0)
			this.owningScreen.drawToolTip(Lists.newArrayList(TextFormatting.AQUA+"Output files regenerated.", TextFormatting.RED+"Don't spam the button!"), mouseX, mouseY);
		else
			super.drawToolTip(mouseX, mouseY);
	}

	@Override
	public void valueButtonPressed(final int slotIndex) {
		if(this.pressedCounter > 0)
			return;
		if(this.youSure)
			try {
				BiomeTweaker.getInstance().generateOutputFiles();
				this.pressedCounter = 200;
			} catch (final Exception e) {
				LogHelper.error("Failed to regenerate output files!");
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
		this.btnValue.displayString = "Regenerate Output Files";
	}

	@Override
	public boolean isChanged() {
		return this.youSure;
	}

	@Override
	public void undoChanges() {
		this.youSure = false;
		this.btnValue.displayString = "Regenerate Output Files";
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

}
