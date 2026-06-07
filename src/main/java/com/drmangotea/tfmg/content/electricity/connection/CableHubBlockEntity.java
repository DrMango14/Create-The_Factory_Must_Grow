package com.drmangotea.tfmg.content.electricity.connection;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class CableHubBlockEntity extends ElectricBlockEntity {
    public CableHubBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public float resistance() {

        if (getData().energyTaken == 0 && getData().energyGiven != 0) {

            return (float) Math.pow(getData().getVoltage(), 2) / (this.getData().energyGiven*11);
        }
        return super.resistance();
        //if(this.getData().energyGiven !=0) {
        //    return (float) getData().getVoltage() / ((float) (this.getData().energyGiven * 20) / getData().getVoltage());
        //} else return 0;

    }

    @Override
    public void onPlaced() {
        doActionNextTick(i -> checkForFEOutputs(Arrays.stream(Direction.values()).toList()));
        super.onPlaced();
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
