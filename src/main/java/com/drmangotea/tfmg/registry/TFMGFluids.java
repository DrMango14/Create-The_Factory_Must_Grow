package com.drmangotea.tfmg.registry;




import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.content.concrete.ConcreteFluid;
import com.drmangotea.tfmg.content.concrete.ConcreteFluidType;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllTags.AllFluidTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid.PotionFluidType;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;

public class TFMGFluids {

    public static final ResourceLocation CONCRETE_RL = CreateTFMG.asResource("fluid/liquid_concrete");

    public static final ResourceLocation LPG_RL = CreateTFMG.asResource("fluid/lpg");

    public static final ResourceLocation BUTANE_RL = CreateTFMG.asResource("fluid/butane");

    public static final ResourceLocation PROPANE_RL = CreateTFMG.asResource("fluid/propane");

    public static final ResourceLocation CARBON_DIOXIDE_RL = CreateTFMG.asResource("fluid/carbon_dioxide");

    public static final ResourceLocation NAPHTHA_STILL_RL = CreateTFMG.asResource("fluid/naphtha_still");
    public static final ResourceLocation NAPHTHA_FLOW_RL = CreateTFMG.asResource("fluid/naphtha_flow");

    public static final ResourceLocation DIESEL_STILL_RL = CreateTFMG.asResource("fluid/diesel_still");
    public static final ResourceLocation DIESEL_FLOW_RL = CreateTFMG.asResource("fluid/diesel_flow");

    public static final ResourceLocation GASOLINE_STILL_RL = CreateTFMG.asResource("fluid/gasoline_still");
    public static final ResourceLocation GASOLINE_FLOW_RL = CreateTFMG.asResource("fluid/gasoline_flow");

    public static final ResourceLocation KEROSENE_STILL_RL = CreateTFMG.asResource("fluid/kerosene_still");
    public static final ResourceLocation KEROSENE_FLOW_RL = CreateTFMG.asResource("fluid/kerosene_flow");

    public static final ResourceLocation HEAVY_OIL_STILL_RL = CreateTFMG.asResource("fluid/heavy_oil_still");
    public static final ResourceLocation HEAVY_OIL_FLOW_RL = CreateTFMG.asResource("fluid/heavy_oil_flow");

    public static final ResourceLocation LUBRICATION_OIL_STILL_RL = CreateTFMG.asResource("fluid/lubrication_oil_still");
    public static final ResourceLocation LUBRICATION_OIL_FLOW_RL = CreateTFMG.asResource("fluid/lubrication_oil_flow");

    public static final ResourceLocation NAPALM_STILL_RL = CreateTFMG.asResource("fluid/napalm_still");
    public static final ResourceLocation NAPALM_FLOW_RL = CreateTFMG.asResource("fluid/napalm_flow");

    public static final ResourceLocation PLASTIC_STILL_RL = CreateTFMG.asResource("fluid/liquid_plastic_still");
    public static final ResourceLocation PLASTIC_FLOW_RL = CreateTFMG.asResource("fluid/liquid_plastic_flow");

    public static final ResourceLocation CRUDE_OIL_STILL_RL = CreateTFMG.asResource("fluid/crude_oil_still");
    public static final ResourceLocation CRUDE_OIL_FLOW_RL = CreateTFMG.asResource("fluid/crude_oil_flow");


    public static final FluidEntry<VirtualFluid> CARBON_DIOXIDE
            = REGISTRATE.virtualFluid("carbon_dioxide",CARBON_DIOXIDE_RL,CARBON_DIOXIDE_RL)
            .lang("Carbon Dioxide")
            .register();


    public static final FluidEntry<VirtualFluid> LPG =
            REGISTRATE.virtualFluid("lpg",LPG_RL,LPG_RL)
                    .lang("LPG")
                    .register();

    public static final FluidEntry<VirtualFluid> PROPANE =
            REGISTRATE.virtualFluid("propane",PROPANE_RL,PROPANE_RL)
                    .lang("Propane")
                    .register();

    public static final FluidEntry<VirtualFluid> BUTANE =
            REGISTRATE.virtualFluid("butane",BUTANE_RL,BUTANE_RL)
                    .lang("Butane")
                    .register();

    public static final FluidEntry<VirtualFluid> ETHYLENE =
            REGISTRATE.virtualFluid("ethylene",BUTANE_RL,BUTANE_RL)
                    .lang("Ethylene")
                    .register();

    public static final FluidEntry<VirtualFluid> PROPYLENE =
            REGISTRATE.virtualFluid("propylene",PROPANE_RL,PROPANE_RL)
                    .lang("Propylene")
                    .register();



