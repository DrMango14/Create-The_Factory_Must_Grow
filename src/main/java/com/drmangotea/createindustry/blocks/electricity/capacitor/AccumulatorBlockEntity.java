package com.drmangotea.createindustry.blocks.electricity.capacitor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AccumulatorBlockEntity extends CapacitorBlockEntity{
    public AccumulatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public int FECapacity() {
        return 100000;
    }
    @Override
    public int transferSpeed() {
        return 333;
    }

}
