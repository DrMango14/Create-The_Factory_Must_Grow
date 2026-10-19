package com.drmangotea.tfmg.content.electricity.experimental.content.devices.voltage_observer;


import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.base.ResistiveLoadBlockEntity;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.experimental.content.devices.voltage_observer.VoltageObserverBlock.POWERED;


public class VoltageObserverBlockEntity extends ResistiveLoadBlockEntity {

    boolean update = false;

    ObservedElectricBehaviour observedElectricBehaviour;

    public VoltageObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(observedElectricBehaviour = new ObservedElectricBehaviour(this));
    }


    public int getComparatorOutput() {
        return ComparatorUtil.fractionToRedstoneLevel(voltage / 250d);
    }

    @Override
    public double resistance() {
        return 1E9;
    }

    @Override
    public void onUpdated() {
        super.onUpdated();
        update = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (update) {
            level.setBlock(getBlockPos(), getBlockState().setValue(POWERED, voltage != 0), 2);
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            update = false;
        }
        if (observedElectricBehaviour != null) {
            observedElectricBehaviour.setObservedPos(getConnectedPos());
        }
    }

    private BlockPos getConnectedPos() {
        return getBlockPos().relative(getBlockState().getValue(WallMountBlock.FACING));
    }
}
