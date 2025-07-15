package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.HalfShaftRenderer;
import com.drmangotea.tfmg.content.decoration.cogs.*;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlockEntity;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorRenderer;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelBlockEntity;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelRenderer;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelVisual;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipeBlockEntity;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankBlockEntity;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankRenderer;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelFluidTankRenderer;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.cable_hub.CableHubBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorRenderer;
import com.drmangotea.tfmg.content.electricity.connection.copycat_cable.CopycatCableBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.tube.CableTubeBlockEntity;
import com.drmangotea.tfmg.content.electricity.generators.GeneratorBlockEntity;
import com.drmangotea.tfmg.content.electricity.generators.creative_generator.CreativeGeneratorBlockEntity;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.RotorBlockEntity;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.RotorRenderer;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.RotorVisual;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlockEntity;
import com.drmangotea.tfmg.content.electricity.lights.LightBulbBlockEntity;
import com.drmangotea.tfmg.content.electricity.lights.LightBulbRenderer;
import com.drmangotea.tfmg.content.electricity.lights.neon_tube.NeonTubeBlockEntity;
import com.drmangotea.tfmg.content.electricity.lights.neon_tube.NeonTubeRenderer;
import com.drmangotea.tfmg.content.electricity.lights.variants.AluminumLampRenderer;
import com.drmangotea.tfmg.content.electricity.lights.variants.CircularLightRenderer;
import com.drmangotea.tfmg.content.electricity.lights.variants.ModernLightRenderer;
import com.drmangotea.tfmg.content.electricity.measurement.VoltMeterBlockEntity;
import com.drmangotea.tfmg.content.electricity.measurement.VoltMeterRenderer;
import com.drmangotea.tfmg.content.electricity.storage.AccumulatorBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.electric_pump.ElectricPumpBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.electric_switch.ElectricSwitchBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.fuse_block.FuseBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.fuse_block.FuseBlockRenderer;
import com.drmangotea.tfmg.content.electricity.utilities.polarizer.PolarizerBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.polarizer.PolarizerRenderer;
import com.drmangotea.tfmg.content.electricity.utilities.potentiometer.PotentiometerBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.resistor.ResistorBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.segmented_display.SegmentedDisplayBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.segmented_display.SegmentedDisplayRenderer;
import com.drmangotea.tfmg.content.electricity.utilities.traffic_light.TrafficLightBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.traffic_light.TrafficLightRenderer;
import com.drmangotea.tfmg.content.electricity.utilities.transformer.TransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.utilities.transformer.TransformerRenderer;
import com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.VoltageObserverBlockEntity;
import com.drmangotea.tfmg.content.engines.base.EngineRenderer;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerRenderer;
import com.drmangotea.tfmg.content.engines.engine_gearbox.EngineGearboxBlockEntity;
import com.drmangotea.tfmg.content.engines.engine_gearbox.EngineGearboxRenderer;
import com.drmangotea.tfmg.content.engines.types.large_engine.LargeEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.large_engine.LargeEngineRenderer;
import com.drmangotea.tfmg.content.engines.types.radial_engine.RadialEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineRenderer;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineVisual;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceHatchBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceRenderer;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinRenderer;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenRenderer;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeRenderer;
import com.drmangotea.tfmg.content.machinery.misc.concrete_hose.ConcreteHoseBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.concrete_hose.ConcreteHoseRenderer;
import com.drmangotea.tfmg.content.machinery.misc.exhaust.ExhaustBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.firebox.FireboxBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineRenderer;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineVisual;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerRenderer;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output.DistillationOutputBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base.PumpjackBaseBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.crank.PumpjackCrankRenderer;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.PumpjackBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.PumpjackRenderer;
import com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner.SurfaceScannerBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner.SurfaceScannerRenderer;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.VatRenderer;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderRenderer;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerRenderer;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerVisual;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveVisual;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlockEntity;
import com.simibubi.create.content.kinetics.gearbox.GearboxRenderer;
import com.simibubi.create.content.kinetics.gearbox.GearboxVisual;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGBlockEntities {


    public static final BlockEntityEntry<DistillationOutputBlockEntity> DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillation_tower_output", DistillationOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
            .register();
    public static final BlockEntityEntry<ElectricPumpBlockEntity> ELECTRIC_PUMP = REGISTRATE
            .blockEntity("electric_pump", ElectricPumpBlockEntity::new)
            .validBlocks(TFMGBlocks.ELECTRIC_PUMP)
            .register();
    public static final BlockEntityEntry<ElectricSwitchBlockEntity> ELECTRIC_SWITCH = REGISTRATE
            .blockEntity("electrical_switch", ElectricSwitchBlockEntity::new)
            .validBlocks(TFMGBlocks.ELECTRICAL_SWITCH)
            .register();
    public static final BlockEntityEntry<PolarizerBlockEntity> POLARIZER = REGISTRATE
            .blockEntity("polarizer", PolarizerBlockEntity::new)
            .validBlocks(TFMGBlocks.POLARIZER)
            .renderer(() -> PolarizerRenderer::new)
            .register();
    public static final BlockEntityEntry<CopycatCableBlockEntity> COPYCAT_CABLE =
            REGISTRATE.blockEntity("copycat_cable", CopycatCableBlockEntity::new)
                    .validBlocks(TFMGBlocks.COPYCAT_CABLE_BLOCK)
                    .register();
    public static final BlockEntityEntry<ResistorBlockEntity> RESISTOR = REGISTRATE
            .blockEntity("resistor", ResistorBlockEntity::new)
            .validBlocks(TFMGBlocks.RESISTOR)
            .register();
    public static final BlockEntityEntry<StatorBlockEntity> STATOR = REGISTRATE
            .blockEntity("stator", StatorBlockEntity::new)
            .validBlocks(TFMGBlocks.STATOR)
            .register();

    public static final BlockEntityEntry<NeonTubeBlockEntity> NEON_TUBE = REGISTRATE
            .blockEntity("neon_tube", NeonTubeBlockEntity::new)
            .validBlocks(TFMGBlocks.NEON_TUBE)
            .renderer(() -> NeonTubeRenderer::new)
            .register();

    public static final BlockEntityEntry<DiagonalCableBlockEntity> DIAGONAL_CABLE_BLOCK = REGISTRATE
            .blockEntity("diagonal_cable_block", DiagonalCableBlockEntity::new)
            .validBlocks(TFMGBlocks.DIAGONAL_CABLE_BLOCK)
            .register();

    public static final BlockEntityEntry<CableTubeBlockEntity> CABLE_TUBE = REGISTRATE
            .blockEntity("cable_tube", CableTubeBlockEntity::new)
            .validBlocks(TFMGBlocks.CABLE_TUBE, TFMGBlocks.ELECTRIC_POST,
                    TFMGBlocks.CONCRETE_ENCASED_CABLE_TUBE, TFMGBlocks.CONCRETE_ENCASED_ELECTRIC_POST)
            .register();

    public static final BlockEntityEntry<RotorBlockEntity> ROTOR = REGISTRATE
            .blockEntity("rotor", RotorBlockEntity::new)
            .visual(() -> RotorVisual::new)
            .validBlocks(TFMGBlocks.ROTOR)
            .renderer(() -> RotorRenderer::new)
            .register();
    public static final BlockEntityEntry<ConcreteHoseBlockEntity> CONCRETE_HOSE = REGISTRATE
            .blockEntity("concrete_hose", ConcreteHoseBlockEntity::new)
            //.instance(() -> ConcreteHoseInstance::new)
            .validBlocks(TFMGBlocks.CONCRETE_HOSE)
            .renderer(() -> ConcreteHoseRenderer::new)
            .register();
    public static final BlockEntityEntry<PumpjackBlockEntity> PUMPJACK_HAMMER = REGISTRATE
            .blockEntity("pumpjack_hammer", PumpjackBlockEntity::new)
            .validBlocks(TFMGBlocks.PUMPJACK_HAMMER)
            .renderer(() -> PumpjackRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = REGISTRATE
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            .validBlocks(TFMGBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpjackBaseBlockEntity> PUMPJACK_BASE = REGISTRATE
            .blockEntity("pumpjack_base", PumpjackBaseBlockEntity::new)
            .validBlocks(TFMGBlocks.PUMPJACK_BASE)
            .register();
    public static final BlockEntityEntry<ConverterBlockEntity> CONVERTER = REGISTRATE
            .blockEntity("converter", ConverterBlockEntity::new)
            .validBlocks(TFMGBlocks.CONVERTER)
            .register();
    public static final BlockEntityEntry<CastingBasinBlockEntity> CASTING_BASIN = REGISTRATE
            .blockEntity("casting_basin", CastingBasinBlockEntity::new)
            .validBlocks(TFMGBlocks.CASTING_BASIN)
            .renderer(() -> CastingBasinRenderer::new)
            .register();

    public static final BlockEntityEntry<SurfaceScannerBlockEntity> SURFACE_SCANNER = REGISTRATE
            .blockEntity("surface_scanner", SurfaceScannerBlockEntity::new)
            .validBlocks(TFMGBlocks.SURFACE_SCANNER)
            .renderer(() -> SurfaceScannerRenderer::new)
            .register();
    public static final BlockEntityEntry<FireboxBlockEntity> FIREBOX = REGISTRATE
            .blockEntity("firebox", FireboxBlockEntity::new)
            .validBlocks(TFMGBlocks.FIREBOX)
            .register();
    public static final BlockEntityEntry<VoltageObserverBlockEntity> VOLTAGE_OBSERVER = REGISTRATE
            .blockEntity("voltage_observer", VoltageObserverBlockEntity::new)
            .validBlocks(TFMGBlocks.VOLTAGE_OBSERVER)
            .register();
    public static final BlockEntityEntry<DistillationControllerBlockEntity> DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillation_tower_controller", DistillationControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER)
            .renderer(() -> DistillationControllerRenderer::new)
            .register();
    public static final BlockEntityEntry<MachineInputBlockEntity> MACHINE_INPUT = REGISTRATE
            .blockEntity("machine_input", MachineInputBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF))
            .validBlocks(TFMGBlocks.MACHINE_INPUT)
            .renderer(() -> HalfShaftRenderer::new)
            .register();
    public static final BlockEntityEntry<LightBulbBlockEntity> LIGHT_BULB = REGISTRATE
            .blockEntity("light_bulb", LightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.LIGHT_BULB)
            .renderer(() -> LightBulbRenderer::new)
            .register();
    public static final BlockEntityEntry<LightBulbBlockEntity> CIRCULAR_LIGHT = REGISTRATE
            .blockEntity("circular_light", LightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.CIRCULAR_LIGHT)
            .renderer(() -> CircularLightRenderer::new)
            .register();
    public static final BlockEntityEntry<LightBulbBlockEntity> MODERN_LIGHT = REGISTRATE
            .blockEntity("modern_light", LightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.MODERN_LIGHT)
            .renderer(() -> ModernLightRenderer::new)
            .register();
    public static final BlockEntityEntry<LightBulbBlockEntity> ALUMINUM_LAMP = REGISTRATE
            .blockEntity("aluminum_lamp", LightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.ALUMINUM_LAMP)
            .renderer(() -> AluminumLampRenderer::new)
            .register();

    public static final BlockEntityEntry<AccumulatorBlockEntity> ACCUMULATOR = REGISTRATE
            .blockEntity("accumulator", AccumulatorBlockEntity::new)
            .validBlocks(TFMGBlocks.ACCUMULATOR)
            .register();

    public static final BlockEntityEntry<ElectricDiodeBlockEntity> DIODE = REGISTRATE
            .blockEntity("electric_diode", ElectricDiodeBlockEntity::new)
            .validBlocks(TFMGBlocks.DIODE, TFMGBlocks.ENCASED_DIODE)
            .register();


    public static final BlockEntityEntry<RegularEngineBlockEntity> REGULAR_ENGINE = REGISTRATE
            .blockEntity("regular_engine", RegularEngineBlockEntity::new)
            .visual(() -> RegularEngineVisual::new, true)
            .renderer(() -> RegularEngineRenderer::new)
            .validBlocks(TFMGBlocks.REGULAR_ENGINE)
            .register();

    public static final BlockEntityEntry<TurbineEngineBlockEntity> TURBINE_ENGINE = REGISTRATE
            .blockEntity("turbine_engine", TurbineEngineBlockEntity::new)
            .visual(() -> RegularEngineVisual::new, true)
            .renderer(() -> EngineRenderer::new)
            .validBlocks(TFMGBlocks.TURBINE_ENGINE)
            .register();

    public static final BlockEntityEntry<LargeEngineBlockEntity> LARGE_ENGINE = REGISTRATE
            .blockEntity("large_engine", LargeEngineBlockEntity::new)
            .renderer(() -> LargeEngineRenderer::new)
            .validBlocks(TFMGBlocks.LARGE_ENGINE, TFMGBlocks.SIMPLE_LARGE_ENGINE)
            .register();

    public static final BlockEntityEntry<RadialEngineBlockEntity> RADIAL_ENGINE = REGISTRATE
            .blockEntity("radial_engine", RadialEngineBlockEntity::new)
            .visual(() -> ShaftVisual::new, true)
            .renderer(() -> RegularEngineRenderer::new)
            .validBlocks(TFMGBlocks.RADIAL_ENGINE)
            .register();

    public static final BlockEntityEntry<PotentiometerBlockEntity> POTENTIOMETER = REGISTRATE
            .blockEntity("potentiometer", PotentiometerBlockEntity::new)
            .validBlocks(TFMGBlocks.POTENTIOMETER, TFMGBlocks.ENCASED_POTENTIOMETER)
            .register();

    public static final BlockEntityEntry<TFMGSlidingDoorBlockEntity> TFMG_SLIDING_DOOR =
            REGISTRATE.blockEntity("tfmg_sliding_door", TFMGSlidingDoorBlockEntity::new)
                    .renderer(() -> TFMGSlidingDoorRenderer::new)
                    .validBlocks(TFMGBlocks.HEAVY_CASING_DOOR, TFMGBlocks.STEEL_CASING_DOOR, TFMGBlocks.HEAVY_PLATED_DOOR, TFMGBlocks.ALUMINUM_DOOR)
                    .register();
    public static final BlockEntityEntry<EngineGearboxBlockEntity> ENGINE_GEARBOX = REGISTRATE
            .blockEntity("engine_gearbox", EngineGearboxBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::shaft)
            .validBlocks(TFMGBlocks.ENGINE_GEARBOX)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<EngineControllerBlockEntity> ENGINE_CONTROLLER = REGISTRATE
            .blockEntity("engine_controller", EngineControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.ENGINE_CONTROLLER)
            .renderer(() -> EngineControllerRenderer::new)
            .register();
    public static final BlockEntityEntry<IndustrialMixerBlockEntity> INDUSTRIAL_MIXER = REGISTRATE
            .blockEntity("industrial_mixer", IndustrialMixerBlockEntity::new)
            .visual(() -> IndustrialMixerVisual::new, true)
            .renderer(() -> IndustrialMixerRenderer::new)
            .validBlocks(TFMGBlocks.INDUSTRIAL_MIXER)
            .register();
    public static final BlockEntityEntry<ElectrodeHolderBlockEntity> ELECTRODE_HOLDER = REGISTRATE
            .blockEntity("electrode_holder", ElectrodeHolderBlockEntity::new)
            .validBlocks(TFMGBlocks.ELECTRODE_HOLDER)
            .renderer(() -> ElectrodeHolderRenderer::new)
            .register();
    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = REGISTRATE
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();

    public static final BlockEntityEntry<TFMGFluidTankBlockEntity> TFMG_FLUID_TANK = REGISTRATE
            .blockEntity("tfmg_fluid_tank", TFMGFluidTankBlockEntity::new)
            .validBlocks(TFMGBlocks.ALUMINUM_FLUID_TANK, TFMGBlocks.CAST_IRON_FLUID_TANK)
            .renderer(() -> TFMGFluidTankRenderer::new)
            .register();


    public static final BlockEntityEntry<VatBlockEntity> CHEMICAL_VAT = REGISTRATE
            .blockEntity("chemical_vat", VatBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_CHEMICAL_VAT, TFMGBlocks.FIREPROOF_CHEMICAL_VAT, TFMGBlocks.CAST_IRON_CHEMICAL_VAT)
            .renderer(() -> VatRenderer::new)
            .register();
    public static final BlockEntityEntry<BlastStoveBlockEntity> BLAST_STOVE = REGISTRATE
            .blockEntity("blast_stove", BlastStoveBlockEntity::new)
            .validBlocks(TFMGBlocks.BLAST_STOVE)
            .register();


    public static final BlockEntityEntry<CreativeGeneratorBlockEntity> CREATIVE_GENERATOR = REGISTRATE
            .blockEntity("creative_generator", CreativeGeneratorBlockEntity::new)
            .validBlocks(TFMGBlocks.CREATIVE_GENERATOR)
            .register();

    public static final BlockEntityEntry<VoltMeterBlockEntity> VOLTMETER = REGISTRATE
            .blockEntity("voltmeter", VoltMeterBlockEntity::new)
            .validBlocks(TFMGBlocks.VOLTMETER)
            .renderer(() -> VoltMeterRenderer::new)
            .register();

    public static final BlockEntityEntry<TrafficLightBlockEntity> TRAFFIC_LIGHT = REGISTRATE
            .blockEntity("traffic_light", TrafficLightBlockEntity::new)
            .validBlocks(TFMGBlocks.TRAFFIC_LIGHT)
            .renderer(() -> TrafficLightRenderer::new)
            .register();
    public static final BlockEntityEntry<TransformerBlockEntity> TRANSFORMER = REGISTRATE
            .blockEntity("transformer", TransformerBlockEntity::new)
            .validBlocks(TFMGBlocks.TRANSFORMER)
            .renderer(() -> TransformerRenderer::new)
            .register();
    public static final BlockEntityEntry<FuseBlockEntity> FUSE_BLOCK = REGISTRATE
            .blockEntity("fuse_block", FuseBlockEntity::new)
          //  .validBlocks(TFMGBlocks.FUSE_BLOCK)
            .renderer(() -> FuseBlockRenderer::new)
            .register();
    public static final BlockEntityEntry<SegmentedDisplayBlockEntity> SEGMENTED_DISPLAY = REGISTRATE
            .blockEntity("segmented_display", SegmentedDisplayBlockEntity::new)
            .validBlocks(TFMGBlocks.SEGMENTED_DISPLAY)
            .renderer(() -> SegmentedDisplayRenderer::new)
            //.onRegister(assignDataBehaviourBE(new SegmentedDisplayTarget()))
            //.onRegister(assignDataBehaviourBE(new SegmentedDisplaySource()))
            .register();

    public static final BlockEntityEntry<SmokestackBlockEntity> SMOKESTACK = REGISTRATE
            .blockEntity("smokestack", SmokestackBlockEntity::new)
            .validBlocks(TFMGBlocks.BRICK_SMOKESTACK, TFMGBlocks.METAL_SMOKESTACK, TFMGBlocks.CONCRETE_SMOKESTACK)
            .register();

    public static final BlockEntityEntry<ExhaustBlockEntity> EXHAUST = REGISTRATE
            .blockEntity("exhaust", ExhaustBlockEntity::new)
            .validBlocks(TFMGBlocks.EXHAUST)
            .register();

    public static final BlockEntityEntry<FlarestackBlockEntity> FLARESTACK = REGISTRATE
            .blockEntity("flarestack", FlarestackBlockEntity::new)
            .validBlocks(TFMGBlocks.FLARESTACK)
            .register();

    public static final BlockEntityEntry<BracketedKineticBlockEntity> TFMG_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_simple_kinetic", BracketedKineticBlockEntity::new)
            .visual(() -> TFMGCogwheelVisual::create, true)
            .validBlocks(TFMGBlocks.STEEL_COGWHEEL, TFMGBlocks.LARGE_STEEL_COGWHEEL, TFMGBlocks.ALUMINUM_COGWHEEL, TFMGBlocks.LARGE_ALUMINUM_COGWHEEL)
            .renderer(() -> TFMGCogwheelRenderer::new)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_STEEL_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_steel_cogwheel", SimpleKineticBlockEntity::new)
            .visual(() -> TFMGEncasedCogVisual::steelSmall, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_STEEL_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_STEEL_COGWHEEL
            )
            .renderer(() -> TFMGEncasedCogRenderer::steelSmall)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .blockEntity("encased_large_steel_cogwheel", SimpleKineticBlockEntity::new)
            .visual(() -> TFMGEncasedCogVisual::steelLarge, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_LARGE_STEEL_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_LARGE_STEEL_COGWHEEL
            )
            .renderer(() -> TFMGEncasedCogRenderer::steellarge)
            .register();
    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_ALUMINUM_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_aluminum_cogwheel", SimpleKineticBlockEntity::new)
            .visual(() -> TFMGEncasedCogVisual::aluminumSmall, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_ALUMINUM_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_ALUMINUM_COGWHEEL

            )
            .renderer(() -> TFMGEncasedCogRenderer::aluminumSmall)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .blockEntity("encased_large_aluminum_cogwheel", SimpleKineticBlockEntity::new)
            .visual(() -> TFMGEncasedCogVisual::aluminumLarge, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_LARGE_ALUMINUM_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_LARGE_ALUMINUM_COGWHEEL
            )
            .renderer(() -> TFMGEncasedCogRenderer::aluminumlarge)
            .register();

    public static final BlockEntityEntry<KineticBlockEntity> TFMG_ENCASED_SHAFT = REGISTRATE
            .blockEntity("tfmg_encased_shaft", KineticBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual::shaft, false)
            .validBlocks(TFMGEncasedBlocks.STEEL_ENCASED_SHAFT, TFMGEncasedBlocks.HEAVY_CASING_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<GearboxBlockEntity> STEEL_GEARBOX = REGISTRATE
            .blockEntity("steel_gearbox", GearboxBlockEntity::new)
            .visual(() -> GearboxVisual::new, false)
            .validBlocks(TFMGBlocks.STEEL_GEARBOX)
            .renderer(() -> GearboxRenderer::new)
            .register();

    public static final BlockEntityEntry<TFMGFlywheelBlockEntity> TFMG_FLYWHEEL = REGISTRATE
            .blockEntity("steel_flywheel", TFMGFlywheelBlockEntity::new)
            .visual(() -> TFMGFlywheelVisual::new, false)
            .validBlocks(
                    TFMGBlocks.STEEL_FLYWHEEL,
                    TFMGBlocks.ALUMINUM_FLYWHEEL,
                    TFMGBlocks.CAST_IRON_FLYWHEEL,
                    TFMGBlocks.LEAD_FLYWHEEL,
                    TFMGBlocks.NICKEL_FLYWHEEL
//
            )
            .renderer(() -> TFMGFlywheelRenderer::new)
            .register();

    public static final BlockEntityEntry<BlastFurnaceOutputBlockEntity> BLAST_FURNACE_OUTPUT = REGISTRATE
            .blockEntity("blast_furnace_output", BlastFurnaceOutputBlockEntity::new)
            .renderer(() -> BlastFurnaceRenderer::new)
            .validBlocks(TFMGBlocks.BLAST_FURNACE_OUTPUT)
            .register();

    public static final BlockEntityEntry<BlastFurnaceHatchBlockEntity> BLAST_FURNACE_HATCH = REGISTRATE
            .blockEntity("blast_furnace_hatch", BlastFurnaceHatchBlockEntity::new)
            .validBlocks(TFMGBlocks.BLAST_FURNACE_HATCH)
            .register();



    public static final BlockEntityEntry<CableHubBlockEntity> CABLE_HUB = REGISTRATE
            .blockEntity("cable_hub", CableHubBlockEntity::new)
            .validBlocks(TFMGBlocks.BRASS_CABLE_HUB,
                    TFMGBlocks.COPPER_CABLE_HUB,
                    TFMGBlocks.STEEL_CABLE_HUB,
                    TFMGBlocks.ALUMINUM_CABLE_HUB,
                    TFMGBlocks.HEAVY_CABLE_HUB,
                    TFMGBlocks.STEEL_CASING_CABLE_HUB
//
            )
            .register();


    public static final BlockEntityEntry<CokeOvenBlockEntity> COKE_OVEN = REGISTRATE
            .blockEntity("coke_oven", CokeOvenBlockEntity::new)
            .renderer(() -> CokeOvenRenderer::new)
            .validBlocks(TFMGBlocks.COKE_OVEN)
            .register();

    public static final BlockEntityEntry<GeneratorBlockEntity> GENERATOR = REGISTRATE
            .blockEntity("generator", GeneratorBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF))
            .validBlocks(TFMGBlocks.GENERATOR)
            .renderer(() -> HalfShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<ElectricMotorBlockEntity> ELECTRIC_MOTOR = REGISTRATE
            .blockEntity("electric_motor", ElectricMotorBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF))
            .validBlocks(TFMGBlocks.ELECTRIC_MOTOR)
            .renderer(() -> HalfShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<AirIntakeBlockEntity> AIR_INTAKE = REGISTRATE
            .blockEntity("air_intake", AirIntakeBlockEntity::new)
            .renderer(() -> AirIntakeRenderer::new)
            .validBlocks(TFMGBlocks.AIR_INTAKE)
            .register();


    public static final BlockEntityEntry<CableConnectorBlockEntity> CABLE_CONNECTOR = REGISTRATE
            .blockEntity("cable_connector", CableConnectorBlockEntity::new)
            .renderer(() -> CableConnectorRenderer::new)
            .validBlocks(TFMGBlocks.CABLE_CONNECTOR, TFMGBlocks.GLASS_CABLE_CONNECTOR)
            .register();

    public static final BlockEntityEntry<WindingMachineBlockEntity> WINDING_MACHINE = REGISTRATE
            .blockEntity("winding_machine", WindingMachineBlockEntity::new)
            .visual(() -> WindingMachineVisual::new,true)
            .validBlocks(TFMGBlocks.WINDING_MACHINE)
            .renderer(() -> WindingMachineRenderer::new)
            .register();


    public static final BlockEntityEntry<TFMGPipeBlockEntity> TFMG_PIPE = REGISTRATE
            .blockEntity("tfmg_pipe", TFMGPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(0),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(0),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(0),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(0),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(0)
            )
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> GLASS_TFMG_PIPE = REGISTRATE
            .blockEntity("glass_tfmg_pipe", StraightPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(2),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(2),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(2),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(2),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(2)
            )
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();


    public static final BlockEntityEntry<FluidPipeBlockEntity> ENCASED_TFMG_PIPE = REGISTRATE
            .blockEntity("encased_tfmg_pipe", FluidPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(1),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(1),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(1),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(1),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(1)
            )
            .register();


    public static final BlockEntityEntry<PumpBlockEntity> TFMG_MECHANICAL_PUMP = REGISTRATE
            .blockEntity("mechanical_pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(AllPartialModels.MECHANICAL_PUMP_COG))
            .validBlocks(
                   TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(3),
                   TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(3),
                   TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(3),
                   TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(3),
                   TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(3)
            )
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> TFMG_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(4),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(4),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(4),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(4),
                    TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(4)
            )
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> TFMG_FLUID_VALVE = REGISTRATE
            .blockEntity("fluid_valve", FluidValveBlockEntity::new)
            .visual(() -> FluidValveVisual::new)
            .validBlocks(
                   // TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(5),
                   // TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(5),
                   // TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(5),
                   // TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(5),
                   // TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(5)
            )
            .renderer(() -> FluidValveRenderer::new)
            .register();

    public static void init() {

    }

}
