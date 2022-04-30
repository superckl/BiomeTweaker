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
import me.superckl.api.superscript.util.WarningHelper;
import me.superckl.biometweaker.BiomeModificationManager;
import me.superckl.biometweaker.BiomeModificationManager.MobEffectModification;
import me.superckl.biometweaker.script.object.effect.MobEffectScriptObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;

public class BTParameterTypes {

	public static final ParameterType<BiomePackage> BASIC_BIOMES_PACKAGE = new ParameterType<>(BiomePackage.class) {

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

	public static final ParameterType<TypeBiomesPackage> TYPE_BIOMES_PACKAGE = new ParameterType<>(TypeBiomesPackage.class, new TypesPackParameterWrapper()) {

		@Override
		public TypeBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new TypeBiomesPackage(ParameterTypes.STRING.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<AllBiomesPackage> ALL_BIOMES_PACKAGE = new ParameterType<>(AllBiomesPackage.class, new NoArgsParameterWrapper<>(AllBiomesPackage.class)) {

		@Override
		public AllBiomesPackage tryParse(final String parameter, final ScriptHandler handler) {
			return new AllBiomesPackage();
		}
	};

	public static final ParameterType<AllButBiomesPackage> ALL_BUT_BIOMES_PACKAGE = new ParameterType<>(AllButBiomesPackage.class, new AllButPackParameterWrapper()) {

		@Override
		public AllButBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new AllButBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<IntersectBiomesPackage> INTERSECT_BIOMES_PACKAGE = new ParameterType<>(IntersectBiomesPackage.class, new IntersectPackParameterWrapper()) {

		@Override
		public IntersectBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new IntersectBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType<SubtractBiomesPackage> SUBTRACT_BIOMES_PACKAGE = new ParameterType<>(SubtractBiomesPackage.class, new SubtractPackParameterWrapper()) {

		@Override
		public SubtractBiomesPackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new SubtractBiomesPackage(BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler), new BasicResourceNameBiomesPackage());
		}
	};

	public static final ParameterType<PropertyRangeBiomePackage> PROPERTY_RANGE_PACKAGE = new ParameterType<>(PropertyRangeBiomePackage.class, new PropertyRangePackParameterWrapper()) {

		@Override
		public PropertyRangeBiomePackage tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			throw new UnsupportedOperationException();
		}
	};

	public static final ParameterType<BlockStateBuilder<?>> BLOCKSTATE_BUILDER = new ParameterType<>(WarningHelper.uncheckedCast(BlockStateBuilder.class)) {

		@Override
		public BlockStateBuilder<?> tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			if(ScriptParser.isStringArg(parameter)){
				final BasicBlockStateBuilder builder = new BasicBlockStateBuilder();
				builder.setrLoc(new ResourceLocation(ParameterTypes.STRING.tryParse(parameter, handler)));
				return builder;
			}
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof final BlockStateScriptObject<?> blockObj)
				return blockObj.getBuilder();
			return null;
		}
	};

