package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.TFMGBuilderTransformers;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.base.TFMGVanillaBlockStates;
import com.drmangotea.tfmg.blocks.concrete.formwork.FormWorkBlock;
import com.drmangotea.tfmg.blocks.concrete.formwork.FormWorkGenerator;
import com.drmangotea.tfmg.blocks.decoration.TrussBlock;
import com.drmangotea.tfmg.blocks.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.blocks.decoration.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.blocks.deposits.FluidDepositBlock;
import com.drmangotea.tfmg.blocks.engines.diesel.DieselEngineBlock;
import com.drmangotea.tfmg.blocks.engines.intake.AirIntakeBlock;
import com.drmangotea.tfmg.blocks.engines.intake.AirIntakeGenerator;
import com.drmangotea.tfmg.blocks.engines.small.EngineGenerator;
import com.drmangotea.tfmg.blocks.engines.small.gasoline.GasolineEngineBackBlock;
import com.drmangotea.tfmg.blocks.engines.small.gasoline.GasolineEngineBlock;
import com.drmangotea.tfmg.blocks.engines.small.gasoline.GasolineEngineGenerator;
import com.drmangotea.tfmg.blocks.engines.small.lpg.LPGEngineBackBlock;
import com.drmangotea.tfmg.blocks.engines.small.lpg.LPGEngineBlock;
import com.drmangotea.tfmg.blocks.engines.small.turbine.TurbineEngineBackBlock;
import com.drmangotea.tfmg.blocks.engines.small.turbine.TurbineEngineBlock;
import com.drmangotea.tfmg.blocks.machines.exhaust.ExhaustBlock;
import com.drmangotea.tfmg.blocks.machines.flarestack.FlarestackBlock;
import com.drmangotea.tfmg.blocks.machines.flarestack.FlarestackGenerator;
import com.drmangotea.tfmg.blocks.pipes.normal.aluminum.AluminumPipeAttachmentModel;
import com.drmangotea.tfmg.blocks.pipes.normal.aluminum.AluminumPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.aluminum.EncasedAluminumPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.aluminum.GlassAluminumPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.brass.BrassPipeAttachmentModel;
import com.drmangotea.tfmg.blocks.pipes.normal.brass.BrassPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.brass.EncasedBrassPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.brass.GlassBrassPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.cast_iron.CastIronPipeAttachmentModel;
import com.drmangotea.tfmg.blocks.pipes.normal.cast_iron.CastIronPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.cast_iron.EncasedCastIronPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.cast_iron.GlassCastIronPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.plastic.EncasedPlasticPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.plastic.GlassPlasticPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.plastic.PlasticPipeAttachmentModel;
import com.drmangotea.tfmg.blocks.pipes.normal.plastic.PlasticPipeBlock;
import com.drmangotea.tfmg.items.gadgets.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_basin.CastingBasinBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_spout.CastingSpoutBlock;
import com.drmangotea.tfmg.items.CoalCokeBlockItem;
import com.drmangotea.tfmg.items.FossilstoneItem;
import com.drmangotea.tfmg.blocks.deposits.surface_scanner.SurfaceScannerBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.MoltenMetalBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.coke_oven.CokeOvenBlock;
import com.drmangotea.tfmg.blocks.machines.metal_processing.coke_oven.CokeOvenGenerator;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower.DistillationControllerBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower.DistillationOutputBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryControllerBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryOutputBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderBlock;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.steel.EncasedSteelPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.steel.GlassSteelPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.normal.steel.SteelPipeAttachmentModel;
import com.drmangotea.tfmg.blocks.pipes.normal.steel.SteelPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.pumps.TFMGPumpBlock;
import com.drmangotea.tfmg.blocks.pipes.smart_pipes.TFMGSmartFluidPipeBlock;
import com.drmangotea.tfmg.blocks.pipes.valves.TFMGFluidValveBlock;
import com.drmangotea.tfmg.blocks.tanks.SteelFluidTankModel;
import com.drmangotea.tfmg.blocks.tanks.SteelTankBlock;
import com.drmangotea.tfmg.blocks.tanks.SteelTankGenerator;
import com.drmangotea.tfmg.blocks.tanks.SteelTankItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;

