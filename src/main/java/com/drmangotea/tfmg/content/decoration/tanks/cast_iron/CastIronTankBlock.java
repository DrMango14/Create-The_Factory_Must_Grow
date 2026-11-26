package com.drmangotea.tfmg.content.decoration.tanks.cast_iron;

import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CastIronTankBlock extends TFMGFluidTankBlock<TFMGFluidTankBlockEntity> {
    public static CastIronTankBlock regular(Properties p) { return new CastIronTankBlock(p); }
    protected CastIronTankBlock(Properties p) { super(p); }

    public static boolean isTank(BlockState state) { return state.getBlock() instanceof CastIronTankBlock; }

    @Override
    public Class<TFMGFluidTankBlockEntity> getBlockEntityClass() {
        return TFMGFluidTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TFMGFluidTankBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TFMG_FLUID_TANK.get();
    }
}