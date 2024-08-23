package com.drmangotea.createindustry.registry;




import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.util.TFMGUtils;
import com.drmangotea.createindustry.blocks.concrete.ConcreteFluid;
import com.drmangotea.createindustry.blocks.concrete.ConcreteFluidType;
import com.drmangotea.createindustry.blocks.concrete.asphalt.AsphaltFluid;
import com.drmangotea.createindustry.blocks.fluids.*;
import com.simibubi.create.AllTags;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;

public class TFMGFluids {

    public static final ResourceLocation CONCRETE_RL = CreateTFMG.asResource("fluid/liquid_concrete");


    public static final ResourceLocation PLASTIC_STILL_RL = CreateTFMG.asResource("fluid/liquid_plastic_still");
    public static final ResourceLocation PLASTIC_FLOW_RL = CreateTFMG.asResource("fluid/liquid_plastic_flow");


    public static final ResourceLocation ASPHALT_RL = CreateTFMG.asResource("fluid/liquid_asphalt");

    public static final ResourceLocation COOLING_FLUID_STILL_RL = CreateTFMG.asResource("fluid/cooling_fluid_still");
    public static final ResourceLocation COOLING_FLUID_FLOW_RL = CreateTFMG.asResource("fluid/cooling_fluid_flow");

    public static final ResourceLocation SULFURIC_AXID_STILL_RL = CreateTFMG.asResource("fluid/sulfuric_acid_still");
    public static final ResourceLocation SULFURIC_AXID_FLOW_RL = CreateTFMG.asResource("fluid/sulfuric_acid_flow");


    public static final ResourceLocation MOLTEN_STEEL_STILL_RL = CreateTFMG.asResource("fluid/molten_steel_still");
    public static final ResourceLocation MOLTEN_STEEL_FLOW_RL = CreateTFMG.asResource("fluid/molten_steel_flow");

    public static final ResourceLocation MOLTEN_SLAG_STILL_RL = CreateTFMG.asResource("fluid/molten_slag_still");
    public static final ResourceLocation MOLTEN_SLAG_FLOW_RL = CreateTFMG.asResource("fluid/molten_slag_flow");



    public static final FluidEntry<VirtualFluid>

            AIR = gas("air"),
            HEATED_AIR = gas("heated_air"),

