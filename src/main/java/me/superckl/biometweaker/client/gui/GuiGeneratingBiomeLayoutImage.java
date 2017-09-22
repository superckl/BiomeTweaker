package me.superckl.biometweaker.client.gui;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiGeneratingBiomeLayoutImage extends GuiScreen{

	@Setter
	private double progress;
	@Setter
	private int currentX;
	@Setter
	private int currentY;
	@Getter
	private boolean cancelled;
	private boolean finished;

	@Override
	public void initGui() {
		this.buttonList.clear();

		this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height/2 +5, 150, 20, "Done"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height/2+5, 150, 20, I18n.format("gui.cancel")));

		this.buttonList.get(0).enabled = false;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Biome Layout Image Setup", this.width / 2, 20, -1);

		this.drawCenteredString(this.fontRenderer, this.finished ? "Progress: Finished":this.cancelled ? "Progress: CANCELLED :(":String.format("Progress: %02d%%", (int) (this.progress * 100)),
				this.width/2-80, this.height/2 - 5 - this.fontRenderer.FONT_HEIGHT, 14737632);
		this.drawString(this.fontRenderer, this.finished ? "Current Chunk: Finished":this.cancelled ? "Current Chunk: CANCELLED :(":String.format("Current Chunk: (%d, %d)", this.currentX, this.currentY),
				this.width/2 + 10, this.height/2 - 5 - this.fontRenderer.FONT_HEIGHT, 14737632);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		if(!button.enabled)
			return;
		if (button.id == 1) {
			this.cancelled = true;
			this.buttonList.get(0).enabled = true;
			this.buttonList.get(1).enabled = false;
		}else if (button.id == 0)
			this.mc.displayGuiScreen(null);
	}

	@Synchronized
	public void setFinished() {
		this.finished = true;
		this.buttonList.get(0).enabled = true;
		this.buttonList.get(1).enabled = false;
	}

}
