package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.concrete.formwork.FormWorkBlockEntity;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkRenderer;
import com.drmangotea.tfmg.content.deposits.FluidDepositTileEntity;
import com.drmangotea.tfmg.content.machines.pipes.normal.LockablePipeBlockEntity;
import com.drmangotea.tfmg.content.machines.tanks.SteelFluidTankRenderer;
import com.drmangotea.tfmg.content.machines.tanks.SteelTankBlockEntity;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;


public class TFMGBlockEntities {



    public static final BlockEntityEntry<FormWorkBlockEntity> FORMWORK = REGISTRATE
            .blockEntity("formwork", FormWorkBlockEntity::new)
            .renderer(() -> FormWorkRenderer::new)
            .validBlocks(TFMGBlocks.FORMWORK_BLOCK)
            .register();
    public static final BlockEntityEntry<FluidDepositTileEntity> OIL_DEPOSIT = REGISTRATE
            .blockEntity("oil_deposit", FluidDepositTileEntity::new)
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





    public static void register() {}
}
