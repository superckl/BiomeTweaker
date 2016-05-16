package me.superckl.biometweaker.server.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

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
		return "biometweaker.msg.reloadscript.usage.text";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if(args.length != 1){
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.usage.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		try {
			final File operateIn = Config.INSTANCE.getWhereAreWe();
			final File scriptFile = new File(operateIn, args[0]);
			if(!scriptFile.exists() || !scriptFile.isFile()){
				sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.nofile.text", scriptFile.getName()).setChatStyle(new Style().setColor(TextFormatting.RED)));
				return;
			}
			BiomeTweaker.getInstance().parseScript(scriptFile);
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.success.text", scriptFile.getName()).setChatStyle(new Style().setColor(TextFormatting.AQUA)));
		} catch (final Exception e) {
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.failure.text", args[0]).setChatStyle(new Style().setColor(TextFormatting.RED)));
			LogHelper.error(String.format("Failed to reload script %s!", args[0]));
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return sender.canCommandSenderUseCommand(server.getOpPermissionLevel(), this.getCommandName());

	}

	@Override
	public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args,
			final BlockPos pos) {
		return null;
	}

}
