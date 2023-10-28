package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PumpjackCrankBlock extends HorizontalDirectionalBlock implements IBE<PumpjackCrankBlockEntity>, IWrenchable {
    public PumpjackCrankBlock(Properties p_54120_) {
        super(p_54120_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54779_) {
        return this.defaultBlockState().setValue(FACING, p_54779_.getHorizontalDirection());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return TFMGShapes.PUMPJACK_CRANK;
    }
    @Override
    public Class<PumpjackCrankBlockEntity> getBlockEntityClass() {
        return PumpjackCrankBlockEntity.class;
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    @Override
    public BlockEntityType<? extends PumpjackCrankBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.PUMPJACK_CRANK.get();
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54794_) {
        p_54794_.add(FACING);
    }
}
