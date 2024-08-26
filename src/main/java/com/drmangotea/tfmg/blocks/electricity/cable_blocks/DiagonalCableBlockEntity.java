package com.drmangotea.tfmg.blocks.electricity.cable_blocks;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.blocks.electricity.cable_blocks.DiagonalCableBlock.FACING_UP;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class DiagonalCableBlockEntity extends ElectricBlockEntity {
    public DiagonalCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.DIAGONAL_CABLE_BLOCK.get());
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {

        if(getBlockState().getValue(FACING_UP)){

            return direction == Direction.UP||direction == getBlockState().getValue(FACING);


        }else {
            return direction == Direction.DOWN||direction == getBlockState().getValue(FACING);
        }
    }
}