            CARBON_DIOXIDE = gas("carbon_dioxide"),
            ETHYLENE = gas("ethylene"),
            PROPYLENE = gas("propylene"),
            PROPANE = gas("propane",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            BUTANE = gas("butane",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            LPG = gas("lpg",TFMGTags.TFMGFluidTags.LPG.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            NEON = gas("neon"),
            BLAST_FURNACE_GAS = gas("blast_furnace_gas",TFMGTags.TFMGFluidTags.FLAMMABLE.tag)
        ;
    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            CRUDE_OIL = flammableFluid("crude_oil",TFMGTags.TFMGFluidTags.CRUDE_OIL.tag),
            HEAVY_OIL = flammableFluid("heavy_oil",TFMGTags.TFMGFluidTags.HEAVY_OIL.tag),
            LUBRICATION_OIL = flammableFluid("lubrication_oil",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            NAPALM = flammableFluid("napalm",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            NAPHTHA = fuel("naphtha",TFMGTags.TFMGFluidTags.NAPHTHA.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            KEROSENE = fuel("kerosene",TFMGTags.TFMGFluidTags.KEROSENE.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            GASOLINE = fuel("gasoline",TFMGTags.TFMGFluidTags.GASOLINE.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            DIESEL = fuel("diesel",TFMGTags.TFMGFluidTags.DIESEL.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            CREOSOTE = fuel("creosote",TFMGTags.TFMGFluidTags.FLAMMABLE.tag)

            ;



    public static final FluidEntry<ForgeFlowingFluid.Flowing> COOLING_FLUID =
            REGISTRATE.fluid("cooling_fluid",COOLING_FLUID_STILL_RL,COOLING_FLUID_FLOW_RL,
                            ConcreteFluidType.create(0x333333,
                                    () -> 1f / 24f ))
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




    public static final FluidEntry<ForgeFlowingFluid.Flowing> SULFURIC_ACID =
            REGISTRATE.fluid("sulfuric_acid",SULFURIC_AXID_STILL_RL,SULFURIC_AXID_FLOW_RL,
                            AcidFluidType.create(0x333333,
                                    () -> 1f / 24f ))
                    .lang("Sulfuric Acid")
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

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_ASPHALT =
            REGISTRATE.fluid("liquid_asphalt", ASPHALT_RL, ASPHALT_RL,
                            ConcreteFluidType.create(0x333333,
                                    () -> 1f / 24f ))
                    .lang("Liquid Asphalt")
                    .properties(b -> b.viscosity(9999)
                            .density(9999))
                    .fluidProperties(p -> p.levelDecreasePerBlock(0)
                            .tickRate(99999)
                            .slopeFindDistance(0)
                            .explosionResistance(4f)
                    )
                    .source(AsphaltFluid.Source::new)
                    .bucket()
                    //.tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();



    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_PLASTIC =
            REGISTRATE.fluid("liquid_plastic",PLASTIC_STILL_RL,PLASTIC_FLOW_RL
                            , PlasticFluidType.create(0xc4c4c4,
                                    () -> 1f / 24f ))
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

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_STEEL =
            REGISTRATE.fluid("molten_steel",MOLTEN_STEEL_STILL_RL,MOLTEN_STEEL_FLOW_RL,
                            HotFluidType.create(0xfbfbbb, () -> 1f / 24f ))
                    .lang("Molten Steel")
                    .properties(b -> b.viscosity(1500)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(2)
                            .explosionResistance(100f))
                    .tag(TFMGTags.TFMGFluidTags.MOLTEN_STEEL.tag)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/steel"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_SLAG =
            REGISTRATE.fluid("molten_slag",MOLTEN_SLAG_STILL_RL,MOLTEN_SLAG_FLOW_RL ,
                            HotFluidType.create(0xfbfbbb, () -> 1f / 24f ))
                    .lang("Molten Slag")
                    .properties(b -> b.viscosity(1500)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .slopeFindDistance(2)
                            .explosionResistance(100f))

                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/slag"))
                    .build()
                    .register();


    ////

    @SafeVarargs
    public static FluidEntry<ForgeFlowingFluid.Flowing> fuel(String name, TagKey<Fluid>... tags) {


            TagKey<Fluid>[] fuelTag = new TagKey[]{TFMGTags.TFMGFluidTags.FUEL.tag};

            TagKey<Fluid>[] tagsWithFuelTag = new TagKey[tags.length+1];

            System.arraycopy(tags, 0, tagsWithFuelTag, 0, tags.length);
            System.arraycopy(fuelTag, 0, tagsWithFuelTag, tags.length, 0);

        return flammableFluid(name,tags);

    }

    @SafeVarargs
    public static FluidEntry<ForgeFlowingFluid.Flowing> flammableFluid(String name, TagKey<Fluid>... tags){


        return  REGISTRATE.fluid(name,CreateTFMG.asResource("fluid/"+name+"_still"),CreateTFMG.asResource("fluid/"+name+"_flow"),
                FlammableFluidType.create(0x606060,
                        () -> 1f / 24f ))
                .lang(TFMGUtils.fromId(name))
                .properties(b -> b.viscosity(1000)
                        .density(1000))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(10)
                        .slopeFindDistance(5)
                        .explosionResistance(100f))
                .tag(tags)
                .source(FlammableFluid.Source::new)
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }

    @SafeVarargs
    public static FluidEntry<VirtualFluid> gas(String name, TagKey<Fluid>... tags){
        TagKey<Fluid> tag = FluidTags.create(CreateTFMG.asResource(name));

        TagKey<Fluid>[] fluidTags = tags;

        if(tags.length==0){

            fluidTags = new TagKey[]{tag};

        }

        return  REGISTRATE.virtualFluid(name,CreateTFMG.asResource("fluid/"+name),CreateTFMG.asResource("fluid/"+name))
                .lang(TFMGUtils.fromId(name))
                .tag(tags)
                .tag(TFMGTags.TFMGFluidTags.GAS.tag)
              //  .source(GasFluid.Source::new)

                .bucket()
                .lang(TFMGUtils.fromId(name)+" Tank")
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }




    // Load this class

    public static void register() {
    }


    public static void registerFluidInteractions() {
       // FluidInteractionRegistry.addInteraction(TFMGFluids.CRUDE_OIL.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
//
       // FluidInteractionRegistry.addInteraction(TFMGFluids.DIESEL.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
       // FluidInteractionRegistry.addInteraction(TFMGFluids.GASOLINE.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
       // FluidInteractionRegistry.addInteraction(TFMGFluids.LUBRICATION_OIL.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
       // FluidInteractionRegistry.addInteraction(TFMGFluids.HEAVY_OIL.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
       // FluidInteractionRegistry.addInteraction(TFMGFluids.KEROSENE.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
       //         ForgeMod.LAVA_TYPE.get(),
       //         fluidState -> TFMGBlocks.FOSSILSTONE.getDefaultState()));
      //  FluidInteractionRegistry.addInteraction(ForgeMod.WATER_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
      //          CRUDE_OIL.get().getFluidType(),
      //          fluidState -> TFMGFluids.POLLUTED_WATER.getBlock().get().defaultBlockState()));

        FluidInteractionRegistry.addInteraction( TFMGFluids.COOLING_FLUID.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
                ForgeMod.LAVA_TYPE.get(),
                fluidState -> AllPaletteStoneTypes.LIMESTONE.baseBlock
                        .get()
                        .defaultBlockState()));





    }







}