    public static final FluidEntry<ForgeFlowingFluid.Flowing> CRUDE_OIL_FLUID =
            REGISTRATE.fluid("crude_oil_fluid",CRUDE_OIL_STILL_RL,CRUDE_OIL_FLOW_RL)
                    .lang("Crude Oil")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/gasoline"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_PLASTIC =
            REGISTRATE.fluid("liquid_plastic",PLASTIC_STILL_RL,PLASTIC_FLOW_RL)
                    .lang("Liquid Plastic")
                    .properties(b -> b.viscosity(1500)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(2)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/plastic"))
                    .build()
                    .register();





    public static final FluidEntry<ForgeFlowingFluid.Flowing> GASOLINE =
            REGISTRATE.fluid("gasoline",GASOLINE_STILL_RL,GASOLINE_FLOW_RL)
                    .lang("Gasoline")
                    .properties(b -> b.viscosity(500)
                            .density(500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(7)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/gasoline"))
                    .build()
                    .register();



    public static final FluidEntry<ForgeFlowingFluid.Flowing> DIESEL =
            REGISTRATE.fluid("diesel",DIESEL_STILL_RL,DIESEL_FLOW_RL)
                    .lang("Diesel")
                    .properties(b -> b.viscosity(500)
                            .density(500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(7)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/diesel"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> KEROSENE =
            REGISTRATE.fluid("kerosene",KEROSENE_STILL_RL,KEROSENE_FLOW_RL)
                    .lang("Kerosene")
                    .properties(b -> b.viscosity(500)
                            .density(500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(7)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/kerosene"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> NAPHTHA =
            REGISTRATE.fluid("naphtha",NAPHTHA_STILL_RL,NAPHTHA_FLOW_RL)
                    .lang("Naphtha")
                    .properties(b -> b.viscosity(500)
                            .density(500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(7)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/naphtha"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> HEAVY_OIL =
            REGISTRATE.fluid("heavy_oil",HEAVY_OIL_STILL_RL,HEAVY_OIL_FLOW_RL)
                    .lang("Heavy Oil")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LUBRICATION_OIL =
            REGISTRATE.fluid("lubrication_oil",LUBRICATION_OIL_STILL_RL,LUBRICATION_OIL_FLOW_RL)
                    .lang("Lubrication Oil")
                    .properties(b -> b.viscosity(500)
                            .density(500))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(7)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .build()
                    .register();
    public static final FluidEntry<ForgeFlowingFluid.Flowing> NAPALM =
            REGISTRATE.fluid("napalm",NAPALM_STILL_RL,NAPALM_FLOW_RL)
                    .lang("Napalm")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();
    public static final FluidEntry<ForgeFlowingFluid.Flowing> COOLING_FLUID =
            REGISTRATE.fluid("cooling_fluid",NAPALM_STILL_RL,NAPALM_FLOW_RL)
                    .lang("Cooling Fluid")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                 //   .tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CREOSOTE =
            REGISTRATE.fluid("creosote",NAPALM_STILL_RL,NAPALM_FLOW_RL)
                    .lang("Creosote")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(5)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    //.tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();



    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_CONCRETE =
            REGISTRATE.fluid("liquid_concrete",CONCRETE_RL,CONCRETE_RL,
                            ConcreteFluidType.create(0x333333,
                                    () -> 1f / 24f ))
                    .lang("Liquid Concrete")
                    .properties(b -> b.viscosity(9999)
                            .density(9999))
                    .fluidProperties(p -> p.levelDecreasePerBlock(0)
                            .tickRate(99999)
                            .slopeFindDistance(0)
                            .explosionResistance(4f)
                            )
                    .source(ConcreteFluid.Source::new)
                    .bucket()
                    //.tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();


    ////




    // Load this class

    public static void register() {
    }

/*
    private static class NoColorFluidAttributes extends FluidType {

        protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }

 */
/*
    @Nullable
    public static BlockState getLavaInteraction(FluidState fluidState) {
        Fluid fluid = fluidState.getType();

        if (fluid.isSame(TFMGFluids.CRUDE_OIL_FLUID.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(TFMGFluids.GASOLINE.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(DIESEL.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(LUBRICATION_OIL.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(HEAVY_OIL.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(KEROSENE.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        if (fluid.isSame(NAPHTHA.get()))
            return TFMGBlocks.FOSSILSTONE
                    .get()
                    .defaultBlockState();
        return null;
    }



 */

}
