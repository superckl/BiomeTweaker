package me.superckl.biometweaker.server.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.BiomeGenBase;

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
		return I18n.format("biometweaker.msg.listbiomes.usage.text");
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
		sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.listbiomes.output.text").setChatStyle(new Style().setColor(TextFormatting.AQUA)));

		final Iterator<BiomeGenBase> it = BiomeGenBase.biomeRegistry.iterator();
		while(it.hasNext()){
			final BiomeGenBase gen = it.next();
			if(gen != null){
				final String message = new StringBuilder().append(BiomeGenBase.getIdForBiome(gen)).append(" - ").append(gen.getBiomeName()).toString();
				sender.addChatMessage(new TextComponentString(message).setChatStyle(new Style().setColor(TextFormatting.GOLD)));
			}
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
