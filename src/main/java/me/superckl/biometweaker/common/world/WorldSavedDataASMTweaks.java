package me.superckl.biometweaker.common.world;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.common.reference.ModData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class WorldSavedDataASMTweaks extends WorldSavedData{

	private static final String DATA_NAME = ModData.MOD_ID+"_asmtweakdata";

	@Getter
	private final Set<String> tweaks = new HashSet<>();
	@Getter
	private boolean wasntSaved = false;

	public WorldSavedDataASMTweaks(final String s) {
		super(s);
		this.wasntSaved = false;
	}

	public WorldSavedDataASMTweaks(final boolean wasntSaved){
		super(WorldSavedDataASMTweaks.DATA_NAME);
		this.wasntSaved = wasntSaved;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		this.tweaks.clear();
		final NBTTagList list = nbt.getTagList("asmtweaks", NBT.TAG_STRING);
		for(int i = 0; i < list.tagCount(); i++)
			this.tweaks.add(list.getStringTagAt(i));
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		final NBTTagList list = new NBTTagList();
		BiomeTweaker.getInstance().getEnabledTweaks().forEach(t -> {list.appendTag(new NBTTagString(t));});
		compound.setTag("asmtweaks", list);
		return compound;
	}

	public static WorldSavedDataASMTweaks get(final World world){
		WorldSavedDataASMTweaks data = (WorldSavedDataASMTweaks) world.getMapStorage().getOrLoadData(WorldSavedDataASMTweaks.class, WorldSavedDataASMTweaks.DATA_NAME);
		if(data == null){
			data = new WorldSavedDataASMTweaks(true);
			world.getMapStorage().setData(WorldSavedDataASMTweaks.DATA_NAME, data);
		}
		return data;
	}

}
