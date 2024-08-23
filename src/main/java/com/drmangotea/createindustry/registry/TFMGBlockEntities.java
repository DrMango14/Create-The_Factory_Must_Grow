package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.base.TFMGEncasedBlocks;
import com.drmangotea.createindustry.base.TFMGPipes;
import com.drmangotea.createindustry.base.multiblock.FluidOutputBlockEntity;
import com.drmangotea.createindustry.blocks.HalfShaftRenderer;
import com.drmangotea.createindustry.blocks.cogwheeels.*;
import com.drmangotea.createindustry.blocks.concrete.formwork.FormWorkBlockEntity;
import com.drmangotea.createindustry.blocks.concrete.formwork.FormWorkRenderer;
import com.drmangotea.createindustry.blocks.concrete.formwork.rebar.RebarFormWorkBlockEntity;
import com.drmangotea.createindustry.blocks.decoration.doors.TFMGSlidingDoorBlockEntity;
import com.drmangotea.createindustry.blocks.decoration.doors.TFMGSlidingDoorRenderer;
import com.drmangotea.createindustry.blocks.decoration.kinetics.flywheels.TFMGFlywheelBlockEntity;
import com.drmangotea.createindustry.blocks.decoration.kinetics.flywheels.TFMGFlywheelInstance;
import com.drmangotea.createindustry.blocks.decoration.kinetics.flywheels.TFMGFlywheelRenderer;
import com.drmangotea.createindustry.blocks.deposits.surface_scanner.SurfaceScannerBlockEntity;
import com.drmangotea.createindustry.blocks.deposits.surface_scanner.SurfaceScannerRenderer;
import com.drmangotea.createindustry.blocks.electricity.base.ConverterBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.CableTubeBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.DiagonalCableBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.capacitor.AccumulatorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.generation.creative_generator.VoltageCubeBlock;
import com.drmangotea.createindustry.blocks.electricity.generation.creative_generator.VoltageCubeBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.generation.large_generator.*;
import com.drmangotea.createindustry.blocks.electricity.lights.neon.NeonTubeBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.lights.neon.NeonTubeRenderer;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.energy_meter.EnergyMeterBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.energy_meter.EnergyMeterRenderer;
import com.drmangotea.createindustry.blocks.engines.compact.CompactEngineBlockEntity;
import com.drmangotea.createindustry.blocks.engines.compact.CompactEngineRenderer;
import com.drmangotea.createindustry.blocks.engines.diesel.DieselEngineBlockEntity;
import com.drmangotea.createindustry.blocks.engines.diesel.DieselEngineInstance;
import com.drmangotea.createindustry.blocks.engines.diesel.DieselEngineRenderer;
import com.drmangotea.createindustry.blocks.engines.diesel.engine_expansion.DieselEngineExpansionBlockEntity;
import com.drmangotea.createindustry.blocks.engines.intake.AirIntakeBlockEntity;
import com.drmangotea.createindustry.blocks.engines.intake.AirIntakeInstance;
import com.drmangotea.createindustry.blocks.engines.intake.AirIntakeRenderer;
import com.drmangotea.createindustry.blocks.engines.low_grade_fuel.LowGradeFuelEngineBlockEntity;
import com.drmangotea.createindustry.blocks.engines.low_grade_fuel.LowGradeFuelEngineRenderer;
import com.drmangotea.createindustry.blocks.engines.radial.RadialEngineBlockEntity;
import com.drmangotea.createindustry.blocks.engines.radial.RadialEngineRenderer;
import com.drmangotea.createindustry.blocks.engines.radial.input.RadialEngineInputBlockEntity;
import com.drmangotea.createindustry.blocks.engines.radial.large.LargeRadialEngineBlockEntity;
import com.drmangotea.createindustry.blocks.engines.small.gasoline.GasolineEngineBackTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.gasoline.GasolineEngineTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.lpg.LPGEngineBackTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.lpg.LPGEngineTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.turbine.TurbineEngineBackTileEntity;
import com.drmangotea.createindustry.blocks.engines.small.turbine.TurbineEngineTileEntity;
import com.drmangotea.createindustry.blocks.electricity.base.cables.CableConnectorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.base.cables.CableConnectorRenderer;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.CableHubBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.copycat_cable_block.CopycatCableBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.lights.LightBulbRenderer;
import com.drmangotea.createindustry.blocks.electricity.lights.rgb.RGBLightBulbBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.lights.rgb.RGBLightBulbRenderer;
import com.drmangotea.createindustry.blocks.electricity.resistors.ResistorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.transformer.CoilBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.capacitor.CapacitorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.generation.creative_generator.CreativeGeneratorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.batteries.GalvanicCellBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.generation.generator.GeneratorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.polarizer.PolarizeRenderer;
import com.drmangotea.createindustry.blocks.electricity.polarizer.PolarizerBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.VoltMeterBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.voltmeter.VoltMeterRenderer;
import com.drmangotea.createindustry.blocks.machines.exhaust.ExhaustBlockEntity;
import com.drmangotea.createindustry.blocks.machines.firebox.FireboxBlockEntity;
import com.drmangotea.createindustry.blocks.machines.flarestack.FlarestackBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.BlastFurnaceRenderer;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_furnace.MoltenMetalBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove.BlastStoveBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinRenderer;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_spout.CastingSpoutBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_spout.CastingSpoutRenderer;
import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven.CokeOvenRenderer;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.controller.DistillationControllerBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.output.DistillationOutputBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankRenderer;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.PumpjackRenderer;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputRenderer;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.drmangotea.createindustry.blocks.machines.simple.welding_machine.WeldingMachineBlockEntity;
import com.drmangotea.createindustry.blocks.machines.simple.welding_machine.WeldingMachineInstance;
import com.drmangotea.createindustry.blocks.machines.simple.welding_machine.WeldingMachineRenderer;
import com.drmangotea.createindustry.blocks.pipes.normal.LockablePipeBlockEntity;
import com.drmangotea.createindustry.blocks.tanks.SteelFluidTankRenderer;
import com.drmangotea.createindustry.blocks.tanks.SteelTankBlockEntity;
import com.drmangotea.createindustry.blocks.engines.small.UniversalEngineRenderer;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveInstance;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpCogInstance;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlockEntity;
import com.simibubi.create.content.kinetics.gearbox.GearboxInstance;
import com.simibubi.create.content.kinetics.gearbox.GearboxRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogInstance;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;


