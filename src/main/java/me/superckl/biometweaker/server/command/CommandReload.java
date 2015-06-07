package me.superckl.biometweaker.server.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import lombok.Cleanup;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.BiomeTweakerCore;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommandReload implements ICommand{

	private final List<String> aliases = Arrays.asList("btreload", "biometweakerreload", "btr", "biometweakerr");

	@Override
	public int compareTo(final Object o) {
		if((o instanceof ICommand) == false)
			return 0;
		return this.getCommandName().compareTo(((ICommand)o).getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTReload";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return LanguageRegistry.instance().getStringLocalization("biometweaker.msg.reload.usage.text");
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] p_71515_2_) {
		try {
			final File operateIn = new File(BiomeTweakerCore.mcLocation, "config/BiomeTweaker/");
			final File mainConfig = new File(operateIn, "BiomeTweaker.cfg");
			@Cleanup
			final
			BufferedReader reader = new BufferedReader(new FileReader(mainConfig));
			final JsonObject obj = (JsonObject) new JsonParser().parse(reader);
			if(obj.entrySet().isEmpty())
				LogHelper.warn("The configuration file read as empty! BiomeTweaker isn't going to do anything.");
			Config.INSTANCE.init(operateIn, obj);
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reload.success.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
		} catch (final Exception e) {
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reload.failure.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			LogHelper.error("Failed to reload scripts!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender sender) {
		return sender.canCommandSenderUseCommand(MinecraftServer.getServer().getOpPermissionLevel(), this.getCommandName());
	}

	@Override
	public List addTabCompletionOptions(final ICommandSender p_71516_1_, final String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

}
