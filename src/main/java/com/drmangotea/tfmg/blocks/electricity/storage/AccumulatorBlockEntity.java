package com.drmangotea.tfmg.blocks.electricity.storage;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AccumulatorBlockEntity extends CapacitorBlockEntity{
    public AccumulatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.ACCUMULATOR.get());
    }

    @Override
    public int FETransferSpeed() {
        return 500;
    }


    @Override
    public int FECapacity() {
        return 250000;
    }
}
