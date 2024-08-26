package com.drmangotea.tfmg.blocks.electricity.cable_blocks;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.RotatedPillarBlock.AXIS;

public class CableTubeBlockEntity extends ElectricBlockEntity implements IElectric {
    public CableTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis() == getBlockState().getValue(AXIS);
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CABLE_TUBE.get());
    }
}
