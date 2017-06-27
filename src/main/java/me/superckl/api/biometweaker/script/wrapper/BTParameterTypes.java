package me.superckl.api.biometweaker.script.wrapper;

import java.util.List;

import me.superckl.api.biometweaker.block.BasicBlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.object.BlockStateScriptObject;
import me.superckl.api.biometweaker.script.object.DecorationBuilderScriptObject;
import me.superckl.api.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.api.biometweaker.script.pack.AllButBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BasicBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BasicResourceNameBiomesPackage;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.IntersectBiomesPackage;
import me.superckl.api.biometweaker.script.pack.SubtractBiomesPackage;
import me.superckl.api.biometweaker.script.pack.TypeBiomesPackage;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.ScriptParser;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ParameterType;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.util.ResourceLocation;

public class BTParameterTypes {

	public static final ParameterType SPAWN_TYPE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final String extracted = (String) ParameterTypes.STRING.tryParse(parameter, handler);
			if(extracted != null)
				return SpawnListType.valueOf(extracted);
			return null;
		}
	};

	public static final ParameterType BASIC_BIOMES_PACKAGE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final List<Integer> ints = WarningHelper.uncheckedCast(ParameterTypes.NON_NEG_INTEGERS.tryParse(parameter, handler));
			if(ints.isEmpty()){
				final ScriptObject obj = handler.getObjects().get(parameter);
				if(obj != null && obj instanceof BiomePackScriptObject)
					return ((BiomePackScriptObject)obj).getPack();
				else{
					final String rLoc = (String) ParameterTypes.STRING.tryParse(parameter, handler);
					if(rLoc == null || rLoc.isEmpty())
						return null;
					return new BasicResourceNameBiomesPackage(rLoc);
				}
			}
			final int[] array = new int[ints.size()];
			for(int i = 0; i < array.length; i++)
				array[i] = ints.get(i);
			return new BasicBiomesPackage(array);
		}
	};

	public static final ParameterType TYPE_BIOMES_PACKAGE = new ParameterType(new TypesPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new TypeBiomesPackage((String) ParameterTypes.STRING.tryParse(parameter, handler));
		}
	};

	public static final ParameterType ALL_BIOMES_PACKAGE = new ParameterType(new NoArgsParameterWrapper<>(AllBiomesPackage.class)) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			return new AllBiomesPackage();
		}
	};

	public static final ParameterType ALL_BUT_BIOMES_PACKAGE = new ParameterType(new AllButPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new AllButBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType INTERSECT_BIOMES_PACKAGE = new ParameterType(new IntersectPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new IntersectBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType SUBTRACT_BIOMES_PACKAGE = new ParameterType(new SubtractPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new SubtractBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler), new BasicBiomesPackage());
		}
	};

	public static final ParameterType PROPERTY_RANGE_PACKAGE = new ParameterType(new PropertyRangePackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			throw new UnsupportedOperationException();
		}
	};

	public static final ParameterType WORLD_GENERATOR_BUILDER = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj != null && obj instanceof DecorationBuilderScriptObject)
				return ((DecorationBuilderScriptObject<?>)obj).getBuilder();
			return null;
		}
	};

	public static final ParameterType BLOCKSTATE_BUILDER = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			if(ScriptParser.isStringArg(parameter)){
				final BasicBlockStateBuilder builder = new BasicBlockStateBuilder();
				builder.setrLoc(new ResourceLocation((String) ParameterTypes.STRING.tryParse(parameter, handler)));
				return builder;
			}
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj != null && obj instanceof BlockStateScriptObject)
				return ((BlockStateScriptObject<?>) obj).getBuilder();
			return null;
		}
	};

}
