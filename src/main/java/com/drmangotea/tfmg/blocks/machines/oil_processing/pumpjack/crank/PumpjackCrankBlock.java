package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.crank;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PumpjackCrankBlock extends HorizontalDirectionalBlock implements IBE<PumpjackCrankBlockEntity> {
    public PumpjackCrankBlock(Properties p_54120_) {
        super(p_54120_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54779_) {
        return this.defaultBlockState().setValue(FACING, p_54779_.getHorizontalDirection());
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return AllShapes.CASING_14PX.get(Direction.UP);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    @Override
    public Class<PumpjackCrankBlockEntity> getBlockEntityClass() {
        return PumpjackCrankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpjackCrankBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.PUMPJACK_CRANK.get();
    }
}
