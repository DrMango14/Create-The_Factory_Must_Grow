package com.drmangotea.tfmg.blocks.electricity.batteries;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class GalvanicCellBlockEntity extends BatteryBlockEntity {

    public static final int GALVANIC_CELL_CAPACITY = 75000;



    public GalvanicCellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public boolean hasElectricitySlot(Direction direction) {

        return direction == getBlockState().getValue(FACING);
    }


    @Override
    public int FECapacity() {
        return GALVANIC_CELL_CAPACITY;
    }
}