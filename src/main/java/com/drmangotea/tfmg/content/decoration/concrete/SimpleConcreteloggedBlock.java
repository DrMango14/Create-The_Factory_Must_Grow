package com.drmangotea.tfmg.content.decoration.concrete;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public class SimpleConcreteloggedBlock extends Block implements ConcreteloggedBlock {


    public SimpleConcreteloggedBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(this.getStateDefinition().any().setValue(CONCRETELOGGED, false));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return fluidState(state);
    }
    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
                                  LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        updateConcrete(pLevel, pState, pCurrentPos);
        return pState;
    }



    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return onClicked(level, pos, state, player, hand);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return withConcrete(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        tickDrying(level,state,TFMGBlocks.REBAR_CONCRETE.block.getDefaultState(),pos, randomSource);
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONCRETELOGGED);
    }


}
