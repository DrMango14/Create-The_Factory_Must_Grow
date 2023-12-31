package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillation_tower;


import com.drmangotea.createindustry.blocks.tanks.SteelTankBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;


import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.StateDefinition;

public class DistillationControllerBlock extends FaceAttachedHorizontalDirectionalBlock
        implements  IWrenchable, IBE<DistillationControllerBlockEntity> {


    public DistillationControllerBlock(Properties properties) {
        super(properties);

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACE, FACING));
    }


    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, getConnectedDirection(pState).getOpposite());
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {

        return true;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        SteelTankBlock.updateTowerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));

    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity()))
            world.removeBlockEntity(pos);
        SteelTankBlock.updateTowerState(state, world, pos.relative(getFacing(state).getOpposite()));

    }



    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static Direction getFacing(BlockState sideState) {
        return getConnectedDirection(sideState);
    }


    @Override
    public Class<DistillationControllerBlockEntity> getBlockEntityClass() {
        return DistillationControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DistillationControllerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STEEL_DISTILLATION_CONTROLLER.get();
    }

}
