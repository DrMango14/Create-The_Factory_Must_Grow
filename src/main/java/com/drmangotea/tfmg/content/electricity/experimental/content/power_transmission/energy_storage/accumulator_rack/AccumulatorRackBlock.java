package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AccumulatorRackBlock extends TFMGDirectionalBlock implements IBE<AccumulatorRackBlockEntity> {
    public AccumulatorRackBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return onBlockEntityUse(level, pos, be -> be.use(player).result());
    }
    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {

        withBlockEntityDo(level, pos, b -> b.refreshNextTick = true);
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        withBlockEntityDo(level, pos, b -> b.refreshNextTick = true);

    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState1, boolean a) {


        withBlockEntityDo(level, pos, AccumulatorRackBlockEntity::onPlaced);
        withBlockEntityDo(level, pos, AccumulatorRackBlockEntity::refreshMultiblock);
        withBlockEntityDo(level, pos, b -> b.refreshNextTick = true);

        super.onPlace(state, level, pos, blockState1, a);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockState(pos).is(TFMGBlocks.ACCUMULATOR_RACK.get()))
                withBlockEntityDo(level, neighborPos, AccumulatorRackBlockEntity::refreshMultiblock);

        }
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public Class<AccumulatorRackBlockEntity> getBlockEntityClass() {
        return AccumulatorRackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AccumulatorRackBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.ACCUMULATOR_RACK.get();
    }

}
