package me.superckl.biometweaker.server.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.Output;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class CommandOutput {

	public static int output(final CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		Output.generateOutputFiles(ctx.getSource().getServer().registryAccess(),
				ctx.getSource().getServer().getWorldData().worldGenSettings().dimensions(), BiomeTweaker.getINSTANCE().getOutputDir());
		ctx.getSource().sendSuccess(Component.literal("Regenerated output files."), true);
		return 0;
	}

}
