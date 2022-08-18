package me.superckl.biometweaker.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigGui extends Screen{

	private final Screen prevScreen;

	protected ConfigGui(final Screen prevScreen) {
		super(Component.literal("BiomeTweaker Configuration"));
		this.prevScreen = prevScreen;
	}

	@Override
	public void render(final PoseStack poseStack, final int pMouseX, final int pMouseY, final float pPartialTick) {
		this.renderBackground(poseStack);
		super.render(poseStack, pMouseX, pMouseY, pPartialTick);
	}

	@Override
	public void onClose() {
		super.minecraft.setScreen(this.prevScreen);
	}

}
