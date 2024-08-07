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
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

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

            CARBON_DIOXIDE = gas("carbon_dioxide"),
            ETHYLENE = gas("ethylene"),
            PROPYLENE = gas("propylene"),
            PROPANE = gas("propane",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            BUTANE = gas("butane",TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            LPG = gas("lpg",TFMGTags.TFMGFluidTags.LPG.tag,TFMGTags.TFMGFluidTags.FLAMMABLE.tag),
            NEON = gas("neon")
        ;
    public static final FluidEntry<SimpleFlowableFluid>
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



    public static final FluidEntry<ConcreteFluid> COOLING_FLUID =
            REGISTRATE.fluid("cooling_fluid",COOLING_FLUID_STILL_RL,COOLING_FLUID_FLOW_RL, ConcreteFluid::new
//                            ConcreteFluidType.create(0x333333,
//                                    () -> 1f / 24f )
                    )
                    .lang("Cooling Fluid")
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .flowSpeed(5)
                            .blastResistance(100f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 1000, 1000))
                    .source(SimpleFlowableFluid.Source::new)
                    .bucket()
                 //   .tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();





    public static final FluidEntry<SimpleFlowableFluid> SULFURIC_ACID =
            REGISTRATE.fluid("sulfuric_acid",SULFURIC_AXID_STILL_RL,SULFURIC_AXID_FLOW_RL,
                            AcidFluidType.create(0x333333,
                                    () -> 1f / 24f ))
                    .lang("Sulfuric Acid")
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .flowSpeed(5)
                            .blastResistance(100f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 2000, 1400))
                    .source(SimpleFlowableFluid.Source::new)
                    .bucket()
                    //   .tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();







    public static final FluidEntry<ConcreteFluid> LIQUID_CONCRETE =
            REGISTRATE.fluid("liquid_concrete",CONCRETE_RL,CONCRETE_RL, ConcreteFluid::new
//                            ConcreteFluidType.create(0x333333,
//                                    () -> 1f / 24f )
                    )
                    .lang("Liquid Concrete")
                    .fluidProperties(p -> p.levelDecreasePerBlock(0)
                            .tickRate(99999)
                            .flowSpeed(0)
                            .blastResistance(4f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 9999, 9999))
                    .source(ConcreteFluid.Source::new)
                    .bucket()
                    //.tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();

    public static final FluidEntry<ConcreteFluid> LIQUID_ASPHALT =
            REGISTRATE.fluid("liquid_asphalt", ASPHALT_RL, ASPHALT_RL, ConcreteFluid::new
//                            ConcreteFluidType.create(0x333333,
//                                    () -> 1f / 24f )
                    )
                    .lang("Liquid Asphalt")
                    .fluidProperties(p -> p.levelDecreasePerBlock(0)
                            .tickRate(99999)
                            .flowSpeed(0)
                            .blastResistance(4f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 9999, 9999))
                    .source(AsphaltFluid.Source::new)
                    .bucket()
                    //.tag(AllTags.forgeItemTag("buckets/napalm"))
                    .build()
                    .register();



    public static final FluidEntry<SimpleFlowableFluid> LIQUID_PLASTIC =
            REGISTRATE.fluid("liquid_plastic",PLASTIC_STILL_RL,PLASTIC_FLOW_RL
                            , PlasticFluidType.create(0xc4c4c4,
                                    () -> 1f / 24f ))
                    .lang("Liquid Plastic")
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .flowSpeed(2)
                            .blastResistance(100f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 1500, 1000))
                    .source(SimpleFlowableFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/plastic"))
                    .build()
                    .register();

    public static final FluidEntry<SimpleFlowableFluid> MOLTEN_STEEL =
            REGISTRATE.fluid("molten_steel",MOLTEN_STEEL_STILL_RL,MOLTEN_STEEL_FLOW_RL,
                            HotFluidType.create(0xfbfbbb, () -> 1f / 24f ))
                    .lang("Molten Steel")
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .flowSpeed(2)
                            .blastResistance(100f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 1500, 1000))
                    .tag(TFMGTags.TFMGFluidTags.MOLTEN_STEEL.tag)
                    .source(SimpleFlowableFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/steel"))
                    .build()
                    .register();

    public static final FluidEntry<SimpleFlowableFluid> MOLTEN_SLAG =
            REGISTRATE.fluid("molten_slag",MOLTEN_SLAG_STILL_RL,MOLTEN_SLAG_FLOW_RL ,
                            HotFluidType.create(0xfbfbbb, () -> 1f / 24f ))
                    .lang("Molten Slag")
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(10)
                            .flowSpeed(2)
                            .blastResistance(100f))
                    .fluidAttributes(() -> new AttributeHandler("block.create.honey", 1500, 1000))
                    .source(SimpleFlowableFluid.Source::new)
                    .bucket()
                    .tag(AllTags.forgeItemTag("buckets/slag"))
                    .build()
                    .register();


    ////

    @SafeVarargs
    public static FluidEntry<SimpleFlowableFluid> fuel(String name, TagKey<Fluid>... tags) {


            TagKey<Fluid>[] fuelTag = new TagKey[]{TFMGTags.TFMGFluidTags.FUEL.tag};

            TagKey<Fluid>[] tagsWithFuelTag = new TagKey[tags.length+1];

            System.arraycopy(tags, 0, tagsWithFuelTag, 0, tags.length);
            System.arraycopy(fuelTag, 0, tagsWithFuelTag, tags.length, 0);

        return flammableFluid(name,tags);

    }

    @SafeVarargs
    public static FluidEntry<SimpleFlowableFluid> flammableFluid(String name, TagKey<Fluid>... tags){
        return  REGISTRATE.fluid(name,CreateTFMG.asResource("fluid/"+name+"_still"),CreateTFMG.asResource("fluid/"+name+"_flow"),
                FlammableFluidType.create(0x606060,
                        () -> 1f / 24f ))
                .lang(TFMGUtils.fromId(name))
                .fluidProperties(p -> p.levelDecreasePerBlock(1)
                        .tickRate(10)
                        .flowSpeed(2)
                        .blastResistance(100f))
                .fluidAttributes(() -> new AttributeHandler("block.create.honey", 1000, 1000))
                .tag(tags)
                .source(FlammableFluid.Source::new)
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }

    @SafeVarargs
    public static FluidEntry<VirtualFluid> gas(String name, TagKey<Fluid>... tags){
        //ignore error, AW handles it
        TagKey<Fluid> tag = TagKey.create(Registry.FLUID_REGISTRY, CreateTFMG.asResource(name));

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

//        FluidInteractionRegistry.addInteraction( TFMGFluids.COOLING_FLUID.get().getFluidType(), new FluidInteractionRegistry.InteractionInformation(
//                ForgeMod.LAVA_TYPE.get(),
//                fluidState -> AllPaletteStoneTypes.LIMESTONE.baseBlock
//                        .get()
//                        .defaultBlockState()));





    }

    //this isnt public in create for some reason
    record AttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private AttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        public AttributeHandler(String key) {
            this(key, FluidConstants.WATER_VISCOSITY, 1000);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name.copy();
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }
    }
}
