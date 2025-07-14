package com.drmangotea.tfmg.content.electricity.connection.diagonal;



import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlock.FACING_UP;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class DiagonalCableBlockEntity extends ElectricBlockEntity {
    public DiagonalCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