public class TFMGBlockEntities {
    
    public static final BlockEntityEntry<FluidOutputBlockEntity> FLUID_OUTPUT = REGISTRATE
            .blockEntity("fluid_output", FluidOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.FLUID_OUTPUT)
            .register();

    public static final BlockEntityEntry<WeldingMachineBlockEntity> WELDING_MACHINE = REGISTRATE
            .blockEntity("welding_machine", WeldingMachineBlockEntity::new)
            .instance(() -> WeldingMachineInstance::new)
           // .validBlocks(TFMGBlocks.WELDING_MACHINE)
            .renderer(() -> WeldingMachineRenderer::new)
            .register();

    public static final BlockEntityEntry<LightBulbBlockEntity> LIGHT_BULB = REGISTRATE
            .blockEntity("light_bulb", LightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.LIGHT_BULB
                    //,TFMGBlocks.INDUSTRIAL_LIGHT
                    )
            .renderer(() -> LightBulbRenderer::new)
            .register();



    public static final BlockEntityEntry<RGBLightBulbBlockEntity> RGB_LIGHT_BULB = REGISTRATE
            .blockEntity("rgb_light_bulb", RGBLightBulbBlockEntity::new)
            .validBlocks(TFMGBlocks.RGB_LIGHT_BULB)
            .renderer(() -> RGBLightBulbRenderer::new)
            .register();

