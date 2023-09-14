package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.concrete.formwork.FormWorkBlockEntity;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkRenderer;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlockEntity;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorRenderer;
import com.drmangotea.tfmg.content.deposits.FluidDepositBlockEntity;
import com.drmangotea.tfmg.content.deposits.surface_scanner.SurfaceScannerRenderer;
import com.drmangotea.tfmg.content.deposits.surface_scanner.SurfaceScannerTileEntity;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillery.DistilleryControllerBlockEntity;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.distillery.DistilleryOutputBlockEntity;
import com.drmangotea.tfmg.content.machines.pipes.normal.LockablePipeBlockEntity;
import com.drmangotea.tfmg.content.machines.tanks.SteelFluidTankRenderer;
import com.drmangotea.tfmg.content.machines.tanks.SteelTankBlockEntity;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlockEntity;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveInstance;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpCogInstance;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
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

    public static final BlockEntityEntry<SurfaceScannerTileEntity> SURFACE_SCANNER = REGISTRATE
            .blockEntity("deposit_scanner", SurfaceScannerTileEntity::new)
            .instance(() -> HorizontalHalfShaftInstance::new)
            .validBlocks(TFMGBlocks.SURFACE_SCANNER)
            .renderer(() -> SurfaceScannerRenderer::new)
            .register();


    public static final BlockEntityEntry<DistilleryOutputBlockEntity> CAST_IRON_DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distiller", DistilleryOutputBlockEntity::new)
            .validBlocks(TFMGBlocks.CAST_IRON_DISTILLATION_OUTPUT)
            .register();

    public static final BlockEntityEntry<DistilleryControllerBlockEntity> CAST_IRON_DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distiller_controller", DistilleryControllerBlockEntity::new)
            .validBlocks(TFMGBlocks.CAST_IRON_DISTILLATION_CONTROLLER)
            .register();


    public static void register() {}
}
