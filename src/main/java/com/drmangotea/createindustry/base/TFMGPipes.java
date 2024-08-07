package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.blocks.pipes.normal.aluminum.AluminumPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.aluminum.EncasedAluminumPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.aluminum.GlassAluminumPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.brass.BrassPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.brass.EncasedBrassPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.brass.GlassBrassPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.cast_iron.CastIronPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.cast_iron.EncasedCastIronPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.cast_iron.GlassCastIronPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.plastic.EncasedPlasticPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.plastic.GlassPlasticPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.plastic.PlasticPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.steel.EncasedSteelPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.GeneralPipeAttachmentModel;
import com.drmangotea.createindustry.blocks.pipes.normal.steel.GlassSteelPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.normal.steel.SteelPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.pumps.TFMGPumpBlock;
import com.drmangotea.createindustry.blocks.pipes.smart_pipes.TFMGSmartFluidPipeBlock;
import com.drmangotea.createindustry.blocks.pipes.valves.TFMGFluidValveBlock;
import com.drmangotea.createindustry.registry.TFMGCreativeModeTabs;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.drmangotea.createindustry.registry.TFMGPartialModels.*;

public class TFMGPipes {


    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }

    //STEEL
    public static final BlockEntry<SteelPipeBlock> STEEL_PIPE = REGISTRATE.block("steel_pipe", SteelPipeBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedSteelPipeBlock> COPPER_ENCASED_STEEL_PIPE =
            REGISTRATE.block("copper_encased_steel_pipe", p -> new EncasedSteelPipeBlock(p, AllBlocks.COPPER_CASING::get))
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.encasedPipe())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                            (s, f) -> !s.getValue(EncasedSteelPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGPipes.STEEL_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassSteelPipeBlock> GLASS_STEEL_PIPE =
            REGISTRATE.block("glass_steel_pipe", GlassSteelPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/steel_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> STEEL_MECHANICAL_PUMP = REGISTRATE.block("steel_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGSmartFluidPipeBlock> STEEL_SMART_FLUID_PIPE =
            REGISTRATE.block("steel_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .transform(pickaxeOnly())
                    .blockstate(new SmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> STEEL_FLUID_VALVE = REGISTRATE.block("steel_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, STEEL_PIPE_ATTACHMENTS, STEEL_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();
    //CAST_IRON
    public static final BlockEntry<CastIronPipeBlock> CAST_IRON_PIPE = REGISTRATE.block("cast_iron_pipe", CastIronPipeBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedCastIronPipeBlock> COPPER_ENCASED_CAST_IRON_PIPE =
            REGISTRATE.block("copper_encased_cast_iron_pipe", p -> new EncasedCastIronPipeBlock(p, AllBlocks.COPPER_CASING::get))
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.encasedPipe())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                            (s, f) -> !s.getValue(EncasedCastIronPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, CAST_IRON_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGPipes.CAST_IRON_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassCastIronPipeBlock> GLASS_CAST_IRON_PIPE =
            REGISTRATE.block("glass_cast_iron_pipe", GlassCastIronPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/cast_iron_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, CAST_IRON_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> CAST_IRON_MECHANICAL_PUMP = REGISTRATE.block("cast_iron_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGSmartFluidPipeBlock> CAST_IRON_SMART_FLUID_PIPE =
            REGISTRATE.block("cast_iron_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .transform(pickaxeOnly())
                    .blockstate(new SmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> CAST_IRON_FLUID_VALVE = REGISTRATE.block("cast_iron_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, CAST_IRON_PIPE_ATTACHMENTS, CAST_IRON_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();
    //BRASS
    public static final BlockEntry<BrassPipeBlock> BRASS_PIPE = REGISTRATE.block("brass_pipe", BrassPipeBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedBrassPipeBlock> COPPER_ENCASED_BRASS_PIPE =
            REGISTRATE.block("copper_encased_brass_pipe", p -> new EncasedBrassPipeBlock(p, AllBlocks.COPPER_CASING::get))
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.encasedPipe())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                            (s, f) -> !s.getValue(EncasedBrassPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, BRASS_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGPipes.BRASS_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassBrassPipeBlock> GLASS_BRASS_PIPE =
            REGISTRATE.block("glass_brass_pipe", GlassBrassPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/brass_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> BRASS_MECHANICAL_PUMP = REGISTRATE.block("brass_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGSmartFluidPipeBlock> BRASS_SMART_FLUID_PIPE =
            REGISTRATE.block("brass_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .transform(pickaxeOnly())
                    .blockstate(new SmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> BRASS_FLUID_VALVE = REGISTRATE.block("brass_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, BRASS_PIPE_ATTACHMENTS, BRASS_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();
    //PLASTIC
    public static final BlockEntry<PlasticPipeBlock> PLASTIC_PIPE = REGISTRATE.block("plastic_pipe", PlasticPipeBlock::new)
            .initialProperties(() -> Blocks.QUARTZ_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedPlasticPipeBlock> COPPER_ENCASED_PLASTIC_PIPE =
            REGISTRATE.block("copper_encased_plastic_pipe", p -> new EncasedPlasticPipeBlock(p, AllBlocks.COPPER_CASING::get))
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.encasedPipe())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                            (s, f) -> !s.getValue(EncasedPlasticPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, PLASTIC_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGPipes.PLASTIC_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassPlasticPipeBlock> GLASS_PLASTIC_PIPE =
            REGISTRATE.block("glass_plastic_pipe", GlassPlasticPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/plastic_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, PLASTIC_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> PLASTIC_MECHANICAL_PUMP = REGISTRATE.block("plastic_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGSmartFluidPipeBlock> PLASTIC_SMART_FLUID_PIPE =
            REGISTRATE.block("plastic_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .transform(pickaxeOnly())
                    .blockstate(new SmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> PLASTIC_FLUID_VALVE = REGISTRATE.block("plastic_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, PLASTIC_PIPE_ATTACHMENTS, PLASTIC_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();
    //ALUMINUM
    public static final BlockEntry<AluminumPipeBlock> ALUMINUM_PIPE = REGISTRATE.block("aluminum_pipe", AluminumPipeBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EncasedAluminumPipeBlock> COPPER_ENCASED_ALUMINUM_PIPE =
            REGISTRATE.block("copper_encased_aluminum_pipe", p -> new EncasedAluminumPipeBlock(p, AllBlocks.COPPER_CASING::get))
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.encasedPipe())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.COPPER_CASING)))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.COPPER_CASING,
                            (s, f) -> !s.getValue(EncasedAluminumPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, ALUMINUM_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGPipes.ALUMINUM_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassAluminumPipeBlock> GLASS_ALUMINUM_PIPE =
            REGISTRATE.block("glass_aluminum_pipe", GlassAluminumPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.getVariantBuilder(c.getEntry())
                            .forAllStatesExcept(state -> {
                                Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                return ConfiguredModel.builder()
                                        .modelFile(p.models()
                                                .getExistingFile(p.modLoc("block/aluminum_pipe/window")))
                                        .uvLock(false)
                                        .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                        .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                        .build();
                            }, BlockStateProperties.WATERLOGGED))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
                    .loot((p, b) -> p.dropOther(b, ALUMINUM_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> ALUMINUM_MECHANICAL_PUMP = REGISTRATE.block("aluminum_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<TFMGSmartFluidPipeBlock> ALUMINUM_SMART_FLUID_PIPE =
            REGISTRATE.block("aluminum_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .transform(pickaxeOnly())
                    .blockstate(new SmartFluidPipeGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> ALUMINUM_FLUID_VALVE = REGISTRATE.block("aluminum_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> model -> new GeneralPipeAttachmentModel(model, ALUMINUM_PIPE_ATTACHMENTS, ALUMINUM_FLUID_PIPE_CASING)))
            .item()
            .transform(customItemModel())
            .register();


    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }


    public static void register(){}
}
