package me.superckl.biometweaker.server.command;

import java.io.IOException;
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

public class CommandOutput implements ICommand{

	private final List<String> aliases = Arrays.asList("btoutput", "biometweakeroutput", "bto", "biometweakero");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTOutput";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return "biometweaker.msg.output.usage.text";
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
		try {
			BiomeTweaker.getInstance().generateOutputFiles();
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.output.success.text").setStyle(new Style().setColor(TextFormatting.AQUA)));
		} catch (final IOException e) {
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.output.failure.text").setStyle(new Style().setColor(TextFormatting.RED)));
			LogHelper.error("Failed to regenerate output files!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return sender.canCommandSenderUseCommand(server.getOpPermissionLevel(), this.getCommandName());
	}

	@Override
	public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, final BlockPos pos) {
		return null;
	}

}