	public static final ParameterType<ReplacementConstraints> REPLACEMENT_CONSTRAINT = new ParameterType<>(ReplacementConstraints.class) {

		@Override
		public ReplacementConstraints tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final BlockStateBuilder<?> builder = BTParameterTypes.BLOCKSTATE_BUILDER.tryParse(parameter, handler);
			if(builder != null) {
				final ReplacementConstraints constraints = new ReplacementConstraints();
				constraints.builder(builder);
				return constraints;
			}
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof final BlockReplacementScriptObject blockObj)
				return blockObj.getConstraints();
			return null;
		}
	};

	public static final ParameterType<MobEffectModification.Builder> MOB_EFFECT_BUILDER = new ParameterType<>(MobEffectModification.Builder.class) {

		@Override
		public MobEffectModification.Builder tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			if(ScriptParser.isStringArg(parameter))
				return MobEffectModification.builder(new ResourceLocation(ParameterTypes.STRING.tryParse(parameter, handler)));
			final ScriptObject obj = handler.getObjects().get(parameter);
			if(obj instanceof final MobEffectScriptObject effectObj)
				return effectObj.getBuilder();
			return null;
		}
	};

	public static final ParameterType<ResourceKey<Biome>> BIOME_RESOURCE_KEY = new ParameterType<>(WarningHelper.uncheckedCast(ResourceKey.class)) {

		@Override
		public ResourceKey<Biome> tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(parameter));
		}
	};

	public static final ParameterType<Biome.Precipitation> PRECIPITATION = new ParameterType<>(Biome.Precipitation.class) {

		@Override
		public Precipitation tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return Precipitation.byName(ParameterTypes.STRING.tryParse(parameter, handler).toLowerCase());
		}
	};

	public static final ParameterType<Biome.TemperatureModifier> TEMPERATURE_MODIFIER= new ParameterType<>(Biome.TemperatureModifier.class) {

		@Override
		public TemperatureModifier tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return TemperatureModifier.byName(ParameterTypes.STRING.tryParse(parameter, handler).toLowerCase());
		}
	};

	public static final ParameterType<BiomeSpecialEffects.GrassColorModifier> GRASS_COLOR_MODIFIER= new ParameterType<>(BiomeSpecialEffects.GrassColorModifier.class) {

		@Override
		public GrassColorModifier tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return GrassColorModifier.byName(ParameterTypes.STRING.tryParse(parameter, handler).toLowerCase());
		}
	};

	public static final ParameterType<BiomeCategory> BIOME_CATEGORY = new ParameterType<>(BiomeCategory.class) {

		@Override
		public BiomeCategory tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return BiomeCategory.byName(ParameterTypes.STRING.tryParse(parameter, handler).toLowerCase());
		}
	};

	public static final ParameterType<BiomeModificationManager.FogShape> FOG_SHAPE = new ParameterType<>(BiomeModificationManager.FogShape.class) {

		@Override
		public BiomeModificationManager.FogShape tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return BiomeModificationManager.FogShape.valueOf(ParameterTypes.STRING.tryParse(parameter, handler).toUpperCase());
		}
	};

	public static final ParameterType<ResourceLocation> RESOURCE_LOCATION = new ParameterType<>(ResourceLocation.class) {

		@Override
		public ResourceLocation tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new ResourceLocation(ParameterTypes.STRING.tryParse(parameter, handler).toLowerCase());
		}
	};

	static {
		ParameterTypes.registerDefaultType(BlockStateBuilder.class, BTParameterTypes.BLOCKSTATE_BUILDER);
		ParameterTypes.registerDefaultType(BiomePackage.class, BTParameterTypes.BASIC_BIOMES_PACKAGE);
		ParameterTypes.registerDefaultType(ReplacementConstraints.class, BTParameterTypes.REPLACEMENT_CONSTRAINT);
		ParameterTypes.registerDefaultType(MobEffectModification.Builder.class, BTParameterTypes.MOB_EFFECT_BUILDER);
		ParameterTypes.registerDefaultType(Biome.Precipitation.class, BTParameterTypes.PRECIPITATION);
		ParameterTypes.registerDefaultType(Biome.TemperatureModifier.class, BTParameterTypes.TEMPERATURE_MODIFIER);
		ParameterTypes.registerDefaultType(BiomeSpecialEffects.GrassColorModifier.class, BTParameterTypes.GRASS_COLOR_MODIFIER);
		ParameterTypes.registerDefaultType(BiomeCategory.class, BTParameterTypes.BIOME_CATEGORY);
		ParameterTypes.registerDefaultType(ResourceLocation.class, BTParameterTypes.RESOURCE_LOCATION);
		ParameterTypes.registerDefaultType(BiomeModificationManager.FogShape.class, BTParameterTypes.FOG_SHAPE);
	}

}
