package com.drmangotea.tfmg.content.decoration.tanks.steel;

import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankBlock;
import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SteelTankBlock extends TFMGFluidTankBlock<SteelTankBlockEntity> {
    public static SteelTankBlock regular(Properties p) { return new SteelTankBlock(p); }
    protected SteelTankBlock(Properties p) { super(p); }
	
    public static boolean isTank(BlockState state) { return state.getBlock() instanceof SteelTankBlock; }

    @Override
    public Class<SteelTankBlockEntity> getBlockEntityClass() {
        return SteelTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SteelTankBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_FLUID_TANK.get();
    }

    public static boolean updateTowerState(Level pLevel, BlockPos tankPos, boolean assemble, boolean simulate) {
        BlockState tankState = pLevel.getBlockState(tankPos);

        if (!(tankState.getBlock() instanceof SteelTankBlock tank))
            return false;

        SteelTankBlockEntity tankBE = tank.getBlockEntity(pLevel, tankPos);
        if (tankBE == null)
            return false;

        if (tankBE.getControllerBE() == null)
            return false;

        if (assemble && tankBE.getControllerBE().isDistillationTower)
            return false;

        if (!simulate) {
            tankBE.getControllerBE().updateBoilerState();
            tankBE.getControllerBE().isDistillationTower = assemble;
            ((FluidTankBlockEntityAccessor)tankBE).tfmg$refreshCapability();


            tankBE.updateBoilerState();
            tankBE.isDistillationTower = assemble;
            ((FluidTankBlockEntityAccessor)tankBE).tfmg$refreshCapability();
            tankBE.sendData();
            tankBE.getControllerBE().sendData();
        }
        return true;
    }
}