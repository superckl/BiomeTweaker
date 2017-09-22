package me.superckl.biometweaker.client.gui;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicates;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.world.biome.BiomePainter;
import me.superckl.biometweaker.util.PredicateHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.StartupQuery;

public class GuiBiomeLayoutImage extends GuiScreen{

	private final GuiScreen parentScreen;

	private GuiTextField worldNameField;
	private GuiTextField worldSeedField;
	private GuiTextField radiusField;
	private GuiTextField xField;
	private GuiTextField yField;
	private GuiButton btnMapType;
	private GuiButton btnBypassProvider;

	private int selectedIndex;
	private String worldSeed;
	private String worldName;
	private int radius;
	private int xCoord;
	private int yCoord;
	private String saveDirName;

	public GuiBiomeLayoutImage(final GuiScreen parentScreen)
	{
		this.parentScreen = parentScreen;
		this.worldSeed = "";
		this.worldName = I18n.format("selectWorld.newWorld");
	}

	@Override
	public void updateScreen()
	{
		this.worldNameField.updateCursorCounter();
		this.worldSeedField.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();

		this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, "Generate Image"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
		this.btnBypassProvider = this.addButton(new GuiButton(2, this.width / 2 - 155, 151, 150, 20, "Bypass Biome Provider: NO"));
		this.btnBypassProvider.enabled = false;
		this.btnMapType = this.addButton(new GuiButton(3, this.width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.mapType")));

		this.worldNameField = new GuiTextField(4, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
		this.worldNameField.setFocused(true);
		this.worldNameField.setText(this.worldName);

		this.worldSeedField = new GuiTextField(5, this.fontRenderer, this.width / 2 - 100, 111, 200, 20);
		this.worldSeedField.setText(this.worldSeed);

		final int y = 173+this.fontRenderer.FONT_HEIGHT + ((this.height-28)-(173+this.fontRenderer.FONT_HEIGHT))/2-10;
		final int width = (150-this.fontRenderer.getStringWidth("X:")-this.fontRenderer.getStringWidth("Y:")-30)/2;

		this.radiusField = new GuiTextField(6, this.fontRenderer, this.width/2-155+this.fontRenderer.getStringWidth("Radius (chunks):")+10,
				y, 150-this.fontRenderer.getStringWidth("Radius (chunks):")-10, 20);
		this.radiusField.setValidator(Predicates.or(PredicateHelper.EMPTY_STRING, PredicateHelper.NONNEG_INT));
		this.radiusField.setText(String.valueOf(this.radius));

		this.xField = new GuiTextField(7, this.fontRenderer, this.width/2+5+this.fontRenderer.getStringWidth("X:")+10, y, width, 20);
		this.xField.setValidator(Predicates.or(PredicateHelper.EMPTY_STRING, PredicateHelper.INT));
		this.xField.setText(String.valueOf(this.xCoord));

		this.yField = new GuiTextField(8, this.fontRenderer,
				this.width/2+5+this.fontRenderer.getStringWidth("X:")+10+width+10+this.fontRenderer.getStringWidth("Y:")+10, y, width, 20);
		this.yField.setValidator(Predicates.or(PredicateHelper.EMPTY_STRING, PredicateHelper.INT));
		this.yField.setText(String.valueOf(this.yCoord));

		this.calcSaveDirName();
		this.updateDisplayState();
	}

	private void updateDisplayState()
	{
		this.btnMapType.displayString = I18n.format("selectWorld.mapType") + " " + I18n.format(WorldType.WORLD_TYPES[this.selectedIndex].getTranslationKey());

		(this.buttonList.get(0)).enabled = !this.worldName.isEmpty() && this.radius > 0;
	}

	private void calcSaveDirName()
	{
		this.saveDirName = this.worldName.trim();

		for (final char c0 : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS)
			this.saveDirName = this.saveDirName.replace(c0, '_');

		if (StringUtils.isEmpty(this.saveDirName))
			this.saveDirName = "World";
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		if(!button.enabled)
			return;

		if (button.id == 1)
			this.mc.displayGuiScreen(this.parentScreen);
		else if (button.id == 0)
		{
			long i = (new Random()).nextLong();
			final String s = this.worldSeedField.getText();

			if (!StringUtils.isEmpty(s))
				try
			{
					final long j = Long.parseLong(s);

					if (j != 0L)
						i = j;
			}
			catch (final NumberFormatException var7)
			{
				i = s.hashCode();
			}

			WorldType.WORLD_TYPES[this.selectedIndex].onGUICreateWorldPress();

			final WorldSettings worldsettings = new WorldSettings(i, GameType.SURVIVAL, false, false, WorldType.WORLD_TYPES[this.selectedIndex]);
			worldsettings.setGeneratorOptions("");

			final File imagesDir = new File(BiomeTweaker.getInstance().getConfig().getBtConfigFolder(), "output/images/");
			imagesDir.mkdirs();

			this.calcSaveDirName();
			final File imageFile = new File(imagesDir, this.saveDirName+".png");

			final char newLine = '\n';

			final StringBuilder builder = new StringBuilder("You are about to generate a PNG image for the following biome layout:").append(newLine).append(newLine)
					.append("World Name: ").append(this.worldName.trim()).append(newLine)
					.append("World Seed: ").append(i).append(newLine)
					.append("World Type: ").append(WorldType.WORLD_TYPES[this.selectedIndex].getName()).append(newLine)
					.append("Size: ").append(this.radius*2).append("x").append(this.radius*2).append(" chunks").append(newLine)
					.append("Center: (").append(this.xCoord).append(", ").append(this.yCoord).append(')').append(newLine).append(newLine)
					.append("The generated PNG file will have the following properties:").append(newLine).append(newLine)
					.append("Location: ").append(this.mc.mcDataDir.getAbsoluteFile().toPath().relativize(imageFile.getAbsoluteFile().toPath())).append(newLine)
					.append("Dimensions: ").append(this.radius*2).append('x').append(this.radius*2).append(" pixels").append(newLine).append(newLine);

			builder.append("This may take a long time. You may cancel the process at any time.").append(newLine)
			.append("Continue?");


			final boolean cont = StartupQuery.confirm(builder.toString());
			if(cont) {
				final GuiGeneratingBiomeLayoutImage gui = new GuiGeneratingBiomeLayoutImage();
				this.mc.displayGuiScreen(gui);
				new Thread(() -> new BiomePainter(GuiBiomeLayoutImage.this.worldName, worldsettings)
						.paintImage(GuiBiomeLayoutImage.this.radius, new ChunkPos(GuiBiomeLayoutImage.this.xCoord, GuiBiomeLayoutImage.this.yCoord), imageFile, gui)).start();
			}else
				this.mc.displayGuiScreen(this);

		}else if(button.id == 3) {
			++this.selectedIndex;

			if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
				this.selectedIndex = 0;

			while (!this.canSelectCurWorldType())
			{
				++this.selectedIndex;

				if (this.selectedIndex >= WorldType.WORLD_TYPES.length)
					this.selectedIndex = 0;
			}

			this.updateDisplayState();
		}
	}

	private boolean canSelectCurWorldType()
	{
		final WorldType worldtype = WorldType.WORLD_TYPES[this.selectedIndex];

		if (worldtype != null && worldtype.canBeCreated())
			return worldtype == WorldType.DEBUG_ALL_BLOCK_STATES ? GuiScreen.isShiftKeyDown() : true;
			else
				return false;
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException
	{
		if (this.worldNameField.isFocused()){
			this.worldNameField.textboxKeyTyped(typedChar, keyCode);
			this.worldName = this.worldNameField.getText();
		}else if (this.worldSeedField.isFocused()){
			this.worldSeedField.textboxKeyTyped(typedChar, keyCode);
			this.worldSeed = this.worldSeedField.getText();
		}else if(this.radiusField.isFocused()) {
			this.radiusField.textboxKeyTyped(typedChar, keyCode);
			this.radius = this.radiusField.getText().isEmpty() ? 0:Integer.parseInt(this.radiusField.getText());
		}else if(this.xField.isFocused()) {
			this.xField.textboxKeyTyped(typedChar, keyCode);
			this.xCoord = this.xField.getText().isEmpty() ? 0:Integer.parseUnsignedInt(this.xField.getText());
		}else if(this.yField.isFocused()) {
			this.yField.textboxKeyTyped(typedChar, keyCode);
			this.yCoord = this.yField.getText().isEmpty() ? 0:Integer.parseUnsignedInt(this.yField.getText());
		}

		if (keyCode == 28 || keyCode == 156)
			this.actionPerformed(this.buttonList.get(0));

		(this.buttonList.get(0)).enabled = !this.worldNameField.getText().isEmpty() && this.radius > 0;
		this.calcSaveDirName();
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.worldSeedField.mouseClicked(mouseX, mouseY, mouseButton);
		this.worldNameField.mouseClicked(mouseX, mouseY, mouseButton);
		this.radiusField.mouseClicked(mouseX, mouseY, mouseButton);
		this.xField.mouseClicked(mouseX, mouseY, mouseButton);
		this.yField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Biome Layout Image Setup", this.width / 2, 20, -1);

		this.drawString(this.fontRenderer, I18n.format("selectWorld.enterSeed"), this.width / 2 - 100, 98, -6250336);
		this.drawString(this.fontRenderer, I18n.format("selectWorld.seedInfo"), this.width / 2 - 100, 136, -6250336);
		this.worldSeedField.drawTextBox();

		this.drawString(this.fontRenderer, I18n.format("selectWorld.enterName"), this.width / 2 - 100, 47, -6250336);
		this.drawString(this.fontRenderer, "Image will be saved as "+this.saveDirName+"-layout.png", this.width / 2 - 100, 85, -6250336);
		this.worldNameField.drawTextBox();

		this.radiusField.drawTextBox();
		this.xField.drawTextBox();
		this.yField.drawTextBox();

		final int y = 173+this.fontRenderer.FONT_HEIGHT + ((this.height-28)-(173+this.fontRenderer.FONT_HEIGHT))/2-this.fontRenderer.FONT_HEIGHT/2;
		final int width = (150-this.fontRenderer.getStringWidth("X:")-this.fontRenderer.getStringWidth("Y:")-30)/2;

		this.drawString(this.fontRenderer, "Radius (chunks):", this.width/2-155, y, 14737632);

		this.drawString(this.fontRenderer, "X:", this.width/2+5, y, 14737632);
		this.drawString(this.fontRenderer, "Y:", this.width/2+5+this.fontRenderer.getStringWidth("X:")+10+width+10, y, 14737632);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
