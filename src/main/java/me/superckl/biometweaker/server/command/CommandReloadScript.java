package me.superckl.biometweaker.server.command;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import me.superckl.biometweaker.BiomeTweaker;
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
		return this.getName().compareTo(c.getName());
	}

	@Override
	public String getName() {
		return "BTReloadScript";
	}

	@Override
	public String getUsage(final ICommandSender p_71518_1_) {
		return "biometweaker.msg.reloadscript.usage.text";
	}

	@Override
	public List getAliases() {
		return this.aliases;
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if(args.length != 1){
			sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.usage.text").setStyle(new Style().setColor(TextFormatting.RED)));
			return;
		}
		try {
			final File operateIn = BiomeTweaker.getInstance().getConfig().getWhereAreWe();
			final File scriptFile = new File(operateIn, args[0]);
			if(!scriptFile.exists() || !scriptFile.isFile()){
				sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.nofile.text", scriptFile.getName()).setStyle(new Style().setColor(TextFormatting.RED)));
				return;
			}
			BiomeTweaker.getInstance().parseScript(scriptFile);
			sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.success.text", scriptFile.getName()).setStyle(new Style().setColor(TextFormatting.AQUA)));
		} catch (final Exception e) {
			sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reloadscript.failure.text", args[0]).setStyle(new Style().setColor(TextFormatting.RED)));
			LogHelper.error(String.format("Failed to reload script %s!", args[0]));
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return sender.canUseCommand(server.getOpPermissionLevel(), this.getName());

	}

	@Override
	public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args,
			final BlockPos pos) {
		return null;
	}

}
