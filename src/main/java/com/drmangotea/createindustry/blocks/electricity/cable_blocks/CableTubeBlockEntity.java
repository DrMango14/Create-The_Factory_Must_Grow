package com.drmangotea.createindustry.blocks.electricity.cable_blocks;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import static net.minecraft.world.level.block.RotatedPillarBlock.AXIS;

public class CableTubeBlockEntity extends ElectricBlockEntity  {
    public CableTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float maxVoltage() {
        return 6000;
    }
    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis() == getBlockState().getValue(AXIS);
    }
}
