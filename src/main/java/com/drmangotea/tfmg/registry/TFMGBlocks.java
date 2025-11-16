package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.base.*;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.blocks.TFMGVanillaBlockStates;
import com.drmangotea.tfmg.config.TFMGStress;
import com.drmangotea.tfmg.content.decoration.*;
import com.drmangotea.tfmg.content.decoration.cogs.TFMGCogWheelBlock;
import com.drmangotea.tfmg.content.decoration.cogs.TFMGCogwheelBlockItem;
import com.drmangotea.tfmg.content.decoration.concrete.*;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.content.decoration.gearbox.SteelGearboxBlock;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankItem;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankItem;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelFluidTankModel;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGTankGenerator;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankItem;
import com.drmangotea.tfmg.content.electricity.connection.cable_hub.CableHubBlock;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlock;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorGenerator;
import com.drmangotea.tfmg.content.electricity.connection.copycat_cable.CopycatCableBlock;
import com.drmangotea.tfmg.content.electricity.connection.copycat_cable.CopycatCableBlockModel;
import com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlock;
import com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableGenerator;
import com.drmangotea.tfmg.content.electricity.connection.tube.CableTubeBlock;
import com.drmangotea.tfmg.content.electricity.generators.GeneratorBlock;
import com.drmangotea.tfmg.content.electricity.generators.creative_generator.CreativeGeneratorBlock;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.RotorBlock;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlock;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorGenerator;
import com.drmangotea.tfmg.content.machinery.misc.gas_lamp.GasLampBlock;
import com.drmangotea.tfmg.content.electricity.lights.LampGenerator;
import com.drmangotea.tfmg.content.electricity.lights.LightBulbBlock;
import com.drmangotea.tfmg.content.electricity.lights.neon_tube.NeonTubeBlock;
import com.drmangotea.tfmg.content.electricity.measurement.VoltMeterBlock;
import com.drmangotea.tfmg.content.electricity.storage.AccumulatorBlock;
import com.drmangotea.tfmg.content.electricity.storage.AccumulatorItem;
import com.drmangotea.tfmg.content.electricity.storage.CapacitorCTBehavior;
import com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterBlock;
import com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterGenerator;
import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlock;
import com.drmangotea.tfmg.content.electricity.utilities.diode.EncasedDiodeBlock;
import com.drmangotea.tfmg.content.electricity.utilities.electric_motor.ElectricMotorBlock;
import com.drmangotea.tfmg.content.electricity.utilities.electric_pump.ElectricPumpBlock;
import com.drmangotea.tfmg.content.electricity.utilities.electric_switch.ElectricSwitchBlock;
import com.drmangotea.tfmg.content.electricity.utilities.polarizer.PolarizerBlock;
import com.drmangotea.tfmg.content.electricity.utilities.potentiometer.PotentiometerBlock;
import com.drmangotea.tfmg.content.electricity.utilities.potentiometer.EncasedPotentiometerBlock;
import com.drmangotea.tfmg.content.electricity.utilities.resistor.ResistorBlock;
import com.drmangotea.tfmg.content.electricity.utilities.resistor.ResistorBlockItem;
import com.drmangotea.tfmg.content.electricity.utilities.segmented_display.SegmentedDisplayBlock;
import com.drmangotea.tfmg.content.electricity.utilities.segmented_display.SegmentedDisplayCTBehavior;
import com.drmangotea.tfmg.content.electricity.utilities.traffic_light.TrafficLightBlock;
import com.drmangotea.tfmg.content.electricity.utilities.transformer.TransformerBlock;
import com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.VoltageObserverBlock;
import com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.VoltageObserverGenerator;
import com.drmangotea.tfmg.content.engines.base.EngineCTBehavior;
import com.drmangotea.tfmg.content.engines.base.EngineGenerator;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlock;
import com.drmangotea.tfmg.content.engines.engine_gearbox.EngineGearboxBlock;
import com.drmangotea.tfmg.content.engines.types.large_engine.LargeEngineBlock;
import com.drmangotea.tfmg.content.engines.types.radial_engine.RadialEngineBlock;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlock;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineGenerator;
import com.drmangotea.tfmg.content.items.CoalCokeBlockItem;
import com.drmangotea.tfmg.content.items.weapons.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceHatchBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceOutputBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement.BlastFurnaceReinforcementBlockItem;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement.BlastFurnaceReinforcementWallBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveItem;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.MultiblockCTBehavior;
import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlock;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenCTBehavior;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenGenerator;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlock;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeGenerator;
import com.drmangotea.tfmg.content.machinery.misc.concrete_hose.ConcreteHoseBlock;
import com.drmangotea.tfmg.content.machinery.misc.exhaust.ExhaustBlock;
import com.drmangotea.tfmg.content.machinery.misc.firebox.FireboxBlock;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackBlock;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackGenerator;
import com.drmangotea.tfmg.content.machinery.misc.gas_lamp.GasLampGenerator;
import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlock;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlock;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackGenerator;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.IndustrialPipeBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output.DistillationOutputBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base.PumpjackBaseBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.crank.PumpjackCrankBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.PumpjackBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.PumpjackGenerator;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.PumpjackHammerHeadBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.PumpjackHammerPartBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.large.LargePumpjackHammerConnectorBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.large.LargePumpjackHammerHeadBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.large.LargePumpjackHammerPartBlock;
import com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner.SurfaceScannerBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatGenerator;
import com.drmangotea.tfmg.content.machinery.vat.base.VatItem;
import com.drmangotea.tfmg.content.machinery.vat.base.VatModel;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderBlock;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.contraptions.bearing.StabilizedBearingMovementBehaviour;
import com.simibubi.create.content.decoration.MetalLadderBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.TrainTrapdoorBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.fluids.tank.FluidTankMovementBehavior;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.base.TFMGBuilderTransformers.*;
import static com.drmangotea.tfmg.content.electricity.lights.LightBulbBlock.LIGHT;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType.mountedFluidStorage;
import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;
import static com.simibubi.create.foundation.data.TagGen.tagBlockAndItem;
import static com.drmangotea.tfmg.registry.TFMGTags.forgeBlockTag;
import static com.drmangotea.tfmg.registry.TFMGTags.forgeItemTag;

