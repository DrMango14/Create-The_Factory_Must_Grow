package com.drmangotea.tfmg.content.engines.types.radial_engine;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlock;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadialEngineBlock extends RegularEngineBlock {
    public RadialEngineBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ENGINE_STATE, EngineState.SINGLE));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {

        if(state.getValue(ENGINE_STATE)==EngineState.SINGLE)
            return face.getAxis() == state.getValue(SHAFT_FACING).getAxis();

        return face==state.getValue(SHAFT_FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext p_60558_) {
        return switch (state.getValue(ENGINE_STATE)){
            case NORMAL -> TFMGShapes.RADIAL_ENGINE_MIDDLE.get(state.getValue(HORIZONTAL_FACING));
            case SHAFT -> TFMGShapes.RADIAL_ENGINE_SIDE.get(state.getValue(HORIZONTAL_FACING).getOpposite());
            case BACK -> TFMGShapes.RADIAL_ENGINE_SIDE.get(state.getValue(HORIZONTAL_FACING));
            case SINGLE -> TFMGShapes.RADIAL_ENGINE_SINGLE.get(state.getValue(HORIZONTAL_FACING));
        };
    }

    @Override
    public BlockEntityType<? extends RegularEngineBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.RADIAL_ENGINE.get();
    }
}
