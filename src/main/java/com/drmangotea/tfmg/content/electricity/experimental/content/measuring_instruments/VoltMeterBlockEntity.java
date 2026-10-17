package com.drmangotea.tfmg.content.electricity.experimental.content.measuring_instruments;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VoltMeterBlockEntity extends ElectricMeasuringBlockEntity{
    public VoltMeterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public double resistance() {
        return ((VoltMeterBlock)getBlockState().getBlock()).voltmeter ? 1E9 : 0.1;
    }
}
