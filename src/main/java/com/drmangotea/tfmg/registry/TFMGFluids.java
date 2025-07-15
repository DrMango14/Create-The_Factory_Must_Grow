package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.AcidFluidType;
import com.drmangotea.tfmg.base.fluid.AsphaltFluid;
import com.drmangotea.tfmg.base.fluid.ConcreteFluid;
import com.drmangotea.tfmg.base.fluid.HotFluidType;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.base.TFMGUtils.toHumanReadable;

public class TFMGFluids {


    public static final FluidEntry<VirtualFluid>
            LPG = gasFuel("lpg", 0xfff5e687, TFMGTags.TFMGFluidTags.LPG.tag, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            BUTANE = gasFuel("butane", 0xffad77d4, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            PROPANE = gasFuel("propane", 0xff88bf80, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            HYDROGEN = gasFuel("hydrogen", 0xffd0f2f5),
            FURNACE_GAS = gasFuel("furnace_gas", 0xff5c5555, TFMGTags.TFMGFluidTags.BLAST_STOVE_FUEL.tag, TFMGTags.TFMGFluidTags.FURNACE_GAS.tag),
            ETHYLENE = gas("ethylene", 0xffbcadcc),
            PROPYLENE = gas("propylene", 0xffc0d1b4),
            NEON = gas("neon", 0xff9dede9),
            CARBON_DIOXIDE = gas("carbon_dioxide", 0xff525252),
            AIR = gas("air", 0xffdfe6e5, TFMGTags.TFMGFluidTags.AIR.tag),
            HOT_AIR = gas("hot_air", 0xffe8e1d5);


    public static final FluidEntry<BaseFlowingFluid.Flowing>
            CRUDE_OIL = fluid("crude_oil", 0x010101, TFMGTags.TFMGFluidTags.CRUDE_OIL.tag, TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            HEAVY_OIL = fluid("heavy_oil", 0x010101, TFMGTags.TFMGFluidTags.HEAVY_OIL.tag, TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            GASOLINE = fuel("gasoline", 0xCCB17D, TFMGTags.TFMGFluidTags.GASOLINE.tag),
            DIESEL = fuel("diesel", 0xBE9C84, TFMGTags.TFMGFluidTags.DIESEL.tag, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            NAPHTHA = fuel("naphtha", 0x683525, TFMGTags.TFMGFluidTags.NAPHTHA.tag, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            KEROSENE = fuel("kerosene", 0x7C82D5, TFMGTags.TFMGFluidTags.KEROSENE.tag, TFMGTags.TFMGFluidTags.FIREBOX_FUEL.tag),
            CREOSOTE = fuel("creosote", 0x010101, TFMGTags.TFMGFluidTags.CREOSOTE.tag, TFMGTags.TFMGFluidTags.BLAST_STOVE_FUEL.tag),
            MOLTEN_STEEL = hotFluid("molten_steel", 0xFFF760, TFMGTags.TFMGFluidTags.MOLTEN_STEEL.tag),
            MOLTEN_SLAG = hotFluid("molten_slag", 0xFFF760),
            MOLTEN_PLASTIC = hotFluid("molten_plastic", 0xDEE4FF),
            LIQUID_SILICON = hotFluid("liquid_silicon", 0xFFF760),
            LUBRICATION_OIL = fluid("lubrication_oil", 0x9D945F, TFMGTags.TFMGFluidTags.LUBRICATION_OIL.tag, TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            COOLING_FLUID = fluid("cooling_fluid", 0x7BC1C1, TFMGTags.TFMGFluidTags.COOLING_FLUID.tag),
            NAPALM = fluid("napalm", 0xC0CA97),
            SULFURIC_ACID = acidFluid("sulfuric_acid", 0xE9E7CC),
            LIQUID_CONCRETE = concreteFluid("liquid_concrete", 0x5B5B59, ConcreteFluid.Source::new),
            LIQUID_ASPHALT = concreteFluid("liquid_asphalt", 0x010101, AsphaltFluid.Source::new);


    @SafeVarargs
    private static FluidEntry<BaseFlowingFluid.Flowing> fluid(String name, int fogColor, TagKey<Fluid>... tags) {
        return REGISTRATE.fluid(name, getLocation(name), getLocationFlow(name),
                        SolidRenderedPlaceableFluidType.create(fogColor, () -> 1f / 32f))
                .lang(toHumanReadable(name))
                .properties(b -> b.viscosity(1000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(10)
                        .slopeFindDistance(5)
                        .explosionResistance(100f))
                .tag(tags)
                .source(BaseFlowingFluid.Source::new)
                .bucket()
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }
    @SafeVarargs
    private static FluidEntry<BaseFlowingFluid.Flowing> acidFluid(String name, int fogColor, TagKey<Fluid>... tags) {
        return REGISTRATE.fluid(name, getLocation(name), getLocationFlow(name),
                        AcidFluidType.create(fogColor, () -> 1f / 32f))
                .lang(toHumanReadable(name))
                .properties(b -> b.viscosity(1000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(10)
                        .slopeFindDistance(5)
                        .explosionResistance(100f))
                .tag(tags)
                .source(BaseFlowingFluid.Source::new)
                .bucket()
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }

    @SafeVarargs
    private static FluidEntry<BaseFlowingFluid.Flowing> fuel(String name, int fogColor, TagKey<Fluid>... tags) {
        return REGISTRATE.fluid(name, getLocation(name), getLocationFlow(name),
                        SolidRenderedPlaceableFluidType.create(fogColor, () -> 1f / 32f))
                .lang(toHumanReadable(name))
                .properties(b -> b.viscosity(1000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(10)
                        .slopeFindDistance(5)
                        .explosionResistance(100f))
                .tag(tags)
                .tag(TFMGTags.TFMGFluidTags.FUEL.tag, TFMGTags.TFMGFluidTags.FLAMMABLE.tag)
                .source(BaseFlowingFluid.Source::new)
                .bucket()
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }

    @SafeVarargs
    private static FluidEntry<BaseFlowingFluid.Flowing> hotFluid(String name, int fogColor, TagKey<Fluid>... tags) {
        return REGISTRATE.fluid(name, getLocation(name), getLocationFlow(name),
                        HotFluidType.create(fogColor, () -> 1f / 32f))
                .lang(toHumanReadable(name))
                .properties(b -> b.viscosity(1000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(3)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .tag(tags)
                .source(BaseFlowingFluid.Source::new)
                .bucket()
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }

    @SafeVarargs
    private static FluidEntry<BaseFlowingFluid.Flowing> concreteFluid(String name, int fogColor, NonNullFunction<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> factory, TagKey<Fluid>... tags) {
        return REGISTRATE.fluid(name, getLocation(name), getLocationFlow(name),
                        SolidRenderedPlaceableFluidType.create(fogColor, () -> 1f / 32f))
                .lang(toHumanReadable(name))
                .properties(b -> b.viscosity(5000)
                        .density(2500))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(99999)
                        .slopeFindDistance(1)
                        .explosionResistance(1000f))
                .tag(tags)
                .source(factory)
                .bucket()
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }


    @SafeVarargs
    public static FluidEntry<VirtualFluid> gas(String name, int color, TagKey<Fluid>... tags) {
        return REGISTRATE.gasFluid(name, color)
                .lang(TFMGUtils.fromId(name))
                .tag(tags)
                .tag(TFMGTags.TFMGFluidTags.GAS.tag)
                .bucket()
                .lang(TFMGUtils.fromId(name) + " Tank")
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }

    @SafeVarargs
    public static FluidEntry<VirtualFluid> gasFuel(String name, int color, TagKey<Fluid>... tags) {
        return REGISTRATE.gasFluid(name, color)
                .lang(TFMGUtils.fromId(name))
                .tag(tags)
                .tag(TFMGTags.TFMGFluidTags.GAS.tag)
                .tag(TFMGTags.TFMGFluidTags.FUEL.tag)
                .tag(TFMGTags.TFMGFluidTags.FLAMMABLE.tag)
                .bucket()
                .lang(TFMGUtils.fromId(name) + " Tank")
                .tag(AllTags.commonItemTag("buckets/" + name))
                .build()
                .register();
    }

    public static ResourceLocation getGasLocation(String name) {
        return TFMG.asResource("fluid/" + name);
    }

    public static ResourceLocation getGasTexture() {
        return TFMG.asResource("fluid/gas_texture");
    }

    public static ResourceLocation getLocation(String name) {
        return TFMG.asResource("fluid/" + name + "_still");
    }

    public static ResourceLocation getLocationFlow(String name) {
        return TFMG.asResource("fluid/" + name + "_flow");
    }

    public static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;


        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        public SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                               ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(net.neoforged.neoforge.fluids.FluidStack stack) {
            return NO_TINT;
        }


        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }


    public static void init() {
    }
}
