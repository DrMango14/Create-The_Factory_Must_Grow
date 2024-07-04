package com.drmangotea.createindustry.blocks.electricity.batteries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class GalvanicCellBlockEntity extends BatteryBlockEntity {



    public GalvanicCellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public boolean hasElectricitySlot(Direction direction) {

        return direction == getBlockState().getValue(FACING);
    }


    @Override
    public int FECapacity() {
        return 200000;
    }
}