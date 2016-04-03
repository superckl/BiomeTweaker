package me.superckl.biometweaker.server.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xml.internal.security.utils.I18n;

import lombok.Cleanup;
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

public class CommandReload implements ICommand{

	private final List<String> aliases = Arrays.asList("btreload", "biometweakerreload", "btr", "biometweakerr");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTReload";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return I18n.translate("biometweaker.msg.reload.usage.text");
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
			final File operateIn = Config.INSTANCE.getWhereAreWe();
			final File mainConfig = new File(operateIn, "BiomeTweaker.cfg");
			@Cleanup
			final
			BufferedReader reader = new BufferedReader(new FileReader(mainConfig));
			final JsonObject obj = (JsonObject) new JsonParser().parse(reader);
			if(obj.entrySet().isEmpty())
				LogHelper.warn("The configuration file read as empty! BiomeTweaker isn't going to do anything.");
			Config.INSTANCE.init(operateIn, obj);
			BiomeTweaker.getInstance().parseScripts();
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reload.success.text").setChatStyle(new Style().setColor(TextFormatting.AQUA)));
		} catch (final Exception e) {
			sender.addChatMessage(new TextComponentTranslation("biometweaker.msg.reload.failure.text").setChatStyle(new Style().setColor(TextFormatting.RED)));
			LogHelper.error("Failed to reload scripts!");
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
