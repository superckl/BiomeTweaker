package me.superckl.biometweaker.server.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class CommandReloadScript implements ICommand{

	private final List<String> aliases = Arrays.asList("btreloadscript", "biometweakerreloadscript", "btrs", "btrscript", "biometweakerrs");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTReloadScript";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return LanguageRegistry.instance().getStringLocalization("biometweaker.msg.reloadscript.usage.text");
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) {
		if(args.length != 1){
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reloadscript.usage.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			return;
		}
		try {
			final File operateIn = Config.INSTANCE.getWhereAreWe();
			final File scriptFile = new File(operateIn, args[0]);
			if(!scriptFile.exists() || !scriptFile.isFile()){
				sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reloadscript.nofile.text", scriptFile.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			BiomeTweaker.getInstance().parseScript(scriptFile);
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reloadscript.success.text", scriptFile.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
		} catch (final Exception e) {
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.reloadscript.failure.text", args[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			LogHelper.error(String.format("Failed to reload script %s!", args[0]));
			e.printStackTrace();
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender sender) {
		return sender.canCommandSenderUseCommand(MinecraftServer.getServer().getOpPermissionLevel(), this.getCommandName());
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

	@Override
	public List addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
		return null;
	}

}
