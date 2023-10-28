package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class MachineInputBlock extends DirectionalKineticBlock implements IBE<MachineInputBlockEntity> {



    public MachineInputBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }


    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<MachineInputBlockEntity> getBlockEntityClass() {
        return MachineInputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MachineInputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.MACHINE_INPUT.get();
    }
}
