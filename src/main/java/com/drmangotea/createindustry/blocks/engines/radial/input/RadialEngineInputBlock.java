package com.drmangotea.createindustry.blocks.engines.radial.input;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadialEngineInputBlock extends DirectionalBlock implements IBE<RadialEngineInputBlockEntity> {
    public RadialEngineInputBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return TFMGShapes.EMPTY;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

            Direction nearestLookingDirection = context.getNearestLookingDirection();
            return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
                    .isShiftKeyDown() ? nearestLookingDirection : nearestLookingDirection.getOpposite());


    }



    @Override
    public Class<RadialEngineInputBlockEntity> getBlockEntityClass() {
        return RadialEngineInputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RadialEngineInputBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.RADIAL_ENGINE_INPUT.get();
    }
}