    public static final BlockEntityEntry<VoltageCubeBlockEntity> VOLTAGE_CUBE = REGISTRATE
            .blockEntity("voltage_cube", VoltageCubeBlockEntity::new)
            .validBlocks(TFMGBlocks.VOLTAGE_CUBE)
            .register();
    public static final BlockEntityEntry<CableHubBlockEntity> CABLE_HUB = REGISTRATE
            .blockEntity("cable_hub", CableHubBlockEntity::new)
            .validBlocks(TFMGBlocks.BRASS_CABLE_HUB,
                    TFMGBlocks.COPPER_CABLE_HUB,
                    TFMGBlocks.STEEL_CABLE_HUB,
                    TFMGBlocks.ALUMINUM_CABLE_HUB,
                    TFMGBlocks.HEAVY_CABLE_HUB,
                    TFMGBlocks.STEEL_CASING_CABLE_HUB)
            .register();

    public static final BlockEntityEntry<CableTubeBlockEntity> CABLE_TUBE = REGISTRATE
            .blockEntity("cable_tube", CableTubeBlockEntity::new)
            .validBlocks(TFMGBlocks.CABLE_TUBE)
            .register();

    public static final BlockEntityEntry<NeonTubeBlockEntity> NEON_TUBE = REGISTRATE
            .blockEntity("neon_tube", NeonTubeBlockEntity::new)
            .validBlocks(TFMGBlocks.NEON_TUBE)
            .renderer(() -> NeonTubeRenderer::new)
            .register();

    public static final BlockEntityEntry<DiagonalCableBlockEntity> DIAGONAL_CABLE_BLOCK = REGISTRATE
            .blockEntity("diagonal_cable_block", DiagonalCableBlockEntity::new)
            .validBlocks(TFMGBlocks.DIAGONL_CABLE_BLOCK)
            .register();
    public static final BlockEntityEntry<FireboxBlockEntity> FIREBOX = REGISTRATE
            .blockEntity("blaze_heater", FireboxBlockEntity::new)
            .validBlocks(TFMGBlocks.FIREBOX)
            .register();
    public static final BlockEntityEntry<CapacitorBlockEntity>  CAPACITOR = REGISTRATE
            .blockEntity("capacitor", CapacitorBlockEntity::new)
            .validBlocks(TFMGBlocks.CAPACITOR)
            .register();

    public static final BlockEntityEntry<ConverterBlockEntity>  CONVERTER = REGISTRATE
            .blockEntity("converter", ConverterBlockEntity::new)
            .validBlocks(TFMGBlocks.CONVERTER)
            .register();


    public static final BlockEntityEntry<AccumulatorBlockEntity> ACCUMULATOR = REGISTRATE
            .blockEntity("accumulator", AccumulatorBlockEntity::new)
            .validBlocks(TFMGBlocks.ACCUMULATOR)
            .register();

    public static final BlockEntityEntry<GalvanicCellBlockEntity> GALVANIC_CELL = REGISTRATE
            .blockEntity("galvanic_cell", GalvanicCellBlockEntity::new)
            .validBlocks(TFMGBlocks.GALVANIC_CELL)
            .register();
    public static final BlockEntityEntry<PolarizerBlockEntity> POLARIZER = REGISTRATE
            .blockEntity("polarizer", PolarizerBlockEntity::new)
            .renderer(() -> PolarizeRenderer::new)
            .validBlocks(TFMGBlocks.POLARIZER)
            .register();
    public static final BlockEntityEntry<VoltMeterBlockEntity> VOLTMETER = REGISTRATE
            .blockEntity("voltmeter", VoltMeterBlockEntity::new)
            .validBlocks(TFMGBlocks.VOLTMETER)
            .renderer(() -> VoltMeterRenderer::new)
            .register();

    public static final BlockEntityEntry<EnergyMeterBlockEntity> ENERGY_METER = REGISTRATE
            .blockEntity("energy_meter", EnergyMeterBlockEntity::new)
            .validBlocks(TFMGBlocks.ENERGY_METER)
            .renderer(() -> EnergyMeterRenderer::new)
            .register();
    public static final BlockEntityEntry<ResistorBlockEntity> RESISTOR = REGISTRATE
            .blockEntity("resistor", ResistorBlockEntity::new)
            .validBlocks(TFMGBlocks.RESISTOR)
            .register();

