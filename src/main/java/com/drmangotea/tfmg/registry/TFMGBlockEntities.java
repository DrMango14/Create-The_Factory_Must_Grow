package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.blocks.concrete.formwork.FormWorkBlockEntity;
import com.drmangotea.tfmg.blocks.concrete.formwork.FormWorkRenderer;
import com.drmangotea.tfmg.blocks.decoration.doors.TFMGSlidingDoorBlockEntity;
import com.drmangotea.tfmg.blocks.decoration.doors.TFMGSlidingDoorRenderer;
import com.drmangotea.tfmg.blocks.deposits.FluidDepositBlockEntity;
import com.drmangotea.tfmg.blocks.deposits.surface_scanner.SurfaceScannerBlockEntity;
import com.drmangotea.tfmg.blocks.deposits.surface_scanner.SurfaceScannerRenderer;
import com.drmangotea.tfmg.blocks.engines.diesel.DieselEngineBlockEntity;
import com.drmangotea.tfmg.blocks.engines.diesel.DieselEngineInstance;
import com.drmangotea.tfmg.blocks.engines.diesel.DieselEngineRenderer;
import com.drmangotea.tfmg.blocks.engines.intake.AirIntakeBlockEntity;
import com.drmangotea.tfmg.blocks.engines.intake.AirIntakeInstance;
import com.drmangotea.tfmg.blocks.engines.intake.AirIntakeRenderer;
import com.drmangotea.tfmg.blocks.engines.small.gasoline.GasolineEngineBackTileEntity;
import com.drmangotea.tfmg.blocks.engines.small.gasoline.GasolineEngineTileEntity;
import com.drmangotea.tfmg.blocks.engines.small.lpg.LPGEngineBackTileEntity;
import com.drmangotea.tfmg.blocks.engines.small.lpg.LPGEngineTileEntity;
import com.drmangotea.tfmg.blocks.engines.small.turbine.TurbineEngineBackTileEntity;
import com.drmangotea.tfmg.blocks.engines.small.turbine.TurbineEngineTileEntity;
import com.drmangotea.tfmg.blocks.machines.exhaust.ExhaustTileEntity;
import com.drmangotea.tfmg.blocks.machines.flarestack.FlarestackBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.BlastFurnaceRenderer;
import com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.MoltenMetalBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_spout.CastingSpoutBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.casting_spout.CastingSpoutRenderer;
import com.drmangotea.tfmg.blocks.machines.metal_processing.coke_oven.CokeOvenBlockEntity;
import com.drmangotea.tfmg.blocks.machines.metal_processing.coke_oven.CokeOvenRenderer;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower.DistillationControllerBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower.DistillationOutputBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryControllerBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryOutputBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseRenderer;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.base.PumpjackBaseBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankInstance;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankRenderer;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank.PumpjackCrankBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderInstance;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderRenderer;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder.PumpjackHammerHolderBlockEntity;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputRenderer;
import com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.drmangotea.tfmg.blocks.pipes.normal.LockablePipeBlockEntity;
import com.drmangotea.tfmg.blocks.tanks.SteelFluidTankRenderer;
import com.drmangotea.tfmg.blocks.tanks.SteelTankBlockEntity;
import com.drmangotea.tfmg.blocks.engines.small.UniversalEngineRenderer;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveInstance;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpCogInstance;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.content.kinetics.base.HorizontalHalfShaftInstance;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;


public class TFMGBlockEntities {



    public static final BlockEntityEntry<FormWorkBlockEntity> FORMWORK = REGISTRATE
            .blockEntity("formwork", FormWorkBlockEntity::new)
            .renderer(() -> FormWorkRenderer::new)
            .validBlocks(TFMGBlocks.FORMWORK_BLOCK)
            .register();
    public static final BlockEntityEntry<FluidDepositBlockEntity> OIL_DEPOSIT = REGISTRATE
            .blockEntity("oil_deposit", FluidDepositBlockEntity::new)
           // .validBlocks(TFMGBlocks.OIL_DEPOSIT)
            .register();


    public static final BlockEntityEntry<LockablePipeBlockEntity> STEEL_PIPE = REGISTRATE
            .blockEntity("steel_pipe", LockablePipeBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_PIPE)
            .register();

    public static final BlockEntityEntry<LockablePipeBlockEntity> ENCASED_STEEL_PIPE = REGISTRATE
            .blockEntity("encased_steel_pipe", LockablePipeBlockEntity::new)
            .validBlocks(TFMGBlocks.COPPER_ENCASED_STEEL_PIPE)
            .register();


