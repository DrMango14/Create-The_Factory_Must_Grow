package com.drmangotea.tfmg.blocks.engines.radial;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadialEngineBlock extends DirectionalKineticBlock implements IBE<RadialEngineBlockEntity> {


    public RadialEngineBlock(Properties properties) {
        super(properties);


    }


    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();


        return onBlockEntityUse(level, pos, be -> {

            if(be.spawnInput(direction))
                return InteractionResult.SUCCESS;

            return InteractionResult.FAIL;
        });


    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

            return TFMGShapes.RADIAL_ENGINE.get(pState.getValue(FACING));
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState()
                .setValue(FACING, preferred)
                //.setValue(BACK_PART,false)
                ;
    }

    // IRotate:

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(FACING).getAxis();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }


    @Override
    public Class<RadialEngineBlockEntity> getBlockEntityClass() {
        return RadialEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RadialEngineBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.RADIAL_ENGINE.get();
    }
}