    public static final BlockEntityEntry<CoilBlockEntity> COIL = REGISTRATE
            .blockEntity("coil", CoilBlockEntity::new)
            .validBlocks(TFMGBlocks.COPPER_COIL)
            .register();

    public static final BlockEntityEntry<ElectricMotorBlockEntity> ELECTRIC_MOTOR = REGISTRATE
            .blockEntity("electric_motor", ElectricMotorBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(TFMGBlocks.ELECTRIC_MOTOR)
            .renderer(() -> HalfShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<StatorBlockEntity>  STATOR = REGISTRATE
            .blockEntity("stator", StatorBlockEntity::new)
            .validBlocks(TFMGBlocks.STATOR)
            .renderer(() -> StatorRenderer::new)
            .register();

    public static final BlockEntityEntry<RotorBlockEntity> ROTOR = REGISTRATE
            .blockEntity("rotor", RotorBlockEntity::new)
            .instance(() -> RotorInstance::new, false)
            .validBlocks(TFMGBlocks.ROTOR)
            .renderer(() -> RotorRenderer::new)
            .register();
    public static final BlockEntityEntry<GeneratorBlockEntity> GENERATOR = REGISTRATE
            .blockEntity("generator", GeneratorBlockEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(TFMGBlocks.GENERATOR)
            .renderer(() -> HalfShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<CableConnectorBlockEntity> CABLE_CONNECTOR = REGISTRATE
            .blockEntity("cable_connector", CableConnectorBlockEntity::new)
            .renderer(() -> CableConnectorRenderer::new)
            .validBlocks(TFMGBlocks.CABLE_CONNECTOR)
            .register();
    public static final BlockEntityEntry<CreativeGeneratorBlockEntity> CREATIVE_GENERATOR = REGISTRATE
            .blockEntity("creative_generator", CreativeGeneratorBlockEntity::new)
            .validBlocks(TFMGBlocks.CREATIVE_GENERATOR)
            .register();

    public static final BlockEntityEntry<FormWorkBlockEntity> FORMWORK = REGISTRATE
            .blockEntity("formwork", FormWorkBlockEntity::new)
            .renderer(() -> FormWorkRenderer::new)
            .validBlocks(TFMGBlocks.FORMWORK_BLOCK)
            .register();

    public static final BlockEntityEntry<RebarFormWorkBlockEntity> REBAR_FORMWORK = REGISTRATE
            .blockEntity("rebar_formwork", RebarFormWorkBlockEntity::new)
            .renderer(() -> FormWorkRenderer::new)
            .validBlocks(TFMGBlocks.REBAR_FORMWORK_BLOCK)
            .register();



    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = REGISTRATE
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();


    public static final BlockEntityEntry<TFMGSlidingDoorBlockEntity> TFMG_SLIDING_DOOR =
            REGISTRATE.blockEntity("createindustry_sliding_door", TFMGSlidingDoorBlockEntity::new)
                    .renderer(() -> TFMGSlidingDoorRenderer::new)
                    .validBlocks(TFMGBlocks.HEAVY_CASING_DOOR,TFMGBlocks.STEEL_CASING_DOOR)
                    .register();




    public static final BlockEntityEntry<SurfaceScannerBlockEntity> SURFACE_SCANNER = REGISTRATE
            .blockEntity("deposit_scanner", SurfaceScannerBlockEntity::new)
            .instance(() -> HorizontalHalfShaftInstance::new)
            .validBlocks(TFMGBlocks.SURFACE_SCANNER)
            .renderer(() -> SurfaceScannerRenderer::new)
            .register();




    public static final BlockEntityEntry<DistillationOutputBlockEntity> STEEL_DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillation_tower_output", DistillationOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
            .register();

    public static final BlockEntityEntry<DistillationControllerBlockEntity> STEEL_DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillation_tower_controller", DistillationControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER)
            .register();



    public static final BlockEntityEntry<MachineInputBlockEntity> MACHINE_INPUT = REGISTRATE
            .blockEntity("machine_input", MachineInputBlockEntity::new)
            .instance(() -> HalfShaftInstance::new,true)
            .validBlocks(TFMGBlocks.MACHINE_INPUT)
            .renderer(() -> MachineInputRenderer::new)
            .register();
    
    public static final BlockEntityEntry<BlastStoveBlockEntity> BLAST_STOVE = REGISTRATE
            .blockEntity("blast_stove", BlastStoveBlockEntity::new)
            .validBlocks(TFMGBlocks.BLAST_STOVE)
            .register();

    public static final BlockEntityEntry<BlastFurnaceOutputBlockEntity> BLAST_FURNACE_OUTPUT = REGISTRATE
            .blockEntity("blast_furnace_output", BlastFurnaceOutputBlockEntity::new)
            .renderer(() -> BlastFurnaceRenderer::new)
            .validBlocks(TFMGBlocks.BLAST_FURNACE_OUTPUT)
            .register();

    public static final BlockEntityEntry<CokeOvenBlockEntity> COKE_OVEN = REGISTRATE
            .blockEntity("coke_oven", CokeOvenBlockEntity::new)
            .renderer(() -> CokeOvenRenderer::new)
            .validBlocks(TFMGBlocks.COKE_OVEN)
            .register();

    public static final BlockEntityEntry<MoltenMetalBlockEntity> MOLTEN_METAL = REGISTRATE
            .blockEntity("molten_metal", MoltenMetalBlockEntity::new)
            .validBlocks(TFMGBlocks.MOLTEN_METAL)
            .register();

    public static final BlockEntityEntry<CastingBasinBlockEntity> CASTING_BASIN = REGISTRATE
            .blockEntity("casting_basin", CastingBasinBlockEntity::new)
            .renderer(()-> CastingBasinRenderer::new)
            .validBlocks(TFMGBlocks.CASTING_BASIN)
            .register();

    public static final BlockEntityEntry<CastingSpoutBlockEntity> CASTING_SPOUT = REGISTRATE
            .blockEntity("casting_spout", CastingSpoutBlockEntity::new)
            .renderer(()->CastingSpoutRenderer::new)
            .validBlocks(TFMGBlocks.CASTING_SPOUT)
            .register();

    public static final BlockEntityEntry<AirIntakeBlockEntity> AIR_INTAKE = REGISTRATE
            .blockEntity("air_intake", AirIntakeBlockEntity::new)
            .instance(() -> AirIntakeInstance::new, true)
            .renderer(()-> AirIntakeRenderer::new)
            .validBlocks(TFMGBlocks.AIR_INTAKE)
            .register();
    public static final BlockEntityEntry<DieselEngineBlockEntity> DIESEL_ENGINE = REGISTRATE
            .blockEntity("diesel_engine", DieselEngineBlockEntity::new)
            .instance(() -> DieselEngineInstance::new, false)
            .validBlocks(TFMGBlocks.DIESEL_ENGINE)
            .renderer(() -> DieselEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<DieselEngineExpansionBlockEntity> DIESEL_ENGINE_EXPANSION = REGISTRATE
            .blockEntity("diesel_engine_expansion", DieselEngineExpansionBlockEntity::new)
            .validBlocks(TFMGBlocks.DIESEL_ENGINE_EXPANSION)
            .register();

    public static final BlockEntityEntry<GasolineEngineTileEntity> GASOLINE_ENGINE = REGISTRATE
            .blockEntity("gasoline_engine", GasolineEngineTileEntity::new)
            .instance(() -> HalfShaftInstance::new, false)
            .validBlocks(TFMGBlocks.GASOLINE_ENGINE)
            .renderer(() -> UniversalEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<GasolineEngineBackTileEntity> GASOLINE_ENGINE_BACK = REGISTRATE
            .blockEntity("gasoline_engine_back", GasolineEngineBackTileEntity::new)
            .validBlocks(TFMGBlocks.GASOLINE_ENGINE_BACK)
            .register();

    public static final BlockEntityEntry<LPGEngineTileEntity> LPG_ENGINE = REGISTRATE
            .blockEntity("lpg_engine", LPGEngineTileEntity::new)
            .instance(() -> HalfShaftInstance::new, false)
            .validBlocks(TFMGBlocks.LPG_ENGINE)
            .renderer(() -> UniversalEngineRenderer::new)
            .register();
    public static final BlockEntityEntry<LPGEngineBackTileEntity> LPG_ENGINE_BACK = REGISTRATE
            .blockEntity("lpg_engine_back", LPGEngineBackTileEntity::new)
            .validBlocks(TFMGBlocks.LPG_ENGINE_BACK)
            .register();

    public static final BlockEntityEntry<TurbineEngineTileEntity> TURBINE_ENGINE = REGISTRATE
            .blockEntity("turbine_engine", TurbineEngineTileEntity::new)
            .instance(() -> HalfShaftInstance::new, false)
            .validBlocks(TFMGBlocks.TURBINE_ENGINE)
            .renderer(() -> UniversalEngineRenderer::new)
            .register();
    public static final BlockEntityEntry<TurbineEngineBackTileEntity> TURBINE_ENGINE_BACK = REGISTRATE
            .blockEntity("turbine_engine_back", TurbineEngineBackTileEntity::new)
            .validBlocks(TFMGBlocks.TURBINE_ENGINE_BACK)
            .register();

    public static final BlockEntityEntry<ExhaustBlockEntity> EXHAUST = REGISTRATE
            .blockEntity("exhaust", ExhaustBlockEntity::new)
            .validBlocks(TFMGBlocks.EXHAUST)
            .register();

    public static final BlockEntityEntry<FlarestackBlockEntity> FLARESTACK = REGISTRATE
            .blockEntity("flarestack", FlarestackBlockEntity::new)
            .validBlocks(TFMGBlocks.FLARESTACK)
            .register();



    public static final BlockEntityEntry<LockablePipeBlockEntity> LOCKABLE_PIPE = REGISTRATE
            .blockEntity("lockable_pipe", LockablePipeBlockEntity::new)
            .validBlocks(TFMGPipes.STEEL_PIPE, TFMGPipes.ALUMINUM_PIPE,TFMGPipes.PLASTIC_PIPE)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> TFMG_PIPE = REGISTRATE
            .blockEntity("createindustry_pipe", FluidPipeBlockEntity::new)
            .validBlocks(TFMGPipes.CAST_IRON_PIPE,TFMGPipes.BRASS_PIPE)
            .register();

    public static final BlockEntityEntry<LockablePipeBlockEntity> ENCASED_LOCKABLE_PIPE = REGISTRATE
            .blockEntity("encased_lockable_pipe", LockablePipeBlockEntity::new)
            .validBlocks(TFMGPipes.COPPER_ENCASED_STEEL_PIPE,
                         TFMGPipes.COPPER_ENCASED_ALUMINUM_PIPE,
                         TFMGPipes.COPPER_ENCASED_PLASTIC_PIPE
            )
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> ENCASED_TFMG_PIPE = REGISTRATE
            .blockEntity("encased_createindustry_pipe", FluidPipeBlockEntity::new)
            .validBlocks(TFMGPipes.COPPER_ENCASED_CAST_IRON_PIPE,TFMGPipes.COPPER_ENCASED_BRASS_PIPE)
            .register();


    public static final BlockEntityEntry<CopycatCableBlockEntity> COPYCAT_CABLE =
            REGISTRATE.blockEntity("copycat_cable", CopycatCableBlockEntity::new)
                    .validBlocks(TFMGBlocks.COPYCAT_CABLE_BLOCK)
                    .register();

    public static final BlockEntityEntry<PumpBlockEntity> TFMG_MECHANICAL_PUMP = REGISTRATE
            .blockEntity("mechanical_pump", PumpBlockEntity::new)
            .instance(() -> PumpCogInstance::new)
            .validBlocks(
                    TFMGPipes.STEEL_MECHANICAL_PUMP,
                    TFMGPipes.CAST_IRON_MECHANICAL_PUMP,
                    TFMGPipes.BRASS_MECHANICAL_PUMP,
                    TFMGPipes.PLASTIC_MECHANICAL_PUMP,
                    TFMGPipes.ALUMINUM_MECHANICAL_PUMP
            )
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> TFMG_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.STEEL_SMART_FLUID_PIPE,
                    TFMGPipes.CAST_IRON_SMART_FLUID_PIPE,
                    TFMGPipes.BRASS_SMART_FLUID_PIPE,
                    TFMGPipes.PLASTIC_SMART_FLUID_PIPE,
                    TFMGPipes.ALUMINUM_SMART_FLUID_PIPE
            )
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> TFMG_FLUID_VALVE = REGISTRATE
            .blockEntity("fluid_valve", FluidValveBlockEntity::new)
            .instance(() -> FluidValveInstance::new)
            .validBlocks(
                    TFMGPipes.STEEL_FLUID_VALVE,
                    TFMGPipes.CAST_IRON_FLUID_VALVE,
                    TFMGPipes.BRASS_FLUID_VALVE,
                    TFMGPipes.PLASTIC_FLUID_VALVE,
                    TFMGPipes.ALUMINUM_FLUID_VALVE
            )
            .renderer(() -> FluidValveRenderer::new)
            .register();


    public static final BlockEntityEntry<StraightPipeBlockEntity> GLASS_TFMG_PIPE = REGISTRATE
            .blockEntity("glass_createindustry_pipe", StraightPipeBlockEntity::new)
            .validBlocks(
                    TFMGPipes.GLASS_STEEL_PIPE,
                    TFMGPipes.GLASS_CAST_IRON_PIPE,
                    TFMGPipes.GLASS_ALUMINUM_PIPE,
                    TFMGPipes.GLASS_PLASTIC_PIPE,
                    TFMGPipes.GLASS_BRASS_PIPE

            )
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<TFMGFlywheelBlockEntity> STEEL_FLYWHEEL = REGISTRATE
            .blockEntity("steel_flywheel", TFMGFlywheelBlockEntity::new)
            .instance(() -> TFMGFlywheelInstance::new, false)
            .validBlocks(
                    TFMGBlocks.STEEL_FLYWHEEL,
                    TFMGBlocks.ALUMINUM_FLYWHEEL,
                    TFMGBlocks.CAST_IRON_FLYWHEEL,
                    TFMGBlocks.LEAD_FLYWHEEL,
                    TFMGBlocks.NICKEL_FLYWHEEL

            )
            .renderer(() -> TFMGFlywheelRenderer::new)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> TFMG_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_simple_kinetic", SimpleKineticBlockEntity::new)
            .instance(() -> TFMGCogwheelInstance::new, false)
            .validBlocks(TFMGBlocks.STEEL_COGWHEEL,TFMGBlocks.LARGE_STEEL_COGWHEEL,TFMGBlocks.ALUMINUM_COGWHEEL,TFMGBlocks.LARGE_ALUMINUM_COGWHEEL)
            .renderer(() -> TFMGCogwheelRenderer::new)
            .register();

    public static final BlockEntityEntry<GearboxBlockEntity> STEEL_GEARBOX = REGISTRATE
            .blockEntity("steel_gearbox", GearboxBlockEntity::new)
            .instance(() -> GearboxInstance::new, false)
            .validBlocks(TFMGBlocks.STEEL_GEARBOX)
            .renderer(() -> GearboxRenderer::new)
            .register();


    public static final BlockEntityEntry<SimpleKineticBlockEntity> TFMG_ENCASED_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedCogInstance::small, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_COGWHEEL

            )
            .renderer(() -> EncasedCogRenderer::small)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> TFMG_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_large_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedCogInstance::large, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_LARGE_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_LARGE_COGWHEEL
            )

            .renderer(() -> EncasedCogRenderer::large)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_STEEL_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_steel_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedSteelCogInstance::small, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_STEEL_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_STEEL_COGWHEEL

            )
            .renderer(() -> EncasedSteelCogRenderer::small)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .blockEntity("encased_large_steel_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedSteelCogInstance::large, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_LARGE_STEEL_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_LARGE_STEEL_COGWHEEL
            )

            .renderer(() -> EncasedSteelCogRenderer::large)
            .register();
    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_ALUMINUM_COGWHEEL = REGISTRATE
            .blockEntity("tfmg_encased_aluminum_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedAluminumCogInstance::small, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_ALUMINUM_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_ALUMINUM_COGWHEEL

            )
            .renderer(() -> EncasedAluminumCogRenderer::small)
            .register();

    public static final BlockEntityEntry<SimpleKineticBlockEntity> ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .blockEntity("encased_large_aluminum_cogwheel", SimpleKineticBlockEntity::new)
            .instance(() -> EncasedAluminumCogInstance::large, false)
            .validBlocks(
                    TFMGEncasedBlocks.STEEL_ENCASED_LARGE_ALUMINUM_COGWHEEL,
                    TFMGEncasedBlocks.HEAVY_CASING_ENCASED_LARGE_ALUMINUM_COGWHEEL
            )

            .renderer(() -> EncasedAluminumCogRenderer::large)
            .register();

    public static final BlockEntityEntry<KineticBlockEntity> TFMG_ENCASED_SHAFT = REGISTRATE
            .blockEntity("tfmg_encased_shaft", KineticBlockEntity::new)
            .instance(() -> ShaftInstance::new, false)
            .validBlocks(TFMGEncasedBlocks.STEEL_ENCASED_SHAFT, TFMGEncasedBlocks.HEAVY_CASING_ENCASED_SHAFT)
            .renderer(() -> ShaftRenderer::new)
            .register();


    public static final BlockEntityEntry<RadialEngineBlockEntity> RADIAL_ENGINE = REGISTRATE
            .blockEntity("radial_engine", RadialEngineBlockEntity::new)
            .instance(() -> ShaftInstance::new, false)
            .validBlocks(TFMGBlocks.RADIAL_ENGINE)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<LargeRadialEngineBlockEntity> LARGE_RADIAL_ENGINE = REGISTRATE
            .blockEntity("large_radial_engine", LargeRadialEngineBlockEntity::new)
            .instance(() -> ShaftInstance::new, false)
            .validBlocks(TFMGBlocks.LARGE_RADIAL_ENGINE)
            .renderer(() -> ShaftRenderer::new)
            .register();


    public static final BlockEntityEntry<RadialEngineInputBlockEntity> RADIAL_ENGINE_INPUT = REGISTRATE
            .blockEntity("radial_engine_input", RadialEngineInputBlockEntity::new)
            .validBlocks(TFMGBlocks.RADIAL_ENGINE_INPUT)
            .register();
    public static final BlockEntityEntry<CompactEngineBlockEntity> COMPACT_ENGINE = REGISTRATE
            .blockEntity("compact_engine", CompactEngineBlockEntity::new)
            .instance(() -> HalfShaftInstance::new, false)
            .validBlocks(TFMGBlocks.COMPACT_ENGINE)
            .renderer(() -> CompactEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<LowGradeFuelEngineBlockEntity> LOW_GRADE_FUEL_ENGINE = REGISTRATE
            .blockEntity("low_grade_fuel_engine", LowGradeFuelEngineBlockEntity::new)
            .instance(() -> HalfShaftInstance::new, false)
            .validBlocks(TFMGBlocks.LOW_GRADE_FUEL_ENGINE)
            .renderer(() -> LowGradeFuelEngineRenderer::new)
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


    public static void register() {}
}
