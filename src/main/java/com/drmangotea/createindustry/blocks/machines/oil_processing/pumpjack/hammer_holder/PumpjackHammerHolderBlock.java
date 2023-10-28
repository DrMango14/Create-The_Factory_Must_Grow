package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer_holder;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class PumpjackHammerHolderBlock extends HorizontalDirectionalBlock implements IBE<PumpjackHammerHolderBlockEntity> {

    public PumpjackHammerHolderBlock(Properties properties) {
        super(properties);
    }



    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54794_) {
        p_54794_.add(FACING);
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_54779_) {
        return this.defaultBlockState().setValue(FACING, p_54779_.getHorizontalDirection());
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    @Override
    public Class<PumpjackHammerHolderBlockEntity> getBlockEntityClass() {
        return PumpjackHammerHolderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpjackHammerHolderBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.PUMPJACK_HAMMER_HOLDER.get();
    }




}