import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
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

@SuppressWarnings("removal")
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

    public static final BlockEntry<TrussBlock> STEEL_TRUSS = REGISTRATE.block("steel_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .lang("Steel Truss")
            .register();
    public static final BlockEntry<TrussBlock> ALUMINUM_TRUSS = REGISTRATE.block("aluminum_truss", TrussBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.noOcclusion())
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .item()
            .build()
            .lang("Aluminum Truss")
            .register();

    public static final BlockEntry<Block> CAUTION_BLOCK = REGISTRATE.block("caution_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_YELLOW))
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Caution Block")
            .register();

    public static final BlockEntry<Block> RED_CAUTION_BLOCK = REGISTRATE.block("red_caution_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Red Caution Block")
            .register();


    public static final BlockEntry<MetalScaffoldingBlock> STEEL_SCAFFOLD =
            REGISTRATE.block("steel_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(4.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("steel",
                            () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/steel")), MaterialColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.STEEL_SCAFFOLD, TFMGSpriteShifts.STEEL_SCAFFOLD_INSIDE, TFMGSpriteShifts.STEEL_CASING))
                    .register();

    public static final BlockEntry<MetalScaffoldingBlock> ALUMINUM_SCAFFOLD =
            REGISTRATE.block("aluminum_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("aluminum",
                            () -> DataIngredient.tag(AllTags.forgeItemTag("ingots/steel")), MaterialColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.ALUMINUM_SCAFFOLD, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_INSIDE, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_TOP))
                    .register();



    public static final BlockEntry<TFMGFlywheelBlock> STEEL_FLYWHEEL = REGISTRATE.block("steel_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<TFMGFlywheelBlock> ALUMINUM_FLYWHEEL = REGISTRATE.block("aluminum_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_WHITE))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<TFMGFlywheelBlock> CAST_IRON_FLYWHEEL = REGISTRATE.block("cast_iron_flywheel", TFMGFlywheelBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<SlabBlock> FACTORY_FLOOR = withVariants("factory_floor",Blocks.STONE,
            MaterialColor.COLOR_GRAY,"Factory Floor",BlockTags.NEEDS_STONE_TOOL,SoundType.NETHERITE_BLOCK,false);

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


    public static final BlockEntry<ExhaustBlock> EXHAUST =
            REGISTRATE.block("exhaust", ExhaustBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<FlarestackBlock> FLARESTACK =
            REGISTRATE.block("flarestack", FlarestackBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                            .lightLevel(s -> s.getValue(FlarestackBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new FlarestackGenerator()::generate)
                    .item()
                    .transform(customItemModel())
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
    public static final BlockEntry<Block> INDUSTRIAL_PIPE = REGISTRATE.block("industrial_pipe", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Industrial Pipe")
            .register();
////////////////


    //Pumpjack
    public static final BlockEntry<MachineInputBlock> MACHINE_INPUT =
            REGISTRATE.block("machine_input", MachineInputBlock::new)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p
                            .strength(4.5F))
                    .transform(axeOrPickaxe())
                    .transform(BlockStressDefaults.setImpact(4.0))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<PumpjackCrankBlock> PUMPJACK_CRANK =
            REGISTRATE.block("pumpjack_crank", PumpjackCrankBlock::new)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .properties(p -> p
                            .strength(4.5F))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(axeOrPickaxe())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<PumpjackBaseBlock> PUMPJACK_BASE =
            REGISTRATE.block("pumpjack_base", PumpjackBaseBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<PumpjackHammerHolderBlock> PUMPJACK_HAMMER_HOLDER =
            REGISTRATE.block("pumpjack_hammer_holder", PumpjackHammerHolderBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .build()
                    .register();
    //////

    //Blast Furnace

    public static final BlockEntry<Block> FIREPROOF_BRICKS = REGISTRATE.block("fireproof_bricks", Block::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Fireproof Bricks")
            .register();
    public static final BlockEntry<WallBlock> FIREPROOF_BRICK_REINFORCEMENT = REGISTRATE.block("fireproof_brick_reinforcement", WallBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(BlockTags.WALLS)
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "fireproof_brick_reinforcement"))
            .item()
            .build()
            .lang("Fireproof Brick Reinforcement")
            .register();
    public static final BlockEntry<BlastFurnaceOutputBlock> BLAST_FURNACE_OUTPUT = REGISTRATE.block("blast_furnace_output", BlastFurnaceOutputBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Blast Furnace Output")
            .register();
    public static final BlockEntry<MoltenMetalBlock> MOLTEN_METAL =
            REGISTRATE.block("molten_metal", MoltenMetalBlock::new)
                    .initialProperties(SharedProperties.CRUSHING_WHEEL_CONTROLLER_MATERIAL)
                    .properties(p -> p.color(MaterialColor.COLOR_ORANGE))
                    .properties(p -> p.noOcclusion()
                            .noLootTable()
                            .air())
                    .blockstate((c, p) -> p.getVariantBuilder(c.get())
                            .forAllStatesExcept(BlockStateGen.mapToAir(p)))
                    .register();

    //--Casting Basin
    public static final BlockEntry<CastingBasinBlock> CASTING_BASIN = REGISTRATE.block("casting_basin", CastingBasinBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .build()
            .lang("Casting Basin")
            .register();

    public static final BlockEntry<CastingSpoutBlock> CASTING_SPOUT = REGISTRATE.block("casting_spout", CastingSpoutBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .build()
            .lang("Casting Spout")
            .register();


    //////////
    public static final BlockEntry<CokeOvenBlock> COKE_OVEN = REGISTRATE.block("coke_oven", CokeOvenBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.color(MaterialColor.COLOR_RED))
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(new CokeOvenGenerator()::generate)
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .lang("Coke Oven")
            .register();
    ///////


    public static final BlockEntry<AirIntakeBlock> AIR_INTAKE = REGISTRATE.block("air_intake", AirIntakeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new AirIntakeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .lang("Air Intake")
            .register();



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

    //public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
    //        .initialProperties(() -> Blocks.IRON_BLOCK)
    //        .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
    //        .properties(p -> p.requiresCorrectToolForDrops())
    //        .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
    //        .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
    //        .transform(pickaxeOnly())
    //        .blockstate(simpleCubeAll("lead_block"))
    //        .tag(BlockTags.NEEDS_IRON_TOOL)
    //        .tag(Tags.Blocks.STORAGE_BLOCKS)
    //        .tag(BlockTags.BEACON_BASE_BLOCKS)
    //        .transform(tagBlockAndItem("storage_blocks/lead"))
    //        .tag(Tags.Items.STORAGE_BLOCKS)
    //        .build()
    //        .lang("Block of Lead")
    //        .register();





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

    //-----------------------ENGINES---------------------------//
    public static final BlockEntry<GasolineEngineBlock> GASOLINE_ENGINE =
            REGISTRATE.block("gasoline_engine", GasolineEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<GasolineEngineBackBlock> GASOLINE_ENGINE_BACK =
            REGISTRATE.block("gasoline_engine_back", GasolineEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<LPGEngineBlock> LPG_ENGINE =
            REGISTRATE.block("lpg_engine", LPGEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<LPGEngineBackBlock> LPG_ENGINE_BACK =
            REGISTRATE.block("lpg_engine_back", LPGEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TurbineEngineBlock> TURBINE_ENGINE =
            REGISTRATE.block("turbine_engine", TurbineEngineBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TurbineEngineBackBlock> TURBINE_ENGINE_BACK =
            REGISTRATE.block("turbine_engine_back", TurbineEngineBackBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .blockstate(new EngineGenerator()::generate)
                    .transform(BlockStressDefaults.setCapacity(60.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .item()
                    .properties(p -> p.rarity(Rarity.UNCOMMON))
                    .transform(customItemModel())
                    .register();



    public static final BlockEntry<DieselEngineBlock> DIESEL_ENGINE =
            REGISTRATE.block("diesel_engine", DieselEngineBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
                    .transform(BlockStressDefaults.setCapacity(14.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(DieselEngineBlock::getSpeedRange))
                    .item()
                    .transform(customItemModel())
                    .register();

    //----------------------PIPES-------------------------------//

    //STEEL
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
    //CAST_IRON
    public static final BlockEntry<CastIronPipeBlock> CAST_IRON_PIPE = REGISTRATE.block("cast_iron_pipe", CastIronPipeBlock::new)
            .initialProperties(Material.HEAVY_METAL)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, CAST_IRON_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.CAST_IRON_PIPE))
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
                    .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, CAST_IRON_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> CAST_IRON_MECHANICAL_PUMP = REGISTRATE.block("cast_iron_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> CAST_IRON_FLUID_VALVE = REGISTRATE.block("cast_iron_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> CastIronPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();
    //BRASS
    public static final BlockEntry<BrassPipeBlock> BRASS_PIPE = REGISTRATE.block("brass_pipe", BrassPipeBlock::new)
            .initialProperties(Material.HEAVY_METAL)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, BRASS_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.BRASS_PIPE))
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
                    .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, STEEL_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> BRASS_MECHANICAL_PUMP = REGISTRATE.block("brass_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> BRASS_FLUID_VALVE = REGISTRATE.block("brass_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> BrassPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();
    //PLASTIC
    public static final BlockEntry<PlasticPipeBlock> PLASTIC_PIPE = REGISTRATE.block("plastic_pipe", PlasticPipeBlock::new)
            .initialProperties(Material.HEAVY_METAL)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, PLASTIC_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.PLASTIC_PIPE))
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
                    .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, PLASTIC_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> PLASTIC_MECHANICAL_PUMP = REGISTRATE.block("plastic_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> PLASTIC_FLUID_VALVE = REGISTRATE.block("plastic_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> PlasticPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();
    //ALUMINUM
    public static final BlockEntry<AluminumPipeBlock> ALUMINUM_PIPE = REGISTRATE.block("aluminum_pipe", AluminumPipeBlock::new)
            .initialProperties(Material.HEAVY_METAL)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, ALUMINUM_PIPE.get()))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_PIPE))
                    .register();


    @SuppressWarnings("removal")
    public static final BlockEntry<GlassAluminumPipeBlock> GLASS_ALUMINUM_PIPE =
            REGISTRATE.block("glass_aluminum_pipe", GlassAluminumPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/aluminum_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
                    .loot((p, b) -> p.dropOther(b, ALUMINUM_PIPE.get()))
                    .register();


    public static final BlockEntry<TFMGPumpBlock> ALUMINUM_MECHANICAL_PUMP = REGISTRATE.block("aluminum_mechanical_pump", TFMGPumpBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.color(MaterialColor.STONE))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
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
                    .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TFMGFluidValveBlock> ALUMINUM_FLUID_VALVE = REGISTRATE.block("aluminum_fluid_valve", TFMGFluidValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                    (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                            state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
            .onRegister(CreateRegistrate.blockModel(() -> AluminumPipeAttachmentModel::new))
            .item()
            .transform(customItemModel())
            .register();


    ////////////////////////////////////



    //-----------------------CONCRETE---------------------------//
    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }
    //public static final BlockEntry<Block> CONCRETE = REGISTRATE.block("concrete", Block::new)
    //        .initialProperties(() -> Blocks.STONE)
    //        .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
    //        .properties(p -> p.requiresCorrectToolForDrops())
    //        .transform(pickaxeOnly())
    //        .blockstate(simpleCubeAll("concrete"))
    //        .tag(BlockTags.NEEDS_STONE_TOOL)
    //        .transform(tagBlockAndItem("concrete"))
    //        .build()
    //        .lang("Concrete")
    //        .register();


    public static final BlockEntry<Block> CONCRETE = generateConcrete();


    public static final BlockEntry<SlabBlock> CONCRETE_SLAB =  REGISTRATE.block("concrete_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .item()
                .transform(customItemModel("concrete_bottom"))
            .lang("Concrete Slab")
                .register();


    public static BlockEntry<Block> generateConcrete(){


        generateColoredConcrete();



        REGISTRATE.block("concrete_wall", WallBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "concrete"))
                .build()
                .lang("Concrete Wall")
                .register();

        REGISTRATE.block("concrete_stairs", p -> new StairBlock(()-> TFMGBlocks.CONCRETE.get().defaultBlockState(),p))
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, "concrete"))
                .build()
                .lang("Concrete Stairs")
                .register();





        return REGISTRATE.block("concrete", Block::new)
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
    }



    //this saved so much time
    public static void generateColoredConcrete() {
        String[] colours = {"black", "white", "blue", "light_blue", "red", "green", "lime", "pink", "magenta", "yellow", "gray", "light_gray", "brown", "cyan", "purple", "orange"};


        for (String color : colours) {
            String firstLetter = color.substring(0, 1).toUpperCase();
            String colorWithoutC = color.substring(1);

            String upperCaseColor = firstLetter + colorWithoutC;
            String light = "Light";
            if(upperCaseColor.contains(light)){
                String nameWithoutLight = upperCaseColor.substring(6);

                String firstLetter2 = nameWithoutLight.substring(0, 1).toUpperCase();
                String colorWithoutC2 = nameWithoutLight.substring(1);

                upperCaseColor = light+" "+firstLetter2+colorWithoutC2;


            }
            REGISTRATE.block(color + "_concrete", Block::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate(simpleCubeAll(color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .item()
                    .build()
                    .lang(upperCaseColor + " Concrete")
                    .register();


            REGISTRATE.block(color + "_concrete_wall", WallBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.WALLS)
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, color + "_concrete"))
                    .build()
                    .lang(upperCaseColor + " Concrete Wall")
                    .register();

            REGISTRATE.block(color + "_concrete_stairs", p -> new StairBlock(()-> TFMGBlocks.CONCRETE.get().defaultBlockState(),p))
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.STAIRS)
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, color + "_concrete"))
                    .build()
                    .lang(upperCaseColor + " Concrete Stairs")
                    .register();



            REGISTRATE.block(color + "_concrete_slab", SlabBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.WALLS)
                    .item()
                    .transform(customItemModel(color+"_concrete_bottom"))
                    .lang(upperCaseColor + " Concrete Slab")
                    .register();


        }
    }
    public static BlockEntry<SlabBlock> withVariants(String name, Block properties, MaterialColor color,
                                                 String displayName, TagKey<Block> toolRequired,SoundType sound, boolean wall){

        if(wall)
            REGISTRATE.block(name+"_wall", WallBlock::new)
                    .initialProperties(() -> properties)
                    .properties(p -> p.color(color))
                    .properties(p -> p.sound(sound))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, name))
                    .tag(toolRequired)
                    .tag(BlockTags.WALLS)
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, name))
                    .build()
                    .lang(displayName+" Wall")
                    .register();
//
        REGISTRATE.block(name+"_stairs", p -> new StairBlock(()-> TFMGBlocks.CONCRETE.get().defaultBlockState(),p))
                .initialProperties(() -> properties)
                .properties(p -> p.color(color))
                .properties(p -> p.sound(sound))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, name))
                .tag(toolRequired)
                .tag(BlockTags.STAIRS)
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, name))
                .build()
                .lang(displayName+" Stairs")
                .register();




//


         REGISTRATE.block(name, Block::new)
                .initialProperties(() -> properties)
                .properties(p -> p.color(color))
                .properties(p -> p.sound(sound))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate(simpleCubeAll(name))
                .tag(toolRequired)
                .transform(tagBlockAndItem(name))
                .build()
                .lang(displayName)
                .register();

        return  REGISTRATE.block(name+"_slab", SlabBlock::new)
                .initialProperties(() -> properties)
                .properties(p -> p.color(color))
                .properties(p -> p.sound(sound))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, name))
                .tag(toolRequired)
                .tag(BlockTags.WALLS)
                .item()
                .transform(customItemModel(name+"_bottom"))
                .lang(displayName+" Slab")
                .register();


    }

    public static void register() {}
}
