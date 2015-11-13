package me.superckl.biometweaker.server.command;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class CommandOutput implements ICommand{

	private final List<String> aliases = Arrays.asList("btoutput", "biometweakeroutput", "bto", "biometweakero");

	@Override
	public int compareTo(final Object o) {
		if((o instanceof ICommand) == false)
			return 0;
		return this.getCommandName().compareTo(((ICommand)o).getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTOutput";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return LanguageRegistry.instance().getStringLocalization("biometweaker.msg.output.usage.text");
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] p_71515_2_) {
		try {
			BiomeTweaker.getInstance().generateOutputFiles();
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.output.success.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
		} catch (final IOException e) {
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.output.failure.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			LogHelper.error("Failed to regenerate output files!");
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
