package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.TFMGBuilderTransformers;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkBlock;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkGenerator;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.content.deposits.FluidDepositBlock;
import com.drmangotea.tfmg.content.gadgets.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.content.items.CoalCokeBlockItem;
import com.drmangotea.tfmg.content.items.FossilstoneItem;
import com.drmangotea.tfmg.content.deposits.surface_scanner.SurfaceScannerBlock;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillation_tower.DistillationControllerBlock;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillation_tower.DistillationOutputBlock;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillery.DistilleryControllerBlock;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillery.DistilleryOutputBlock;
import com.drmangotea.tfmg.content.machines.pipes.normal.steel.EncasedSteelPipeBlock;
import com.drmangotea.tfmg.content.machines.pipes.normal.steel.GlassSteelPipeBlock;
import com.drmangotea.tfmg.content.machines.pipes.normal.steel.SteelPipeAttachmentModel;
import com.drmangotea.tfmg.content.machines.pipes.normal.steel.SteelPipeBlock;
import com.drmangotea.tfmg.content.machines.pipes.pumps.TFMGPumpBlock;
import com.drmangotea.tfmg.content.machines.pipes.smart_pipes.TFMGSmartFluidPipeBlock;
import com.drmangotea.tfmg.content.machines.pipes.valves.TFMGFluidValveBlock;
import com.drmangotea.tfmg.content.machines.tanks.SteelFluidTankModel;
import com.drmangotea.tfmg.content.machines.tanks.SteelTankBlock;
import com.drmangotea.tfmg.content.machines.tanks.SteelTankGenerator;
import com.drmangotea.tfmg.content.machines.tanks.SteelTankItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;

import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.Tags;


import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;


public class TFMGBlocks {


    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }
    //-----------------------MISC---------------------------//
    public static final BlockEntry<NapalmBombBlock> NAPALM_BOMB = REGISTRATE.block("napalm_bomb", NapalmBombBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_GREEN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("napalm_bomb"))
            .item()
            .build()
            .lang("Napalm Bomb")
            .register();
    public static final BlockEntry<Block> FOSSILSTONE = REGISTRATE.block("fossilstone", Block::new)
            .initialProperties(() -> Blocks.OBSIDIAN)
            .properties(p -> p.strength(100f,1200f))
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("fossilstone"))
            .item(FossilstoneItem::new)
            .build()
            .lang("Fossilstone")
            .register();

    public static final BlockEntry<FluidDepositBlock> OIL_DEPOSIT = REGISTRATE.block("oil_deposit", FluidDepositBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.strength(69696969))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("oil_deposit"))
            .item()
            .build()
            .lang("Oil Deposit")
            .register();

    public static final BlockEntry<CasingBlock> STEEL_CASING = REGISTRATE.block("steel_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.STEEL_CASING))
            .register();
    public static final BlockEntry<CasingBlock> HEAVY_MACHINERY_CASING = REGISTRATE.block("heavy_machinery_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .properties(p -> p.sound(SoundType.COPPER))
            .register();






    //-----------------------MACHINES---------------------------//
    public static final BlockEntry<FormWorkBlock> FORMWORK_BLOCK =
            REGISTRATE.block("formwork_block", FormWorkBlock::new)
                    .initialProperties(Material.WOOD)
                    .properties(p -> p.color(MaterialColor.WOOD))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new FormWorkGenerator()::generate)
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<SurfaceScannerBlock> SURFACE_SCANNER =
            REGISTRATE.block("surface_scanner", SurfaceScannerBlock::new)
                 //  .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                 //  .properties(p -> p
                 //          .strength(4.5F))
                    //  .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(TFMGBuilderTransformers.surfaceScanner())
                    //  .transform(axeOrPickaxe())
                  //  .transform(BlockStressDefaults.setImpact(10.0))
                    .register();


    //fluid stuff
    public static final BlockEntry<SteelPipeBlock> STEEL_PIPE = REGISTRATE.block("steel_pipe", SteelPipeBlock::new)
            .initialProperties(Material.HEAVY_METAL)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_PIPE))
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
                    .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> STEEL_MECHANICAL_PUMP = REGISTRATE.block("steel_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> STEEL_FLUID_VALVE = REGISTRATE.block("steel_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();

    @SuppressWarnings("'addLayer(java.util.function.Supplier<java.util.function.Supplier<net.minecraft.client.renderer.RenderType>>)' is deprecated and marked for removal ")
    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK = REGISTRATE.block("steel_fluid_tank", SteelTankBlock::regular)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
            .transform(pickaxeOnly())
            .blockstate(new SteelTankGenerator()::generate)
            .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(SteelTankItem::new)
            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
            .build()
            .register();
    /////

    //Distillation


    public static final BlockEntry<DistilleryOutputBlock> CAST_IRON_DISTILLATION_OUTPUT =
            REGISTRATE.block("cast_iron_distillation_output", DistilleryOutputBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.color(MaterialColor.STONE))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .item(AssemblyOperatorBlockItem::new)
                    .build()
                    .register();
    public static final BlockEntry<DistilleryControllerBlock> CAST_IRON_DISTILLATION_CONTROLLER =
            REGISTRATE.block("cast_iron_distillation_controller", DistilleryControllerBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .item()
                    .build()
                    .register();
//
public static final BlockEntry<DistillationOutputBlock> STEEL_DISTILLATION_OUTPUT =
        REGISTRATE.block("steel_distillation_output", DistillationOutputBlock::new)
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.color(MaterialColor.STONE))
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(axeOrPickaxe())
                .item(AssemblyOperatorBlockItem::new)
                .build()
                .register();
    public static final BlockEntry<DistillationControllerBlock> STEEL_DISTILLATION_CONTROLLER =
            REGISTRATE.block("steel_distillation_controller", DistillationControllerBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .item()
                    .build()
                    .register();


    //////






    //-----------------------BUILDING BLOCKS---------------------------//
    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/steel"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Steel")
            .register();
    public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE.block("cast_iron_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("cast_iron_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/cast_iron"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Cast Iron")
            .register();

    public static final BlockEntry<Block> ALUMINUM_BLOCK = REGISTRATE.block("aluminum_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("aluminum_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/aluminum"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Aluminum")
            .register();

    public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lead_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/lead"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lead")
            .register();


    public static final BlockEntry<Block> COAL_COKE_BLOCK = REGISTRATE.block("coal_coke_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("coal_coke_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .item(CoalCokeBlockItem::new)
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Coal Coke")
            .register();

    //sheetmetals
    public static final BlockEntry<Block> STEEL_SHEETMETAL = REGISTRATE.block("steel_sheetmetal", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_SHEETMETAL)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_SHEETMETAL)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_sheetmetal"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .build()
            .lang("Steel Sheetmetal")
            .register();

    //
    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_CASING_DOOR =
            REGISTRATE.block("heavy_casing_door", p -> new TFMGSlidingDoorBlock(p, false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_casing"))
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> STEEL_CASING_DOOR =
            REGISTRATE.block("steel_door", p -> new TFMGSlidingDoorBlock(p, true))
                    .transform(TFMGBuilderTransformers.slidingDoor("steel"))
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();




    //-----------------------CONCRETE---------------------------//
    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }
    public static final BlockEntry<Block> CONCRETE = REGISTRATE.block("concrete", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem("concrete"))
            .build()
            .lang("Concrete")
            .register();

    public static void register() {}
}
