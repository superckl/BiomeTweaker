package me.superckl.api.biometweaker.script.wrapper;

import me.superckl.api.biometweaker.block.BasicBlockStateBuilder;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.object.BlockReplacementScriptObject;
import me.superckl.api.biometweaker.script.object.BlockStateScriptObject;
import me.superckl.api.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.api.biometweaker.script.pack.AllButBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BasicResourceNameBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BiomePackage;
import me.superckl.api.biometweaker.script.pack.IntersectBiomesPackage;
import me.superckl.api.biometweaker.script.pack.PropertyRangeBiomePackage;
import me.superckl.api.biometweaker.script.pack.SubtractBiomesPackage;
import me.superckl.api.biometweaker.script.pack.TypeBiomesPackage;
import me.superckl.api.biometweaker.world.gen.ReplacementConstraints;
import me.superckl.api.superscript.script.ParameterType;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.script.ScriptParser;
import me.superckl.api.superscript.script.object.ScriptObject;
import net.minecraft.resources.ResourceLocation;

public class BTParameterTypes {

	public static final ParameterType<BiomePackage> BASIC_BIOMES_PACKAGE = new ParameterType<BiomePackage>(BiomePackage.class) {

		@Override
		public BiomePackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof BiomePackScriptObject)
				return ((BiomePackScriptObject)obj).getPack();
			final String rLoc = ParameterTypes.STRING.tryParse(parameter, handler);
			if(rLoc == null || rLoc.isEmpty())
				return null;
			return new BasicResourceNameBiomesPackage(rLoc);

		}
	};

	public static final ParameterType<TypeBiomesPackage> TYPE_BIOMES_PACKAGE = new ParameterType<TypeBiomesPackage>(TypeBiomesPackage.class, new TypesPackParameterWrapper()) {

		@Override
		public TypeBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new TypeBiomesPackage(ParameterTypes.STRING.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<AllBiomesPackage> ALL_BIOMES_PACKAGE = new ParameterType<AllBiomesPackage>(AllBiomesPackage.class, new NoArgsParameterWrapper<>(AllBiomesPackage.class)) {

		@Override
		public AllBiomesPackage tryParse(final String parameter, final ScriptHandler handler) {
			return new AllBiomesPackage();
		}
	};

	public static final ParameterType<AllButBiomesPackage> ALL_BUT_BIOMES_PACKAGE = new ParameterType<AllButBiomesPackage>(AllButBiomesPackage.class, new AllButPackParameterWrapper()) {

		@Override
		public AllButBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new AllButBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<IntersectBiomesPackage> INTERSECT_BIOMES_PACKAGE = new ParameterType<IntersectBiomesPackage>(IntersectBiomesPackage.class, new IntersectPackParameterWrapper()) {

		@Override
		public IntersectBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new IntersectBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<SubtractBiomesPackage> SUBTRACT_BIOMES_PACKAGE = new ParameterType<SubtractBiomesPackage>(SubtractBiomesPackage.class, new SubtractPackParameterWrapper()) {

		@Override
		public SubtractBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new SubtractBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler), new BasicResourceNameBiomesPackage());
		}
	};

	public static final ParameterType<PropertyRangeBiomePackage> PROPERTY_RANGE_PACKAGE = new ParameterType<PropertyRangeBiomePackage>(PropertyRangeBiomePackage.class, new PropertyRangePackParameterWrapper()) {

		@Override
		public PropertyRangeBiomePackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			throw new UnsupportedOperationException();
		}
	};

	@SuppressWarnings("rawtypes")
	public static final ParameterType<BlockStateBuilder> BLOCKSTATE_BUILDER = new ParameterType<BlockStateBuilder>(BlockStateBuilder.class) {

		@Override
		public BlockStateBuilder<?> tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			if(ScriptParser.isStringArg(parameter)){
				final BasicBlockStateBuilder builder = new BasicBlockStateBuilder();
				builder.setrLoc(new ResourceLocation(ParameterTypes.STRING.tryParse(parameter, handler)));
				return builder;
			}
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof BlockStateScriptObject)
				return ((BlockStateScriptObject<?>) obj).getBuilder();
			return null;
		}
	};

	public static final ParameterType<ReplacementConstraints> REPLACEMENT_CONSTRAINT = new ParameterType<ReplacementConstraints>(ReplacementConstraints.class) {

		@Override
		public ReplacementConstraints tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final BlockStateBuilder<?> builder = BTParameterTypes.BLOCKSTATE_BUILDER.tryParse(parameter, handler);
			if(builder != null) {
				final ReplacementConstraints constraints = new ReplacementConstraints();
				constraints.setBuilder(builder);
				return constraints;
			}
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof BlockReplacementScriptObject)
				return ((BlockReplacementScriptObject) obj).getConstraints();
			return null;
		}
	};

	static {
		ParameterTypes.registerDefaultType(BlockStateBuilder.class, BTParameterTypes.BLOCKSTATE_BUILDER);
		ParameterTypes.registerDefaultType(BiomePackage.class, BTParameterTypes.BASIC_BIOMES_PACKAGE);
		ParameterTypes.registerDefaultType(ReplacementConstraints.class, BTParameterTypes.REPLACEMENT_CONSTRAINT);
	}

}