@SuppressWarnings("removal")
public class TFMGBlocks {


    public static final String[] TFMG_DECOR_METALS = {"steel", "aluminum", "lead", "cast_iron"};


    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    }

    //------------------ENGINES------------------//

    public static final BlockEntry<TurbineEngineBlock> TURBINE_ENGINE = REGISTRATE.block("turbine_engine", TurbineEngineBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(TFMGStress.setCapacity(10))
            .transform(pickaxeOnly())
            .blockstate(new TurbineEngineGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<RegularEngineBlock> REGULAR_ENGINE = REGISTRATE.block("regular_engine", RegularEngineBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new EngineCTBehavior(TFMGSpriteShifts.REGULAR_ENGINE_TOP, TFMGSpriteShifts.REGULAR_ENGINE_BOTTOM, TFMGSpriteShifts.REGULAR_ENGINE_SIDE)))
            .blockstate(new EngineGenerator()::generate)
            .transform(TFMGStress.setCapacity(15))
            .transform(pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<RadialEngineBlock> RADIAL_ENGINE = REGISTRATE.block("radial_engine", RadialEngineBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(15))
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new EngineCTBehavior(TFMGSpriteShifts.REGULAR_ENGINE_TOP, TFMGSpriteShifts.REGULAR_ENGINE_BOTTOM, TFMGSpriteShifts.REGULAR_ENGINE_SIDE)))
            .blockstate(new TurbineEngineGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<LargeEngineBlock> LARGE_ENGINE = REGISTRATE.block("large_engine", LargeEngineBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(55))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<LargeEngineBlock> SIMPLE_LARGE_ENGINE = REGISTRATE.block("simple_large_engine", LargeEngineBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TFMGStress.setCapacity(40))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EngineGearboxBlock> ENGINE_GEARBOX = REGISTRATE.block("engine_gearbox", EngineGearboxBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<EngineControllerBlock> ENGINE_CONTROLLER = REGISTRATE.block("engine_controller", EngineControllerBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();
    //------------------TANKS------------------//
    public static final BlockEntry<AluminumTankBlock> ALUMINUM_FLUID_TANK =
            REGISTRATE.block("aluminum_fluid_tank", AluminumTankBlock::regular)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .blockstate(new TFMGTankGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> AluminumFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(AluminumTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();
    public static final BlockEntry<CastIronTankBlock> CAST_IRON_FLUID_TANK =
            REGISTRATE.block("cast_iron_fluid_tank", CastIronTankBlock::regular)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.METAL))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new TFMGTankGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> CastIronFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(CastIronTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    //------------------DISTILLATION_TOWER------------------//
    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK =
            REGISTRATE.block("steel_fluid_tank", SteelTankBlock::regular)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new TFMGTankGenerator()::generate)
                    .transform(mountedFluidStorage(TFMGMountedStorageTypes.TFMG_FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::standard))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(SteelTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();

    public static final BlockEntry<DistillationOutputBlock> STEEL_DISTILLATION_OUTPUT =
            REGISTRATE.block("steel_distillation_output", DistillationOutputBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<DistillationControllerBlock> STEEL_DISTILLATION_CONTROLLER =
            REGISTRATE.block("steel_distillation_controller", DistillationControllerBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<IndustrialPipeBlock> INDUSTRIAL_PIPE = REGISTRATE.block("industrial_pipe", IndustrialPipeBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.requiresCorrectToolForDrops())
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .tag(TFMGTags.TFMGBlockTags.INDUSTRIAL_PIPE.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/steel")), RecipeCategory.BUILDING_BLOCKS, c::get, 8))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> CONCRETE_ENCASED_INDUSTRIAL_PIPE = REGISTRATE.block("concrete_encased_industrial_pipe", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops().noOcclusion())
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.INDUSTRIAL_PIPE.get().asItem()))
            .tag(TFMGTags.TFMGBlockTags.INDUSTRIAL_PIPE.tag)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .register();

    //------------------PUMPJACK------------------//
    public static final BlockEntry<PumpjackBlock> PUMPJACK_HAMMER =
            REGISTRATE.block("pumpjack_hammer", PumpjackBlock::new)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new PumpjackGenerator()::generate)
                    .onRegister(movementBehaviour(new StabilizedBearingMovementBehaviour()))
                    .item()
                    .transform(customItemModel())
                    .lang("Pumpjack Hammer Holder")
                    .register();


    public static final BlockEntry<PumpjackCrankBlock> PUMPJACK_CRANK =
            REGISTRATE.block("pumpjack_crank", PumpjackCrankBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .build()
                    .lang("Pumpjack Crank")
                    .register();

    public static final BlockEntry<PumpjackHammerPartBlock> PUMPJACK_HAMMER_PART = REGISTRATE.block("pumpjack_hammer_part", PumpjackHammerPartBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)

            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_SMALL_PART.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("storage_blocks/steel")),
                    RecipeCategory.DECORATIONS, c::get, 2))
            .item()
            .build()
            .register();
    public static final BlockEntry<PumpjackHammerHeadBlock> PUMPJACK_HAMMER_HEAD = REGISTRATE.block("pumpjack_hammer_head", PumpjackHammerHeadBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_HEAD.tag)


            .item()
            .build()
            .register();

    public static final BlockEntry<PumpjackHammerConnectorBlock> PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("pumpjack_hammer_connector", PumpjackHammerConnectorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_CONNECTOR.tag)
            .item()
            .build()
            .register();
    public static final BlockEntry<LargePumpjackHammerPartBlock> LARGE_PUMPJACK_HAMMER_PART = REGISTRATE.block("large_pumpjack_hammer_part", LargePumpjackHammerPartBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_PART.tag)
            .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("storage_blocks/steel")),
                    RecipeCategory.DECORATIONS, c::get, 2))
            .item()
            .build()
            .register();

    public static final BlockEntry<LargePumpjackHammerHeadBlock> LARGE_PUMPJACK_HAMMER_HEAD = REGISTRATE.block("large_pumpjack_hammer_head", LargePumpjackHammerHeadBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_HEAD.tag)
            .item()
            .build()
            .register();
    public static final BlockEntry<LargePumpjackHammerConnectorBlock> LARGE_PUMPJACK_HAMMER_CONNECTOR = REGISTRATE.block("large_pumpjack_hammer_connector", LargePumpjackHammerConnectorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .tag(TFMGTags.TFMGBlockTags.PUMPJACK_CONNECTOR.tag)
            .item()
            .build()
            .register();
    public static final BlockEntry<PumpjackBaseBlock> PUMPJACK_BASE = REGISTRATE.block("pumpjack_base", PumpjackBaseBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> OIL_DEPOSIT = REGISTRATE.block("oil_deposit", Block::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.strength(69696969))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(TFMGTags.TFMGBlockTags.SURFACE_SCANNER_FINDABLE.tag)
            .tag(BlockTags.WITHER_IMMUNE)
            .tag(BlockTags.DRAGON_IMMUNE)
            .tag(BlockTags.DRAGON_IMMUNE)
            .tag(BlockTags.INFINIBURN_OVERWORLD)
            .tag(BlockTags.FEATURES_CANNOT_REPLACE)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .item()
            .build()
            .register();
    //------------------VAT_MACHINES------------------//
    @SuppressWarnings("'addLayer(java.util.function.Supplier<java.util.function.Supplier<net.minecraft.client.renderer.RenderType>>)' is deprecated and marked for removal ")
    public static final BlockEntry<VatBlock> STEEL_CHEMICAL_VAT =
            REGISTRATE.block("steel_chemical_vat", VatBlock::steel)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> VatModel::steelVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem.SteelVatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single"))
                    .build()
                    .register();
    public static final BlockEntry<VatBlock> CAST_IRON_CHEMICAL_VAT =
            REGISTRATE.block("cast_iron_chemical_vat", VatBlock::cast_iron)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> VatModel::castIronVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem.CastIronVatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single"))
                    .build()
                    .register();
    public static final BlockEntry<VatBlock> FIREPROOF_CHEMICAL_VAT =
            REGISTRATE.block("fireproof_chemical_vat", VatBlock::fireproof)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.isRedstoneConductor((p1, p2, p3) -> true))
                    .transform(pickaxeOnly())
                    .blockstate(new VatGenerator()::generate)
                    .onRegister(CreateRegistrate.blockModel(() -> VatModel::fireproofVat))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(VatItem.FireproofVatItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single"))
                    .build()
                    .register();

    public static final BlockEntry<IndustrialMixerBlock> INDUSTRIAL_MIXER = REGISTRATE.block("industrial_mixer", IndustrialMixerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<ElectrodeHolderBlock> ELECTRODE_HOLDER = REGISTRATE.block("electrode_holder", ElectrodeHolderBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();


    //------------------ORES------------------//
    public static final BlockEntry<Block> LEAD_ORE = REGISTRATE.block("lead_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/lead"))
            .tag(forgeBlockTag("ores_in_ground/stone"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/lead"), forgeItemTag("ores/lead")))
            .tag(forgeItemTag("ores_in_ground/stone"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE = REGISTRATE.block("deepslate_lead_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LEAD.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/lead"))
            .tag(forgeBlockTag("ores_in_ground/deepslate"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/lead"), forgeItemTag("ores/lead")))
            .tag(forgeItemTag("ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> NICKEL_ORE = REGISTRATE.block("nickel_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/nickel"))
            .tag(forgeBlockTag("ores_in_ground/stone"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/nickel"), forgeItemTag("ores/nickel")))
            .tag(forgeItemTag("ores_in_ground/stone"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_NICKEL_ORE = REGISTRATE.block("deepslate_nickel_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_NICKEL.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/nickel"))
            .tag(forgeBlockTag("ores_in_ground/deepslate"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/nickel"), forgeItemTag("ores/nickel")))
            .tag(forgeItemTag("ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> LITHIUM_ORE = REGISTRATE.block("lithium_ore", Block::new)
            .initialProperties(() -> Blocks.GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/lithium"))
            .tag(forgeBlockTag("ores_in_ground/stone"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/lithium"), forgeItemTag("ores/lithium")))
            .tag(forgeItemTag("ores_in_ground/stone"))
            .tag(Tags.Items.ORES)
            .build()
            .register();

    public static final BlockEntry<Block> DEEPSLATE_LITHIUM_ORE = REGISTRATE.block("deepslate_lithium_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
            .properties(p -> p
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE))
            .transform(pickaxeOnly())
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(TFMGItems.RAW_LITHIUM.get())
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(forgeBlockTag("ores/lithium"))
            .tag(forgeBlockTag("ores_in_ground/deepslate"))
            .transform(tagBlockAndItem(forgeBlockTag("ores/lithium"), forgeItemTag("ores/lithium")))
            .tag(forgeItemTag("ores_in_ground/deepslate"))
            .tag(Tags.Items.ORES)
            .build()
            .register();
    public static final BlockEntry<Block> SULFUR = REGISTRATE.block("sulfur", Block::new)
            .initialProperties(() -> Blocks.CALCITE)
            .transform(pickaxeOnly())
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> LIGNITE = REGISTRATE.block("lignite", Block::new)
            .initialProperties(() -> Blocks.CALCITE)
            .transform(pickaxeOnly())
            .item()
            .build()
            .register();
    public static final BlockEntry<Block> FIRECLAY = REGISTRATE.block("fireclay", Block::new)
            .initialProperties(() -> Blocks.CLAY)
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .loot((p, b) -> p
                    .add(b, p.createSingleItemTable(TFMGItems.FIRECLAY_BALL.get())))
            .simpleItem()
            .register();
    //------------------MISC------------------//
    public static final BlockEntry<Block> FOSSILSTONE = REGISTRATE.block("fossilstone", Block::new)
            .initialProperties(() -> Blocks.OBSIDIAN)
            .properties(p -> p.strength(100f, 1200f))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .item()
            .build()
            .lang("Fossilstone")
            .register();
    public static final BlockEntry<Block> SLAG_BLOCK =
            REGISTRATE.block("slag_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CALCITE))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .lang("Block of Slag")
                    .register();
    public static final BlockEntry<Block> RAW_NICKEL_BLOCK = REGISTRATE.block("raw_nickel_block", Block::new)
            .initialProperties(() -> Blocks.RAW_GOLD_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .lang("Block of Raw Nickel")
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/raw_nickel"), forgeItemTag("storage_blocks/raw_nickel")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();
    public static final BlockEntry<Block> RAW_LEAD_BLOCK = REGISTRATE.block("raw_lead_block", Block::new)
            .initialProperties(() -> Blocks.RAW_GOLD_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .lang("Block of Raw Lead")
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/raw_lead"), forgeItemTag("storage_blocks/raw_lead")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();
    public static final BlockEntry<Block> RAW_LITHIUM_BLOCK = REGISTRATE.block("raw_lithium_block", Block::new)
            .initialProperties(() -> Blocks.RAW_GOLD_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .lang("Block of Raw Lithium")
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/raw_lithium"), forgeItemTag("storage_blocks/raw_lithium")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();


    //------------------MISC_MACHINERY------------------//
    public static final BlockEntry<AirIntakeBlock> AIR_INTAKE = REGISTRATE.block("air_intake", AirIntakeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new AirIntakeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    @SuppressWarnings("'addLayer(java.util.function.Supplier<java.util.function.Supplier<net.minecraft.client.renderer.RenderType>>)' is deprecated and marked for removal ")
    public static final BlockEntry<FireboxBlock> FIREBOX =
            REGISTRATE.block("firebox", FireboxBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHER_BRICKS))
                    .transform(pickaxeOnly())
                    .onRegister(CreateRegistrate.connectedTextures(() -> new MultiblockCTBehavior(null, TFMGSpriteShifts.FIREBOX_TOP)))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item(SteelTankItem::new)
                    .build()
                    .register();

    public static final BlockEntry<WindingMachineBlock> WINDING_MACHINE = REGISTRATE.block("winding_machine", WindingMachineBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .transform(TFMGStress.setImpact(4.0))
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SurfaceScannerBlock> SURFACE_SCANNER = REGISTRATE.block("surface_scanner", SurfaceScannerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::translucent)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<MachineInputBlock> MACHINE_INPUT =
            REGISTRATE.block("machine_input", MachineInputBlock::new)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p
                            .strength(4.5F))
                    .transform(axeOrPickaxe())
                    .transform(TFMGStress.setImpact(4.0))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ConcreteHoseBlock> CONCRETE_HOSE = REGISTRATE.block("concrete_hose", ConcreteHoseBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(TFMGStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();


    //------------------METALLURGY------------------//
    public static final BlockEntry<BlastFurnaceOutputBlock> BLAST_FURNACE_OUTPUT = REGISTRATE.block("blast_furnace_output", BlastFurnaceOutputBlock::new)
            .initialProperties(() -> Blocks.NETHER_BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = REGISTRATE.block("blast_furnace_hatch", BlastFurnaceHatchBlock::new)
            .initialProperties(() -> Blocks.NETHER_BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(TFMGTags.TFMGBlockTags.BLAST_FURNACE_WALL.tag)
            .tag(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_WALL.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();
    public static final BlockEntry<Block> FIREPROOF_BRICKS = REGISTRATE.block("fireproof_bricks", Block::new)
            .initialProperties(() -> Blocks.NETHER_BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(TFMGTags.TFMGBlockTags.BLAST_FURNACE_WALL.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item()
            .build()
            .register();

    public static final BlockEntry<Block> BLAST_FURNACE_REINFORCEMENT = REGISTRATE.block("blast_furnace_reinforcement", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.BLAST_FURNACE_REINFORCEMENT)))
            .tag(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item(BlastFurnaceReinforcementBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<BlastFurnaceReinforcementWallBlock> BLAST_FURNACE_REINFORCEMENT_WALL = REGISTRATE.block("blast_furnace_reinforcement_wall", BlastFurnaceReinforcementWallBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.BLAST_FURNACE_REINFORCEMENT)))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.BLAST_FURNACE_REINFORCEMENT.get().asItem()))
            .register();
    //
    public static final BlockEntry<Block> RUSTED_BLAST_FURNACE_REINFORCEMENT = REGISTRATE.block("rusted_blast_furnace_reinforcement", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.RUSTED_BLAST_FURNACE_REINFORCEMENT)))
            .tag(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item(BlastFurnaceReinforcementBlockItem::new)
            .build()
            .register();

    public static final BlockEntry<BlastFurnaceReinforcementWallBlock> RUSTED_BLAST_FURNACE_REINFORCEMENT_WALL = REGISTRATE.block("rusted_blast_furnace_reinforcement_wall", BlastFurnaceReinforcementWallBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(TFMGSpriteShifts.RUSTED_BLAST_FURNACE_REINFORCEMENT)))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT.get().asItem()))
            .register();


    public static final BlockEntry<WallBlock> FIREPROOF_BRICK_REINFORCEMENT = REGISTRATE.block("fireproof_brick_reinforcement", WallBlock::new)
            .initialProperties(() -> Blocks.NETHER_BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(BlockTags.WALLS)
            .tag(TFMGTags.TFMGBlockTags.BLAST_FURNACE_SUPPORT.tag)
            .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "fireproof_brick_reinforcement"))
            .item()
            .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "fireproof_brick_reinforcement"))
            .build()
            .register();


    public static final BlockEntry<CokeOvenBlock> COKE_OVEN = REGISTRATE.block("coke_oven", CokeOvenBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .blockstate(new CokeOvenGenerator()::generate)
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(CokeOvenCTBehavior::new))
            .item()
            .transform(customItemModel())
            .register();

    @SuppressWarnings("'addLayer(java.util.function.Supplier<java.util.function.Supplier<net.minecraft.client.renderer.RenderType>>)' is deprecated and marked for removal ")
    public static final BlockEntry<BlastStoveBlock> BLAST_STOVE =
            REGISTRATE.block("blast_stove", BlastStoveBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .transform(pickaxeOnly())
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new MultiblockCTBehavior(TFMGSpriteShifts.BLAST_STOVE_SIDE, TFMGSpriteShifts.BLAST_STOVE_TOP)))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(BlastStoveItem::new)
                    .build()
                    .register();

    public static final BlockEntry<CastingBasinBlock> CASTING_BASIN = REGISTRATE.block("casting_basin", CastingBasinBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    //------------------GADGETS------------------//

    public static final BlockEntry<NapalmBombBlock> NAPALM_BOMB = REGISTRATE.block("napalm_bomb", NapalmBombBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .simpleItem()
            .register();

    //------------------ELECTRICITY------------------//

    public static final BlockEntry<GeneratorBlock> GENERATOR =
            REGISTRATE.block("generator", GeneratorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(TFMGStress.setImpact(50.0f))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<CableConnectorBlock> CABLE_CONNECTOR =
            REGISTRATE.block("cable_connector", CableConnectorBlock::new)
                    .initialProperties(() -> Blocks.TERRACOTTA)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CableConnectorGenerator()::generate)
                    .lang("Cable Insulator")
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<CableConnectorBlock> GLASS_CABLE_CONNECTOR =
            REGISTRATE.block("glass_cable_insulator", CableConnectorBlock::new)
                    .initialProperties(() -> Blocks.TERRACOTTA)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::translucent)
                    .blockstate(new CableConnectorGenerator()::generate)
                    .lang("Glass Cable Insulator")
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ResistorBlock> RESISTOR =
            REGISTRATE.block("resistor", ResistorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item(ResistorBlockItem::new)
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<CopycatCableBlock> COPYCAT_CABLE_BLOCK =
            REGISTRATE.block("copycat_cable_block", CopycatCableBlock::new)
                    .transform(TFMGBuilderTransformers.copycatCable())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatCableBlockModel::new))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> COPYCAT_CABLE_BASE = REGISTRATE.block("copycat_cable_base", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .register();
    public static final BlockEntry<CableHubBlock> BRASS_CABLE_HUB =
            REGISTRATE.block("brass_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> COPPER_CABLE_HUB =
            REGISTRATE.block("copper_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.COPPER_BLOCK)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> STEEL_CABLE_HUB =
            REGISTRATE.block("steel_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> ALUMINUM_CABLE_HUB =
            REGISTRATE.block("aluminum_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> STEEL_CASING_CABLE_HUB =
            REGISTRATE.block("steel_casing_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();
    public static final BlockEntry<CableHubBlock> HEAVY_CABLE_HUB =
            REGISTRATE.block("heavy_cable_hub", CableHubBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<CableTubeBlock> CABLE_TUBE =
            REGISTRATE.block("cable_tube", p -> new CableTubeBlock(p, false))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(p -> p.noOcclusion())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<CableTubeBlock> CONCRETE_ENCASED_CABLE_TUBE =
            REGISTRATE.block("concrete_encased_cable_tube", p -> new CableTubeBlock(p, true))
                    .initialProperties(() -> Blocks.STONE)
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .transform(pickaxeOnly())
                    .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.CABLE_TUBE.get().asItem()))
                    .register();

    public static final BlockEntry<CableTubeBlock> ELECTRIC_POST =
            REGISTRATE.block("electric_post", p -> new CableTubeBlock(p, false))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<CableTubeBlock> CONCRETE_ENCASED_ELECTRIC_POST =
            REGISTRATE.block("concrete_encased_electric_post", p -> new CableTubeBlock(p, true))
                    .initialProperties(() -> Blocks.STONE)
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .transform(pickaxeOnly())
                    .loot((lt, block) -> lt.dropOther(block, TFMGBlocks.ELECTRIC_POST.get().asItem()))
                    .register();

    public static final BlockEntry<DiagonalCableBlock> DIAGONAL_CABLE_BLOCK =
            REGISTRATE.block("diagonal_cable_block", DiagonalCableBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new DiagonalCableGenerator()::generate)
                    .properties(p -> p.noOcclusion())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR =
            REGISTRATE.block("electric_motor", ElectricMotorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .transform(TFMGStress.setCapacity(45.0))
                    .onRegister(BlockStressValues.setGeneratorSpeed(64, true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<CreativeGeneratorBlock> CREATIVE_GENERATOR = REGISTRATE.block("creative_generator", CreativeGeneratorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .simpleItem()
            .register();

    public static final BlockEntry<AccumulatorBlock> ACCUMULATOR =
            REGISTRATE.block("accumulator", AccumulatorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(() -> new CapacitorCTBehavior(TFMGSpriteShifts.ACCUMULATOR)))
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item(AccumulatorItem::new)
                    .build()
                    .register();

    public static final BlockEntry<GasLampBlock> GAS_LAMP =
            REGISTRATE.block("gas_lamp", GasLampBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p
                            .lightLevel(s -> s.getValue(GasLampBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new GasLampGenerator()::generate)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<LightBulbBlock> LIGHT_BULB =
            REGISTRATE.block("light_bulb", p -> new LightBulbBlock(p, TFMGBlockEntities.LIGHT_BULB, TFMGShapes.LIGHT_BULB))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<LightBulbBlock> CIRCULAR_LIGHT =
            REGISTRATE.block("circular_light", p -> new LightBulbBlock(p, TFMGBlockEntities.CIRCULAR_LIGHT, TFMGShapes.CIRCULAR_LIGHT))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Circular Lamp")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<LightBulbBlock> MODERN_LIGHT =
            REGISTRATE.block("modern_light", p -> new LightBulbBlock(p, TFMGBlockEntities.MODERN_LIGHT, TFMGShapes.MODERN_LIGHT))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Light Panel")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<LightBulbBlock> ALUMINUM_LAMP =
            REGISTRATE.block("aluminum_lamp", p -> new LightBulbBlock(p, TFMGBlockEntities.ALUMINUM_LAMP, TFMGShapes.ALUMINUM_LAMP))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new LampGenerator()::generate)
                    .lang("Aluminum Lamp")
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<NeonTubeBlock> NEON_TUBE =
            REGISTRATE.block("neon_tube", NeonTubeBlock::new)
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(p -> p.lightLevel(s -> s.getValue(LIGHT)))
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(TFMGBuilderTransformers::generateNeonTubeBlockState)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ElectricDiodeBlock> DIODE =
            REGISTRATE.block("electric_diode", ElectricDiodeBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .lang("Diode")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<EncasedDiodeBlock> ENCASED_DIODE =
            REGISTRATE.block("encased_diode", EncasedDiodeBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .transform(EncasingRegistry.addVariantTo(DIODE))
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .register();

    public static final BlockEntry<PotentiometerBlock> POTENTIOMETER =
            REGISTRATE.block("potentiometer", PotentiometerBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new CreativeMotorGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<ElectricSwitchBlock> ELECTRICAL_SWITCH =
            REGISTRATE.block("electrical_switch", ElectricSwitchBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .lang("Electric Switch")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<EncasedPotentiometerBlock> ENCASED_POTENTIOMETER =
            REGISTRATE.block("encased_potentiometer", EncasedPotentiometerBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .transform(EncasingRegistry.addVariantTo(POTENTIOMETER))
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .register();
    public static final BlockEntry<ElectricPumpBlock> ELECTRIC_PUMP =
            REGISTRATE.block("electric_pump", ElectricPumpBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<VoltageObserverBlock> VOLTAGE_OBSERVER =
            REGISTRATE.block("voltage_observer", VoltageObserverBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new VoltageObserverGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<VoltMeterBlock> VOLTMETER =
            REGISTRATE.block("voltmeter", VoltMeterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .lang("Electric Gauge")
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<PolarizerBlock> POLARIZER =
            REGISTRATE.block("polarizer", PolarizerBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RotorBlock> ROTOR =
            REGISTRATE.block("rotor", RotorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(BlockStateGen.axisBlockProvider(true))
                    .transform(TFMGStress.setImpact(240))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<StatorBlock> STATOR =
            REGISTRATE.block("stator", StatorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new StatorGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<TrafficLightBlock> TRAFFIC_LIGHT =
            REGISTRATE.block("traffic_light", TrafficLightBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<SegmentedDisplayBlock> SEGMENTED_DISPLAY =
            REGISTRATE.block("segmented_display", SegmentedDisplayBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .onRegister(connectedTextures(SegmentedDisplayCTBehavior::new))
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<TransformerBlock> TRANSFORMER =
            REGISTRATE.block("transformer", TransformerBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .blockstate(BlockStateGen.horizontalBlockProvider(true))
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<ConverterBlock> CONVERTER =
            REGISTRATE.block("converter", ConverterBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .transform(pickaxeOnly())
                    .blockstate(new ConverterGenerator()::generate)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item()
                    .transform(customItemModel())
                    .register();

    //public static final BlockEntry<FuseBlock> FUSE_BLOCK =
    //        REGISTRATE.block("fuse_block", FuseBlock::new)
    //                .initialProperties(() -> Blocks.IRON_BLOCK)
    //                .transform(pickaxeOnly())
    //                .properties(BlockBehaviour.Properties::noOcclusion)
    //                .addLayer(() -> RenderType::cutoutMipped)
    //                .blockstate(BlockStateGen.horizontalBlockProvider(true))
    //                .item()
    //                .transform(customItemModel())
    //                .register();


    //------------------EXHAUST/WASTE_REMOVAL------------------//
    public static final BlockEntry<SmokestackBlock> BRICK_SMOKESTACK = REGISTRATE.block("brick_smokestack", SmokestackBlock::new)
            .initialProperties(() -> Blocks.BRICKS)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SmokestackBlock> METAL_SMOKESTACK = REGISTRATE.block("metal_smokestack", SmokestackBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SmokestackBlock> CONCRETE_SMOKESTACK = REGISTRATE.block("concrete_smokestack", SmokestackBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(new SmokestackGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ExhaustBlock> EXHAUST =
            REGISTRATE.block("exhaust", ExhaustBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .blockstate(BlockStateGen.directionalBlockProvider(false))
                    .properties(p -> p.noOcclusion())
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final BlockEntry<FlarestackBlock> FLARESTACK =
            REGISTRATE.block("flarestack", FlarestackBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .properties(p -> p
                            .lightLevel(s -> s.getValue(FlarestackBlock.LIT) ? 15 : 0)
                            .noOcclusion())
                    .blockstate(new FlarestackGenerator()::generate)
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    //------------------COGWHEELS------------------//

    public static final BlockEntry<TFMGCogWheelBlock> STEEL_COGWHEEL =
            REGISTRATE.block("steel_cogwheel", TFMGCogWheelBlock::small)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(TFMGStress.setNoImpact())
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    public static final BlockEntry<TFMGCogWheelBlock> LARGE_STEEL_COGWHEEL =
            REGISTRATE.block("large_steel_cogwheel", TFMGCogWheelBlock::large)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(axeOrPickaxe())
                    .transform(TFMGStress.setNoImpact())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();
    //
    public static final BlockEntry<TFMGCogWheelBlock> ALUMINUM_COGWHEEL =
            REGISTRATE.block("aluminum_cogwheel", TFMGCogWheelBlock::small)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(TFMGStress.setNoImpact())
                    .transform(axeOrPickaxe())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    public static final BlockEntry<TFMGCogWheelBlock> LARGE_ALUMINUM_COGWHEEL =
            REGISTRATE.block("large_aluminum_cogwheel", TFMGCogWheelBlock::large)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.COPPER))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(axeOrPickaxe())
                    .transform(TFMGStress.setNoImpact())
                    .blockstate(BlockStateGen.axisBlockProvider(false))
                    .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                    .item(TFMGCogwheelBlockItem::new)
                    .build()
                    .register();

    //------------------CASINGS------------------//

    public static final BlockEntry<CasingBlock> STEEL_CASING = REGISTRATE.block("steel_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.STEEL_CASING))
            .register();
    public static final BlockEntry<CasingBlock> HEAVY_MACHINERY_CASING = REGISTRATE.block("heavy_machinery_casing", CasingBlock::new)
            .tag(TFMGTags.TFMGBlockTags.SURFACE_SCANNER_FINDABLE.tag)
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .register();
    public static final BlockEntry<CasingBlock> ALUMINUM_CASING = REGISTRATE.block("industrial_aluminum_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING))
            .properties(p -> p.sound(SoundType.COPPER))
            .lang("Aluminum Casing")
            .register();
    //------------------STORAGE_BLOCKS------------------//
    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)

            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_WALL.tag)
            .tag(TFMGTags.TFMGBlockTags.REINFORCED_BLAST_FURNACE_SUPPORT.tag)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/steel"), forgeItemTag("storage_blocks/steel")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Steel")
            .register();
    public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE.block("cast_iron_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)

            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("cast_iron_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/cast_iron"), forgeItemTag("storage_blocks/cast_iron")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Cast Iron")
            .register();

    public static final BlockEntry<Block> ALUMINUM_BLOCK = REGISTRATE.block("aluminum_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)

            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("aluminum_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/aluminum"), forgeItemTag("storage_blocks/aluminum")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Aluminum")
            .register();
    public static final BlockEntry<Block> PLASTIC_BLOCK = REGISTRATE.block("plastic_block", Block::new)
            .initialProperties(() -> Blocks.QUARTZ_BLOCK)

            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("plastic_block"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/plastic"), forgeItemTag("storage_blocks/plastic")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Plastic")
            .register();

    public static final BlockEntry<Block> LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lead_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/lead"), forgeItemTag("storage_blocks/lead")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lead")
            .register();

    public static final BlockEntry<Block> CONSTANTAN_BLOCK = REGISTRATE.block("constantan_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("constantan_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/constantan"), forgeItemTag("storage_blocks/constantan")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Constantan")
            .register();

    public static final BlockEntry<Block> NICKEL_BLOCK = REGISTRATE.block("nickel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem(forgeBlockTag("storage_blocks/nickel"), forgeItemTag("storage_blocks/nickel")))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Nickel")
            .register();

    public static final BlockEntry<LithiumBlock> LITHIUM_BLOCK = REGISTRATE.block("lithium_block", LithiumBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.LEAD_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.LEAD_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("lithium_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .tag(AllTags.forgeBlockTag("storage_blocks/lithium"))
            .item()
            .tag(AllTags.forgeItemTag("storage_blocks/lithium"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Lithium")
            .register();


    public static final BlockEntry<Block> COAL_COKE_BLOCK = REGISTRATE.block("coal_coke_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("coal_coke_block"))
            .tag(AllTags.forgeBlockTag("storage_blocks/coal_coke"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .item(CoalCokeBlockItem::new)
            .tag(AllTags.forgeItemTag("storage_blocks/coal_coke"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Coal Coke")
            .register();

    public static final BlockEntry<FallingBlock> CEMENT = REGISTRATE.block("cement", FallingBlock::new)
            .initialProperties(() -> Blocks.CLAY)
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .item()
            .build()
            .register();

    //------------------DOOR------------------//
    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_CASING_DOOR =
            REGISTRATE.block("heavy_casing_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_casing"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> STEEL_CASING_DOOR =
            REGISTRATE.block("steel_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), true))
                    .transform(TFMGBuilderTransformers.slidingDoor("steel"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();

    public static final BlockEntry<TFMGSlidingDoorBlock> ALUMINUM_DOOR =
            REGISTRATE.block("aluminum_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("aluminum"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();
    public static final BlockEntry<TFMGSlidingDoorBlock> HEAVY_PLATED_DOOR =
            REGISTRATE.block("heavy_plated_door", p -> new TFMGSlidingDoorBlock(p, SlidingDoorBlock.GLASS_SET_TYPE.get(), false))
                    .transform(TFMGBuilderTransformers.slidingDoor("heavy_plated"))
                    .properties(p -> p
                            .sound(SoundType.COPPER)
                            .noOcclusion())
                    .register();





    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_DECORATION);
    }

    //------------------GEARBOXES------------------//
    public static final BlockEntry<SteelGearboxBlock> STEEL_GEARBOX = REGISTRATE.block("steel_gearbox", SteelGearboxBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TFMGStress.setNoImpact())
            .transform(axeOrPickaxe())
            .properties(p -> p.noOcclusion())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                    (s, f) -> f.getAxis() == s.getValue(GearboxBlock.AXIS))))
            .blockstate((c, p) -> axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true))
            .item()
            .transform(customItemModel())
            .register();

    //------------------BARS------------------//
    public static final BlockEntry<IronBarsBlock> STEEL_BARS = TFMGMetalBarsGen.createBars("steel", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MapColor.TERRACOTTA_CYAN);
    public static final BlockEntry<IronBarsBlock> ALUMINUM_BARS = TFMGMetalBarsGen.createBars("aluminum", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MapColor.TERRACOTTA_WHITE);

    public static final BlockEntry<IronBarsBlock> CAST_IRON_BARS = TFMGMetalBarsGen.createBars("cast_iron", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/cast_iron")), MapColor.COLOR_BLACK);

    public static final BlockEntry<IronBarsBlock> LEAD_BARS = TFMGMetalBarsGen.createBars("lead", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/lead")), MapColor.TERRACOTTA_BLUE);

    public static final BlockEntry<IronBarsBlock> NICKEL_BARS = TFMGMetalBarsGen.createBars("nickel", true,
            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/nickel")), MapColor.TERRACOTTA_YELLOW);

    //------------------LADDERS------------------//
    public static final BlockEntry<MetalLadderBlock> STEEL_LADDER =
            REGISTRATE.block("steel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("steel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MapColor.TERRACOTTA_CYAN))
                    .register();
    public static final BlockEntry<MetalLadderBlock> ALUMINUM_LADDER =
            REGISTRATE.block("aluminum_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("aluminum",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MapColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> CAST_IRON_LADDER =
            REGISTRATE.block("cast_iron_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("cast_iron",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/cast_iron")), MapColor.TERRACOTTA_WHITE))
                    .register();

    public static final BlockEntry<MetalLadderBlock> LEAD_LADDER =
            REGISTRATE.block("lead_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("lead",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/lead")), MapColor.TERRACOTTA_WHITE))
                    .lang("Leadder")
                    .register();

    public static final BlockEntry<MetalLadderBlock> NICKEL_LADDER =
            REGISTRATE.block("nickel_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("nickel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/nickel")), MapColor.TERRACOTTA_WHITE))
                    .register();
    public static final BlockEntry<MetalLadderBlock> CONSTANTAN_LADDER =
            REGISTRATE.block("constantan_ladder", MetalLadderBlock::new)
                    .transform(BuilderTransformers.ladder("constantan",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/constantan")), MapColor.TERRACOTTA_WHITE))
                    .register();
    //------------------SCAFFOLDINGS------------------//
    public static final BlockEntry<MetalScaffoldingBlock> STEEL_SCAFFOLD =
            REGISTRATE.block("steel_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(4.0F)
                            .requiresCorrectToolForDrops())
                    .transform(BuilderTransformers.scaffold("steel",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/steel")), MapColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.STEEL_SCAFFOLD, TFMGSpriteShifts.STEEL_SCAFFOLD_INSIDE, TFMGSpriteShifts.STEEL_CASING))
                    .register();

    public static final BlockEntry<MetalScaffoldingBlock> ALUMINUM_SCAFFOLD =
            REGISTRATE.block("aluminum_scaffolding", MetalScaffoldingBlock::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops())

                    .transform(BuilderTransformers.scaffold("aluminum",
                            () -> DataIngredient.tag(TFMGTags.forgeItemTag("ingots/aluminum")), MapColor.TERRACOTTA_CYAN,
                            TFMGSpriteShifts.ALUMINUM_SCAFFOLD, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_INSIDE, TFMGSpriteShifts.ALUMINUM_SCAFFOLD_TOP))
                    .register();
    //------------------DECOR------------------//
    public static final BlockEntry<Block> SLAG_BRICKS =
            REGISTRATE.block("slag_bricks", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CALCITE))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(TFMGBlocks.SLAG_BLOCK.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 4))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> CINDER_BLOCK =
            REGISTRATE.block("cinder_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.BASALT))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> CINDERFLOUR_BLOCK =
            REGISTRATE.block("cinderflour_block", Block::new)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERRACK))
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .transform(pickaxeOnly())
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final MaterialSet SLAG_BRICKS_SET = makeVariants(SLAG_BRICKS, true);
    public static final BlockEntry<LithiumTorchBlock> LITHIUM_TORCH =
            REGISTRATE.block("lithium_torch", LithiumTorchBlock::new)
                    .initialProperties(() -> Blocks.TORCH)
                    .properties(p -> p.lightLevel(s -> 14).instabreak().noOcclusion())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new LithiumTorchGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<Block> FACTORY_FLOOR =
            REGISTRATE.block("factory_floor", Block::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p
                            .strength(3.0F)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK))
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/aluminum")), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    public static final MaterialSet FACTORY_FLOOR_SET = makeVariants(FACTORY_FLOOR, true);

    public static final BlockEntry<Block> HARDENED_PLANKS =
            REGISTRATE.block("hardened_planks", Block::new)
                    .initialProperties(() -> Blocks.OAK_PLANKS)
                    .properties(p -> p
                            .strength(2.0F)
                            .sound(SoundType.CHERRY_WOOD))
                    .transform(axeOnly())
                    .simpleItem()
                    .register();

    public static final MaterialSet HARDENED_PLANKS_SET = makeVariants(HARDENED_PLANKS, true);

    public static final BlockEntry<TrainTrapdoorBlock> STEEL_TRAPDOOR =
            REGISTRATE.block("steel_trapdoor", TrainTrapdoorBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN)
                            .sound(SoundType.NETHERITE_BLOCK))
                    .transform(BuilderTransformers.trapdoor(true))
                    .register();


    //------------------FLYWHEELS------------------//
    public static final BlockEntry<TFMGFlywheelBlock>
            STEEL_FLYWHEEL = flywheel("steel", TFMGFlywheelBlock::steel),
            LEAD_FLYWHEEL = flywheel("lead", TFMGFlywheelBlock::lead),
            CAST_IRON_FLYWHEEL = flywheel("cast_iron", TFMGFlywheelBlock::cast_iron),
            ALUMINUM_FLYWHEEL = flywheel("aluminum", TFMGFlywheelBlock::aluminum),
            NICKEL_FLYWHEEL = flywheel("nickel", TFMGFlywheelBlock::nickel);

    public static String[] DECOR_METALS = {"steel", "aluminum", "cast_iron", "lead", "nickel", "constantan", "copper", "zinc", "brass"};

    public static final List<BlockEntry<TrussBlock>> TRUSSES = new ArrayList<>();
    public static final List<BlockEntry<FrameBlock>> FRAMES = new ArrayList<>();

    static {
        //------------------TRUSSES------------------//
        for (String metal : DECOR_METALS) {

            TRUSSES.add(truss(metal));
            FRAMES.add(frame(metal));
        }
        //------------------CAUTION_BLOCKS------------------//
        generateCautionBlocks();
    }


    //------------------CONCRETE------------------//
    public static final BlockEntry<SimpleConcreteloggedBlock> REBAR_BLOCK =
            REGISTRATE.block("rebar_block", SimpleConcreteloggedBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarFloorBlock> REBAR_FLOOR =
            REGISTRATE.block("rebar_floor", RebarFloorBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarWallBlock> REBAR_WALL =
            REGISTRATE.block("rebar_wall", RebarWallBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<TFMGDirectionalBlock> REBAR_PILE =
            REGISTRATE.block("rebar_pile", TFMGDirectionalBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();
    public static final BlockEntry<RebarStairsBlock> REBAR_STAIRS =
            REGISTRATE.block("rebar_stairs", p -> new RebarStairsBlock(REBAR_PILE.getDefaultState(), p))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(new RebarStairsGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<RebarPillarBlock> REBAR_PILLAR =
            REGISTRATE.block("rebar_pillar", RebarPillarBlock::new)
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.noOcclusion())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate(BlockStateGen.directionalBlockProvider(true))
                    .item()
                    .transform(customItemModel())
                    .register();


    public static final MaterialSet CONCRETE = generateConcrete(false);
    public static final MaterialSet REBAR_CONCRETE = generateConcrete(true);


    public static final Map<String, MaterialSet> COLORED_CONCRETE = generateColoredConcrete(false);
    public static final Map<String, MaterialSet> COLORED_REBAR_CONCRETE = generateColoredConcrete(true);


    public static final BlockEntry<Block> ASPHALT = REGISTRATE.block("asphalt", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .item()
            .build()
            .register();
    public static final MaterialSet ASPHALT_SET = makeVariants(ASPHALT);

    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_MAIN);
    }


    public static void init() {

    }
}