    public static final BlockEntityEntry<StraightPipeBlockEntity> GLASS_STEEL_PIPE = REGISTRATE
            .blockEntity("glass_steel_pipe", StraightPipeBlockEntity::new)
            .validBlocks(TFMGBlocks.GLASS_STEEL_PIPE)
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = REGISTRATE
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();


    public static final BlockEntityEntry<TFMGSlidingDoorBlockEntity> TFMG_SLIDING_DOOR =
            REGISTRATE.blockEntity("tfmg_sliding_door", TFMGSlidingDoorBlockEntity::new)
                    .renderer(() -> TFMGSlidingDoorRenderer::new)
                    .validBlocks(TFMGBlocks.HEAVY_CASING_DOOR,TFMGBlocks.STEEL_CASING_DOOR)
                    .register();


    public static final BlockEntityEntry<PumpBlockEntity> TFMG_MECHANICAL_PUMP = REGISTRATE
            .blockEntity("mechanical_pump", PumpBlockEntity::new)
            .instance(() -> PumpCogInstance::new)
            .validBlocks(TFMGBlocks.STEEL_MECHANICAL_PUMP)
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<SmartFluidPipeBlockEntity> TFMG_SMART_FLUID_PIPE = REGISTRATE
            .blockEntity("smart_fluid_pipe", SmartFluidPipeBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_SMART_FLUID_PIPE)
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidValveBlockEntity> TFMG_FLUID_VALVE = REGISTRATE
            .blockEntity("fluid_valve", FluidValveBlockEntity::new)
            .instance(() -> FluidValveInstance::new)
            .validBlocks(TFMGBlocks.STEEL_FLUID_VALVE)
            .renderer(() -> FluidValveRenderer::new)
            .register();

    public static final BlockEntityEntry<SurfaceScannerBlockEntity> SURFACE_SCANNER = REGISTRATE
            .blockEntity("deposit_scanner", SurfaceScannerBlockEntity::new)
            .instance(() -> HorizontalHalfShaftInstance::new)
            .validBlocks(TFMGBlocks.SURFACE_SCANNER)
            .renderer(() -> SurfaceScannerRenderer::new)
            .register();


    public static final BlockEntityEntry<DistilleryOutputBlockEntity> CAST_IRON_DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillery", DistilleryOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.CAST_IRON_DISTILLATION_OUTPUT)
            .register();

    public static final BlockEntityEntry<DistilleryControllerBlockEntity> CAST_IRON_DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillery_controller", DistilleryControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.CAST_IRON_DISTILLATION_CONTROLLER)
            .register();

    public static final BlockEntityEntry<DistillationOutputBlockEntity> STEEL_DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillation_tower_output", DistillationOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
            .register();

    public static final BlockEntityEntry<DistillationControllerBlockEntity> STEEL_DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillation_tower_controller", DistillationControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER)
            .register();

    public static final BlockEntityEntry<PumpjackHammerHolderBlockEntity> PUMPJACK_HAMMER_HOLDER = REGISTRATE
            .blockEntity("pumpjack_hammer_holder", PumpjackHammerHolderBlockEntity::new)
            .instance(() -> PumpjackHammerHolderInstance::new, false)
            .validBlocks(TFMGBlocks.PUMPJACK_HAMMER_HOLDER)
            .renderer(() -> PumpjackHammerHolderRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = REGISTRATE
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            //.instance(() -> PumpjackCrankInstance::new, true)
            .validBlocks(TFMGBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            .register();

    public static final BlockEntityEntry<MachineInputBlockEntity> MACHINE_INPUT = REGISTRATE
            .blockEntity("machine_input", MachineInputBlockEntity::new)
            .instance(() -> HalfShaftInstance::new,true)
            .validBlocks(TFMGBlocks.MACHINE_INPUT)
            .renderer(() -> MachineInputRenderer::new)
            .register();

    public static final BlockEntityEntry<PumpjackBaseBlockEntity> PUMPJACK_BASE = REGISTRATE
            .blockEntity("pumpjack_base", PumpjackBaseBlockEntity::new)
            .validBlocks(TFMGBlocks.PUMPJACK_BASE)
            .renderer(() -> PumpjackBaseRenderer::new)
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

    public static final BlockEntityEntry<ExhaustTileEntity> EXHAUST = REGISTRATE
            .blockEntity("exhaust", ExhaustTileEntity::new)
            .validBlocks(TFMGBlocks.EXHAUST)
            .register();

    public static final BlockEntityEntry<FlarestackBlockEntity> FLARESTACK = REGISTRATE
            .blockEntity("flarestack", FlarestackBlockEntity::new)
            .validBlocks(TFMGBlocks.FLARESTACK)
            .register();




    public static void register() {}
}
