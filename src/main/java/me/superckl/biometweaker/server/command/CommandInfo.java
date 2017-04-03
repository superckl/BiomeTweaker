package me.superckl.biometweaker.server.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.superckl.biometweaker.util.BiomeHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class CommandInfo implements ICommand{

	private final List<String> aliases = Arrays.asList("btinfo", "biometweakerinfo", "bti", "biometweakeri");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTInfo";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return "biometweaker.msg.info.usage.text";
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
		final BlockPos coord = sender.getPosition();
		final World world = sender.getEntityWorld();
		if((coord != null) && (world != null)){
			final JsonObject obj = BiomeHelper.fillJsonObject(world.getBiome(coord), coord.getX(), coord.getY(), coord.getZ());
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.info.output.text").setStyle(new Style().setColor(TextFormatting.AQUA)));
			final Style gold = new Style().setColor(TextFormatting.GOLD);
			final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			for(final Entry<String, JsonElement> entry:obj.entrySet())
				if(entry.getValue().isJsonArray())
					sender.addChatMessage(new TextComponentString(entry.getKey()+": Check the output files.").setStyle(gold)); //It looks hideous in MC chat.
				else
					sender.addChatMessage(new TextComponentString(entry.getKey()+": "+gson.toJson(entry.getValue())).setStyle(gold));
		}else
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.info.invalsender.text").setStyle(new Style().setColor(TextFormatting.RED)));
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
