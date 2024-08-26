package com.drmangotea.tfmg.blocks.electricity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class VoltageAlteringBlockEntity extends ElectricBlockEntity {
    public VoltageAlteringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getOutputVoltage();

}
