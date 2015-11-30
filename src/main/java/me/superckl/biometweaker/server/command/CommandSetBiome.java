package me.superckl.biometweaker.server.command;

import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Ints;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class CommandSetBiome implements ICommand{

	private final List<String> aliases = Arrays.asList("btsetbiome", "biometweakersetbiome", "bts", "biometweakers");

	@Override
	public int compareTo(final ICommand c) {
		return this.getCommandName().compareTo(c.getCommandName());
	}

	@Override
	public String getCommandName() {
		return "BTSetBiome";
	}

	@Override
	public String getCommandUsage(final ICommandSender p_71518_1_) {
		return LanguageRegistry.instance().getStringLocalization("biometweaker.msg.setbiome.usage.text");
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender sender, final String[] args) {
		final BlockPos coord = sender.getPosition();
		final World world = sender.getEntityWorld();
		if((coord != null) && (world != null)){
			if((args.length < 2) || (args.length > 3)){
				sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			BiomeGenBase gen = null;
			Integer i = Ints.tryParse(args[0]);
			if(i != null)
				gen = BiomeGenBase.getBiome(i);
			else
				for(final BiomeGenBase biome:BiomeGenBase.getBiomeGenArray())
					if((biome != null) && biome.biomeName.equals(args[0])){
						gen = biome;
						break;
					}
			if(gen == null){
				sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			i = Ints.tryParse(args[1]);
			if(i == null){
				sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				return;
			}
			boolean blocks = true;
			if(args.length == 3)
				if(args[2].equalsIgnoreCase("block"))
					blocks = true;
				else if(args[2].equalsIgnoreCase("chunk"))
					blocks = false;
				else{
					sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.invalargs.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					return;
				}
			int count = 0;
			if(blocks){
				for(int x = coord.getX()-i; x <= (coord.getX()+i); x++)
					for(int z = coord.getZ()-i; z <= (coord.getZ()+i); z++){
						final int realX = x & 15;
						final int realZ = z & 15;
						/*if(x < 0)
							realX = 15-realX;
						if(z < 0)
							realZ = 15-realZ;*/
						final Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
						chunk.getBiomeArray()[(realZ*16)+realX] = (byte) gen.biomeID;
						chunk.setChunkModified();
						count++;
					}
				sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.blocksuccess.text", count, gen.biomeName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
			}else{
				final byte[] biomeArray = new byte[256];
				Arrays.fill(biomeArray, (byte) gen.biomeID);
				final int chunkX = coord.getX() >> 4;
					final int chunkZ = coord.getZ() >> 4;
					for(int x = chunkX-i; x <= (chunkX+i); x++)
						for(int z = chunkZ-i; z <= (chunkZ+i); z++){
							final Chunk chunk = world.getChunkFromChunkCoords(x, z);
							chunk.setBiomeArray(Arrays.copyOf(biomeArray, biomeArray.length));
							chunk.setChunkModified();
							count++;
						}
					sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.setbiome.chunksuccess.text", count, gen.biomeName).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
			}
		}else
			sender.addChatMessage(new ChatComponentTranslation("biometweaker.msg.info.invalsender.text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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
