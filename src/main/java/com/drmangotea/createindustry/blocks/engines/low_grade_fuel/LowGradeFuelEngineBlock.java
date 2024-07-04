package com.drmangotea.createindustry.blocks.engines.low_grade_fuel;



import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LowGradeFuelEngineBlock extends DirectionalKineticBlock implements IBE<LowGradeFuelEngineBlockEntity> {


    public LowGradeFuelEngineBlock(Properties properties) {
        super(properties);


    }


/*
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.MOTOR_BLOCK.get(state.getValue(FACING));
    }

 */


    @Override
public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

     if(pState.getValue(FACING).getAxis()==Axis.Y) {
         return TFMGShapes.COMPACT_ENGINE_VERTICAL.get(pState.getValue(FACING));
     }else
         return TFMGShapes.COMPACT_ENGINE.get(pState.getValue(FACING));
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
        return face == state.getValue(FACING);
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
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {


        if (level.getBlockEntity(pos) instanceof LowGradeFuelEngineBlockEntity be)
            if (be.playerInteract(pPlayer, pHand))
                return InteractionResult.SUCCESS;

        return super.use(pState,level,pos,pPlayer,pHand,pHit);

    }

    @Override
    public Class<LowGradeFuelEngineBlockEntity> getBlockEntityClass() {
        return LowGradeFuelEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LowGradeFuelEngineBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LOW_GRADE_FUEL_ENGINE.get();
    }
}
