package com.drmangotea.tfmg.content.electricity.utilities.voltage_observer;


import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.VoltageObserverBlock.POWERED;


public class VoltageObserverBlockEntity extends ElectricBlockEntity {

    boolean update = false;

    public VoltageObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        update = true;
    }

    public int getComparatorOutput() {
        return ComparatorUtil.fractionToRedstoneLevel((double) getData().getVoltage() /250);
    }

    @Override
    public void tick() {
        super.tick();
        if(update){
            level.setBlock(getBlockPos(),getBlockState().setValue(POWERED,getData().getVoltage() != 0),2);
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            update = false;
        }
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(WallMountBlock.FACING);
    }
}
