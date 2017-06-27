package me.superckl.biometweaker.script.command.generation;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.event.BiomeTweakEvent;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.util.BlockEquivalencePredicate;
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeTweaker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.FlowerEntry;
import net.minecraftforge.common.MinecraftForge;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptCommandAddRemoveBiomeFlower implements IScriptCommand{

	private static Field field;

	private final IBiomePackage pack;
	private final boolean remove;
	private final BlockStateBuilder<?> block;
	private final int weight;

	public ScriptCommandAddRemoveBiomeFlower(final IBiomePackage pack, final BlockStateBuilder<?> block) {
		this(pack, true, block, 0);
	}

	public ScriptCommandAddRemoveBiomeFlower(final IBiomePackage pack, final BlockStateBuilder<?> block, final int weight) {
		this(pack, false, block, weight);
	}

	@Override
	public void perform() throws Exception {
		if(this.remove){
			if(ScriptCommandAddRemoveBiomeFlower.field == null){
				ScriptCommandAddRemoveBiomeFlower.field = Biome.class.getDeclaredField("flowers");
				ScriptCommandAddRemoveBiomeFlower.field.setAccessible(true);
			}
			final Iterator<Biome> it = this.pack.getIterator();
			while(it.hasNext()){
				final Biome gen = it.next();
				if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.RemoveFlower(this, gen, this.block)))
					continue;
				final List<FlowerEntry> flowers = WarningHelper.uncheckedCast(ScriptCommandAddRemoveBiomeFlower.field.get(gen));
				final Iterator<FlowerEntry> itF = flowers.iterator();
				final Predicate<IBlockState> predicate = new BlockEquivalencePredicate(this.block.build());
				while(itF.hasNext()){
					final FlowerEntry entry = itF.next();
					if(predicate.apply(entry.state))
						itF.remove();
				}
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
			}
		}else{
			final Iterator<Biome> it = this.pack.getIterator();
			while(it.hasNext()){
				final Biome gen = it.next();
				if(MinecraftForge.EVENT_BUS.post(new BiomeTweakEvent.AddFlower(this, gen, this.block, this.weight)))
					continue;
				gen.addFlower(this.block.build(), this.weight);
				BiomeTweaker.getInstance().onTweak(Biome.getIdForBiome(gen));
			}
		}
	}

}
