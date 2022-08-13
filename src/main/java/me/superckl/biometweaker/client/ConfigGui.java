package me.superckl.biometweaker.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class ConfigGui extends Screen{

	private final Screen prevScreen;
	
	protected ConfigGui(Screen prevScreen) {
		super(new TextComponent("BiomeTweaker Configuration"));
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(poseStack);
		super.render(poseStack, pMouseX, pMouseY, pPartialTick);
	}
	
	@Override
	public void onClose() {
		super.minecraft.setScreen(this.prevScreen);
	}

}
