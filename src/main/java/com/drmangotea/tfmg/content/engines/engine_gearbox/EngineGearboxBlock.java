package com.drmangotea.tfmg.content.engines.engine_gearbox;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EngineGearboxBlock extends HorizontalKineticBlock implements IBE<EngineGearboxBlockEntity> {
    public EngineGearboxBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING)||face.getAxis() ==state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return TFMGShapes.ENGINE_GEARBOX.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    }

    @Override
    public Class<EngineGearboxBlockEntity> getBlockEntityClass() {
        return EngineGearboxBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EngineGearboxBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ENGINE_GEARBOX.get();
    }
}
