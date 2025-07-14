package com.drmangotea.tfmg.content.engines.types.turbine_engine;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.engines.base.EngineBlock;
import com.drmangotea.tfmg.content.engines.types.radial_engine.RadialEngineBlock;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TurbineEngineBlock extends RadialEngineBlock {
    public TurbineEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face==state.getValue(SHAFT_FACING)&&(state.getValue(ENGINE_STATE)==EngineState.BACK||state.getValue(ENGINE_STATE)==EngineState.SINGLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos pos, CollisionContext p_60558_) {
        return switch (state.getValue(ENGINE_STATE)){
            case NORMAL -> TFMGShapes.TURBINE_ENGINE_MIDDLE.get(state.getValue(SHAFT_FACING));
            case BACK, SINGLE -> TFMGShapes.TURBINE_ENGINE_BACK.get(state.getValue(SHAFT_FACING).getOpposite());
            case SHAFT -> TFMGShapes.TURBINE_ENGINE_FRONT.get(state.getValue(SHAFT_FACING).getOpposite());
        };
    }

    @Override
    public BlockEntityType<? extends RegularEngineBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TURBINE_ENGINE.get();
    }

}
