package me.superckl.biometweaker.client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import lombok.Cleanup;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class ReloadScriptsConfigEntry extends ButtonEntry{

	private boolean youSure;
	private int pressedCounter;

	public ReloadScriptsConfigEntry(final GuiConfig owningScreen, final GuiConfigEntries owningEntryList, final IConfigElement<?> configElement) {
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
			this.owningScreen.drawToolTip(Lists.newArrayList(EnumChatFormatting.AQUA+"Scripts reloaded.", EnumChatFormatting.RED+"Don't spam the button!"), mouseX, mouseY);
		else
			super.drawToolTip(mouseX, mouseY);
	}

	@Override
	public void valueButtonPressed(final int slotIndex) {
		if(this.pressedCounter > 0)
			return;
		if(this.youSure)
			try {
				final File operateIn = new File(BiomeTweakerCore.mcLocation, "config/BiomeTweaker/");
				final File mainConfig = new File(operateIn, "BiomeTweaker.cfg");
				@Cleanup
				final
				BufferedReader reader = new BufferedReader(new FileReader(mainConfig));
				final JsonObject obj = (JsonObject) new JsonParser().parse(reader);
				if(obj.entrySet().isEmpty())
					ModBiomeTweakerCore.logger.warn("The configuration file read as empty! BiomeTweaker isn't going to do anything.");
				Config.INSTANCE.init(operateIn, obj);
				this.pressedCounter = 200;
			} catch (final Exception e) {
				ModBiomeTweakerCore.logger.error("Failed to reload scripts!");
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

}
