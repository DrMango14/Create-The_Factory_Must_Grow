package com.drmangotea.tfmg.content.electricity.utilities.voltage_observer;


import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.utilities.voltage_observer.VoltageObserverBlock.POWERED;


public class VoltageObserverBlockEntity extends ElectricBlockEntity {

    boolean update = false;

    ObservedElectricBehaviour observedElectricBehaviour;

    public VoltageObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(observedElectricBehaviour = new ObservedElectricBehaviour(this));
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
        if (observedElectricBehaviour != null) {
            observedElectricBehaviour.setObservedPos(getConnectedPos());
        }
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(WallMountBlock.FACING);
    }

    private BlockPos getConnectedPos() {
        return getBlockPos().relative(getBlockState().getValue(WallMountBlock.FACING));
    }
}
