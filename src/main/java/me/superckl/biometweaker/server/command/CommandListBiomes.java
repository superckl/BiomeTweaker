package me.superckl.biometweaker.server.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class CommandListBiomes implements ICommand{

	private final List<String> aliases = Arrays.asList("btlistbiomes", "btbiomes", "biometweakerbiomes", "btb", "biometweakerb");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTListBiomes";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return LanguageRegistry.instance().getStringLocalization("biometweaker.msg.listbiomes.usage.text");
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] p_71515_2_) {

		sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.listbiomes.output.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));

		for(final BiomeGenBase gen:BiomeGenBase.getBiomeGenArray())
			if(gen != null){
				final String message = new StringBuilder().append(gen.biomeID).append(" - ").append(gen.biomeName).toString();
				sender.addChatMessage(new ChatComponentText(message).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
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
