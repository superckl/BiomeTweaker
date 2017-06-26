package me.superckl.biometweaker.server.command;

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

public class CommandReload implements ICommand{

	private final List<String> aliases = Arrays.asList("btreload", "biometweakerreload", "btr", "biometweakerr");

	@Override
	public int compareTo(final ICommand c) {
		return this.getName().compareTo(c.getName());
	}

	@Override
	public String getName() {
		return "BTReload";
	}

	@Override
	public String getUsage(final ICommandSender p_71518_1_) {
		return "biometweaker.msg.reload.usage.text";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public boolean isUsernameIndex(final String[] p_82358_1_, final int p_82358_2_) {
		return false;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		try {
			BiomeTweaker.getInstance().getConfig().loadValues();
			BiomeTweaker.getInstance().getCommandManager().reset();
			BiomeTweaker.getInstance().parseScripts();
			sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reload.success.text").setStyle(new Style().setColor(TextFormatting.AQUA)));
		} catch (final Exception e) {
			sender.sendMessage(new TextComponentTranslation("biometweaker.msg.reload.failure.text").setStyle(new Style().setColor(TextFormatting.RED)));
			LogHelper.error("Failed to reload scripts!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return sender.canUseCommand(server.getOpPermissionLevel(), this.getName());
	}

	@Override
	public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, final BlockPos pos) {
		return null;
	}

}
