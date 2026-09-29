package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CableBlockEntity extends AbstractCableBlockEntity {
    public CableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        List<Direction> directions = new ArrayList<>();
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(RotatedPillarBlock.AXIS));
        directions.add(direction);
        directions.add(direction.getOpposite());
        this.p = new CableProperties(getPos(), directions);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }
}
