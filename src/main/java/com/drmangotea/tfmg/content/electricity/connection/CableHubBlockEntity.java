package com.drmangotea.tfmg.content.electricity.connection;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CableHubBlockEntity extends ElectricBlockEntity {
    public CableHubBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getMaxCurrent() {
        return ((CableHubBlock)getBlockState().getBlock()).maxCurrent;
    }

    @Override
    public int getMaxVoltage() {
        return 5000;
    }

    @Override
    public boolean isCable() {
        return true;
    }
}
