package com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlastStoveBlock extends HorizontalDirectionalBlock implements IBE<BlastStoveBlockEntity> {
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public BlastStoveBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(RUNNING, false));
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection().getOpposite()).setValue(RUNNING, false);
    }
    @Override
    public Class<BlastStoveBlockEntity> getBlockEntityClass() {
        return BlastStoveBlockEntity.class;
    }
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    @Override
    public BlockEntityType<? extends BlastStoveBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.BLAST_STOVE.get();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, RUNNING));
    }
}
