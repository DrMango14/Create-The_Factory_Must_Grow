package com.drmangotea.tfmg.content.electricity;


import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.AbstractCableBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.CableProperties;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.content.electricity.DiagonalCableBlock.FACING_PRIMARY;
import static com.drmangotea.tfmg.content.electricity.DiagonalCableBlock.FACING_SECONDARY;

public class DiagonalCableBlockEntity extends AbstractCableBlockEntity {
    public DiagonalCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        List<Direction> directions = new ArrayList<>();
        directions.add(state.getValue(FACING_PRIMARY));
        directions.add(state.getValue(FACING_SECONDARY));
        p = new CableProperties(getPos(), directions);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }


}
